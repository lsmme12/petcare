package com.pet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pet.dto.ExerRecordDTO;

public class ExerRecordDAO {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "petcare";
    private static final String PASSWORD = "1234";

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC 드라이버 로드 실패: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ DB 연결 실패: " + e.getMessage());
            return null;
        }
    }

    // 운동기록 전체 조회
    public List<ExerRecordDTO> getAllRecords() {
        List<ExerRecordDTO> list = new ArrayList<>();
        String sql = """
            SELECT e.exer_id, e.pet_id, e.exer_date, e.exer_time, 
                   e.exer_level, e.memo, e.created_at, e.updated_at, 
                   p.pet_name
            FROM EXER_RECORD e 
            JOIN PET p ON e.pet_id = p.pet_id 
            ORDER BY e.exer_id DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ExerRecordDTO record = new ExerRecordDTO();
                record.setExerId(rs.getInt("exer_id")); // ✅ 이거 꼭 필요!
                record.setPetId(rs.getInt("pet_id"));
                record.setExerDate(rs.getDate("exer_date"));
                record.setExerTime(rs.getInt("exer_time"));
                record.setExerLevel(rs.getString("exer_level"));
                record.setMemo(rs.getString("memo"));
                record.setCreatedAt(rs.getTimestamp("created_at"));
                record.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 운동기록 배치 추가
    public int[] insertRecordsBatch(List<ExerRecordDTO> records) {
        String sql = "INSERT INTO EXER_RECORD (pet_id, exer_date, exer_time, exer_level, memo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // 트랜잭션 시작

            for (ExerRecordDTO record : records) {
                pstmt.setInt(1, record.getPetId());
                pstmt.setDate(2, record.getExerDate());
                pstmt.setInt(3, record.getExerTime());
                pstmt.setString(4, record.getExerLevel());
                pstmt.setString(5, record.getMemo());
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit(); // 한 번에 커밋
            return results;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    // 운동기록 삭제
    public int deleteRecord(int exerId) {
        String sql = "DELETE FROM EXER_RECORD WHERE exer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, exerId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
 // 운동기록 단건 조회
    public ExerRecordDTO getRecordById(int exerId) {
        String sql = "SELECT * FROM EXER_RECORD WHERE exer_id = ?";
        ExerRecordDTO record = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, exerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    record = new ExerRecordDTO();
                    record.setExerId(rs.getInt("exer_id"));
                    record.setPetId(rs.getInt("pet_id"));
                    record.setExerDate(rs.getDate("exer_date"));
                    record.setExerTime(rs.getInt("exer_time"));
                    record.setExerLevel(rs.getString("exer_level"));
                    record.setMemo(rs.getString("memo"));
                    record.setCreatedAt(rs.getTimestamp("created_at"));
                    record.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }

    // 운동기록 수정
    public int updateRecord(ExerRecordDTO record) {
        String sql = """
            UPDATE EXER_RECORD 
            SET exer_date = ?, exer_time = ?, exer_level = ?, memo = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE exer_id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, record.getExerDate());
            pstmt.setInt(2, record.getExerTime());
            pstmt.setString(3, record.getExerLevel());
            pstmt.setString(4, record.getMemo());
            pstmt.setInt(5, record.getExerId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
