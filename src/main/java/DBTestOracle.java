import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTestOracle {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:XE"; // XE 인스턴스
        String user = "petcare"; // DB 아이디
        String password = "1234"; // DB 비밀번호

        Connection conn = null;
        try {
            // 드라이버 로드 (Java 6 이상은 생략 가능)
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 연결
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Oracle DB 연결 성공!");
            } else {
                System.out.println("Oracle DB 연결 실패!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB 연결 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
