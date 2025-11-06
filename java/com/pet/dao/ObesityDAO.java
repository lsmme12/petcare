package com.pet.dao;

import com.pet.config.DBConnPool;
import com.pet.dto.ObesityDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 반려동물 비만도 관련 데이터를 DB에서 읽고 쓰는 DAO 클래스입니다.
 *
 * 초보자 설명:
 * - 사용자가 폼에 입력한 비만도 체크 항목들을 DB에 저장(insert)하거나,
 *   특정 반려동물의 목록을 조회(listByPet), 최신 항목을 조회(latestByPet)합니다.
 * - ResultSet을 ObesityDTO 객체로 변환하는 map() 도우미 메서드를 제공합니다.
 */
public class ObesityDAO extends DBConnPool {

    public ObesityDAO() { super(); }

    // 새 비만도 기록을 DB에 삽입합니다.
    public int insert(ObesityDTO dto) {
        int result = 0;
        if (con == null) {
            System.err.println("[ObesityDAO] DB connection is null. Insert aborted.");
            return 0;
        }
        String sql = "INSERT INTO PET_OBESITY (pet_id, check_1, check_2, check_3, check_4, check_5) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, dto.getPetId());
            psmt.setString(2, dto.getCheck1());
            psmt.setString(3, dto.getCheck2());
            psmt.setString(4, dto.getCheck3());
            psmt.setString(5, dto.getCheck4());
            psmt.setString(6, dto.getCheck5());
            result = psmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return result;
    }

    // 특정 반려동물의 비만도 기록을 최근 순으로(limit 개수) 조회합니다.
    public List<ObesityDTO> listByPet(int petId, int limit) {
        List<ObesityDTO> list = new ArrayList<>();
        if (con == null) {
            System.err.println("[ObesityDAO] DB connection is null. Returning empty list.");
            return list;
        }
        String sql = "SELECT * FROM (" +
                     "  SELECT t.*, ROW_NUMBER() OVER(ORDER BY created_at DESC) rn " +
                     "  FROM PET_OBESITY t WHERE pet_id = ?" +
                     ") WHERE rn <= ?";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, petId);
            psmt.setInt(2, limit);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ObesityDTO o = map(rs); // ResultSet -> DTO 변환
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return list;
    }

    // 특정 반려동물의 최신 비만도 기록 1건을 조회합니다.
    public ObesityDTO latestByPet(int petId) {
        ObesityDTO o = null;
        if (con == null) {
            System.err.println("[ObesityDAO] DB connection is null. Returning null for latestByPet.");
            return null;
        }
        String sql = "SELECT * FROM PET_OBESITY WHERE pet_id = ? ORDER BY created_at DESC FETCH FIRST 1 ROWS ONLY";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, petId);
            rs = psmt.executeQuery();
            if (rs.next()) o = map(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return o;
    }

    // ResultSet에서 값을 꺼내 ObesityDTO로 매핑해주는 도우미 메서드
    private ObesityDTO map(ResultSet rs) throws SQLException {
        ObesityDTO o = new ObesityDTO();
        o.setObesityId(rs.getInt("obesity_id"));
        o.setPetId(rs.getInt("pet_id"));
        o.setCheck1(rs.getString("check_1"));
        o.setCheck2(rs.getString("check_2"));
        o.setCheck3(rs.getString("check_3"));
        o.setCheck4(rs.getString("check_4"));
        o.setCheck5(rs.getString("check_5"));
        o.setTotalScore(rs.getInt("total_score"));
        o.setBcsGrade(rs.getString("bcs_grade"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}
