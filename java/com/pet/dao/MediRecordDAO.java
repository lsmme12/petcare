package com.pet.dao;

import com.pet.config.DBConnPool;
import com.pet.dto.MediRecordDTO;
import java.sql.*;
import java.util.*;

/**
 * 투약기록(MEDI_RECORD) 테이블에 접근하는 DAO 클래스입니다.
 *
 * 초보자 설명:
 * - DAO(Data Access Object)는 DB와 직접 통신하는 역할을 합니다.
 * - 이 클래스는 DB에서 투약기록을 조회(list), 추가(insert), 수정(update), 삭제(delete) 합니다.
 * - DB 연결과 자원 관리는 상위 클래스(DBConnPool)에 맡겨져 있습니다.
 */
public class MediRecordDAO extends DBConnPool {

    public MediRecordDAO() { super(); }

    /**
     * 특정 반려동물(petId)의 투약기록 목록을 최신 순으로 조회합니다.
     * @param petId 조회할 반려동물 ID
     * @return 투약기록 리스트 (없으면 빈 리스트)
     */
    public List<MediRecordDTO> list(int petId) {
        List<MediRecordDTO> list = new ArrayList<>();
        if (con == null) {
            System.err.println("[MediRecordDAO] DB connection is null. Check DataSource (JNDI jdbc/myoracle). Returning empty list.");
            return list;
        }
        String sql = "SELECT record_id, medicine, dosage_time FROM MEDI_RECORD WHERE pet_id = ? ORDER BY record_id DESC";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, petId);
            rs = psmt.executeQuery();
            while (rs.next()) {
                MediRecordDTO dto = new MediRecordDTO();
                dto.setRecordId(rs.getInt("record_id"));
                dto.setMedicine(rs.getString("medicine"));
                dto.setDosageTime(rs.getTimestamp("dosage_time"));
                list.add(dto);
            }
        } catch (Exception e) {
            // 에러는 콘솔에 출력합니다. 개발 중에 발생 원인을 확인할 때 도움됩니다.
            e.printStackTrace();
        } finally { close(); } // 사용한 DB 리소스는 반드시 닫습니다.
        return list;
    }

    /**
     * 투약기록을 1건 등록합니다.
     * @param dto 등록할 데이터(약품명, 투약시각)
     * @param petId 대상 반려동물 ID
     * @return 영향 받은 행 수 (성공 시 1)
     */
    public int insert(MediRecordDTO dto, int petId) {
        int result = 0;
        if (con == null) {
            System.err.println("[MediRecordDAO] DB connection is null. Insert aborted.");
            return 0;
        }
        String sql = "INSERT INTO MEDI_RECORD (pet_id, medicine, dosage_time) VALUES (?, ?, ?)";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, petId);
            psmt.setString(2, dto.getMedicine());
            // java.util.Date -> java.sql.Timestamp로 변환해서 저장
            psmt.setTimestamp(3, new Timestamp(dto.getDosageTime().getTime()));
            result = psmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return result;
    }

    /**
     * 투약기록을 1건 삭제합니다.
     * @param recordId 삭제 대상 기록 ID
     * @return 영향 받은 행 수 (성공 시 1)
     */
    public int delete(int recordId) {
        int result = 0;
        if (con == null) {
            System.err.println("[MediRecordDAO] DB connection is null. Delete aborted.");
            return 0;
        }
        String sql = "DELETE FROM MEDI_RECORD WHERE record_id = ?";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, recordId);
            result = psmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return result;
    }

    /**
     * 투약기록을 수정합니다.
     * @param dto 수정할 데이터(레코드ID, 약품명, 투약시각)
     * @return 영향 받은 행 수 (성공 시 1)
     */
    public int update(MediRecordDTO dto) {
        int result = 0;
        if (con == null) {
            System.err.println("[MediRecordDAO] DB connection is null. Update aborted.");
            return 0;
        }
        String sql = "UPDATE MEDI_RECORD SET medicine = ?, dosage_time = ? WHERE record_id = ?";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, dto.getMedicine());
            psmt.setTimestamp(2, new Timestamp(dto.getDosageTime().getTime()));
            psmt.setInt(3, dto.getRecordId());
            result = psmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return result;
    }
}