// 📦 패키지 경로 : com.pet.config
//   → 프로젝트에서 "설정(config)"과 관련된 파일이 들어있는 폴더야.
package com.pet.config;

import java.sql.*;          // 🔹 DB 관련 핵심 클래스들 (Connection, PreparedStatement, ResultSet 등)
import javax.naming.*;      // 🔹 JNDI를 통해 context.xml의 자원을 찾기 위해 필요
import javax.sql.DataSource; // 🔹 커넥션 풀(DataSource) 사용을 위해 필요

/**
 * 📘 DBConnPool (JNDI + DriverManager Fallback)
 * ------------------------------------------------------
 * 💡 이 클래스는 데이터베이스와 연결을 담당하는 “공용 통로” 역할을 해요.
 *    기본적으로 톰캣의 context.xml에 등록된 DBCP 연결을 사용하고,
 *    만약 서버 환경에서 JNDI 연결이 실패하면, 로컬 개발환경용 직접 연결(DriverManager)을 자동으로 시도합니다.
 *
 * 🧩 DBCP (Database Connection Pool)
 *   → 미리 여러 개의 DB 연결을 만들어 두고, 필요할 때 빌려 쓰고 다시 돌려주는 시스템.
 *
 * 🧩 JNDI (Java Naming and Directory Interface)
 *   → 톰캣의 context.xml 파일에 등록된 DB 연결 리소스("jdbc/myoracle")를 찾아 쓰는 기능이에요.
 *
 * ⚙️ context.xml 예시:
 *   <Resource name="jdbc/myoracle"
 *             auth="Container"
 *             type="javax.sql.DataSource"
 *             driverClassName="oracle.jdbc.OracleDriver"
 *             url="jdbc:oracle:thin:@localhost:1521:xe"
 *             username="petcare"
 *             password="1234"
 *             maxTotal="20" />
 */

// =============================================================
// ✅ [1️⃣ 테스트용 버전 - 개발 중 콘솔 출력용]
// -------------------------------------------------------------
// 💡 개발 중엔 println으로 연결/해제 상태를 콘솔에서 바로 확인.
// 💡 배포 시엔 아래 “배포용 버전”으로 교체.
// =============================================================
public class DBConnPool {

    protected Connection con;         // 🪄 실제 DB 연결 통로
    protected PreparedStatement psmt; // 🧾 SQL 실행 객체
    protected ResultSet rs;           // 📋 SQL 실행 결과 저장소
    protected DataSource ds;          // 🧩 커넥션 풀 관리 객체

    public Connection getConnection() {
        return con;
    }

    public DBConnPool() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🚀 [DBConnPool] 데이터베이스 연결 시도 시작");

        // 1️⃣ JNDI 기반 DBCP 연결 시도
        try {
            System.out.println("🔍 1단계: InitialContext 생성 중...");
            Context initCtx = new InitialContext();

            System.out.println("🔍 2단계: java:comp/env 환경 조회 중...");
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            System.out.println("🔍 3단계: context.xml의 리소스(jdbc/myoracle) 조회 중...");
            ds = (DataSource) envCtx.lookup("jdbc/myoracle");

            con = ds.getConnection();
            System.out.println("✅ [DBConnPool] DBCP 연결 성공 (jdbc/myoracle)");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            return;
        } catch (Exception e) {
            System.out.println("⚠️ [DBConnPool] DBCP(JNDI) 연결 실패 → Fallback으로 전환");
            e.printStackTrace();
        }

        // 2️⃣ Fallback: DriverManager 직접 연결
        /*
        try {
            System.out.println("🔁 Fallback 경로 실행 중...");
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "petcare";
            String password = "1234";
            System.out.println("🔗 Oracle JDBC 드라이버 로드 완료. DB 연결 시도 중...");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("✅ [DBConnPool] DriverManager fallback 연결 성공");
        } catch (Exception e) {
            System.out.println("❌ [DBConnPool] DriverManager fallback 연결 실패");
            e.printStackTrace();
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        */
    }

    public void close() {
        System.out.println("🧹 [DBConnPool] DB 자원 해제 시도 중...");
        try {
            if (rs != null) {
                rs.close();
                System.out.println("   📋 ResultSet 닫힘");
            }
        } catch (Exception e) {
            System.out.println("⚠️ ResultSet 닫는 중 오류 발생");
        }

        try {
            if (psmt != null) {
                psmt.close();
                System.out.println("   🧾 PreparedStatement 닫힘");
            }
        } catch (Exception e) {
            System.out.println("⚠️ PreparedStatement 닫는 중 오류 발생");
        }

        try {
            if (con != null) {
                con.close();
                System.out.println("   🔌 Connection 반환 완료 (커넥션 풀로 복귀)");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Connection 닫는 중 오류 발생");
        }

        System.out.println("✅ [DBConnPool] 자원 해제 완료");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}



/* =============================================================
 * ✅ [2️⃣ 배포용 버전 - 운영 환경에서 사용할 코드]
 * -------------------------------------------------------------
 * 💡 println 로그를 전부 제거해 성능과 보안을 강화한 버전.
 * 💡 실제 서버에 배포할 땐, 위 테스트용을 삭제하고
 *    아래 주석을 풀어서 이 클래스를 활성화시키면 됩니다.
 * =============================================================

public class DBConnPool {

    protected Connection con;
    protected PreparedStatement psmt;
    protected ResultSet rs;
    protected DataSource ds;

    public Connection getConnection() { return con; }

    public DBConnPool() {
        try {
            // JNDI 기반 DBCP 연결 시도
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/myoracle");
            con = ds.getConnection();
            return;
        } catch (Exception e) {
            // 실패 시 fallback
        }

        try {
            // Fallback: 직접 연결
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "petcare";
            String password = "1234";
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace(); // 심각한 오류만 출력
        }
    }

    public void close() {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (psmt != null) psmt.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}

 * =============================================================
 */

