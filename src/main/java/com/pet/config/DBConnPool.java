// 📦 패키지 경로 : com.pet.config
//   → 프로젝트에서 "설정(config)"과 관련된 파일이 들어있는 폴더야.
package com.pet.config;

import java.sql.*;        // 🔹 DB와 대화할 때 필요한 기본 클래스들 (Connection, ResultSet 등)
import javax.naming.*;    // 🔹 JNDI를 이용해서 context.xml에 등록된 자원을 찾을 때 필요
import javax.sql.DataSource; // 🔹 커넥션 풀(DataSource)을 사용할 때 필요

/**
 * 📘 DBConnPool (JNDI 기반 DBCP 커넥션 풀)
 * ------------------------------------------------------
 * 💡 이 클래스는 데이터베이스와 연결해주는 “공용 통로” 역할을 해요.
 *     직접 연결하지 않고, 미리 만들어둔 연결을 "빌려쓰는 방식(DBCP)"이에요.
 *
 * 🧩 DBCP = Database Connection Pool
 *   → 미리 여러 개의 DB 연결을 만들어 두고, 필요할 때 꺼내 쓰고 다 쓰면 다시 돌려주는 시스템.
 *
 * 🧩 JNDI = Java Naming and Directory Interface
 *   → 톰캣의 context.xml 파일 안에 등록된 DB 연결 이름("jdbc/myoracle")을 찾아 쓰는 기능이에요.
 *
 * ⚙️ context.xml 안에는 이런 설정이 있어야 해요:
 * <Resource name="jdbc/myoracle"
 *           auth="Container"
 *           type="javax.sql.DataSource"
 *           driverClassName="oracle.jdbc.OracleDriver"
 *           url="jdbc:oracle:thin:@localhost:1521:xe"
 *           username="system"
 *           password="1234"
 *           maxTotal="20" />
 */
public class DBConnPool {

    // 🧱 DB 연결 관련 도구들을 변수로 만들어둠 (필요할 때마다 꺼내서 씀)
    protected Connection con;         // 🪄 실제로 DB와 연결되는 통로
    protected PreparedStatement psmt; // 🧾 SQL문(명령어)을 미리 준비해두는 객체
    protected ResultSet rs;           // 📋 SQL 실행 결과(조회 결과표)를 담는 상자
    protected DataSource ds;          // 🧩 커넥션 풀(DataSource)을 관리하는 객체

    /** ✅ Controller나 DAO 외부 접근용 Getter */
    public Connection getConnection() {
        // 💡 이 메서드를 부르면 DB 연결(con)을 다른 클래스에서 가져다 쓸 수 있어요.
        return con;
    }

    /** ✅ 기본 생성자: DBCP 풀에서 커넥션 가져오기 */
    public DBConnPool() {
        try {
            // 🧭 JNDI를 이용해 context.xml 안에 등록된 DB 자원을 찾는 과정이에요.

            Context initCtx = new InitialContext(); // 📍 1단계: JNDI 초기 환경 객체를 만듬.
            Context envCtx = (Context) initCtx.lookup("java:comp/env"); 
            // 📍 2단계: 톰캣 서버의 환경 설정(java:comp/env)을 찾음.

            ds = (DataSource) envCtx.lookup("jdbc/myoracle");
            // 📍 3단계: context.xml에 등록된 "jdbc/myoracle" 자원을 찾아서 DataSource로 받음.

            con = ds.getConnection();
            // 📍 4단계: 커넥션 풀에서 진짜 DB 연결을 하나 빌려옴.
            // System.out.println("✅ DBCP 연결 성공: jdbc/myoracle"); // 👉 연결 성공 확인용 출력 (지금은 주석처리)

        } catch (Exception e) {
            // 🚨 연결 과정에서 오류가 나면 여기가 실행돼요.
            System.out.println("❌ DBCP 연결 실패 (jdbc/myoracle)");
            e.printStackTrace(); // 어떤 오류인지 콘솔에 자세히 출력
        }
    }

    /** ✅ 자원 해제 메서드 */
    public void close() {
        try {
            // 💡 DB 연결은 꼭 다 쓴 다음 닫아줘야 해요. (안 닫으면 메모리 낭비!)
            if (rs != null) rs.close();     // 📋 조회 결과 닫기
            if (psmt != null) psmt.close(); // 🧾 SQL 실행 객체 닫기
            if (con != null) con.close();   // 🪄 DB 연결 반납하기 (커넥션 풀에 다시 돌려줌)
            // System.out.println("🔌 커넥션 반환 완료"); // 👉 확인용 출력 (주석처리)

        } catch (Exception e) {
            // 🚨 닫는 중에도 오류가 날 수 있어서 예외 처리함.
            System.out.println("❌ DB 자원 해제 실패");
            e.printStackTrace();
        }
    }
}
