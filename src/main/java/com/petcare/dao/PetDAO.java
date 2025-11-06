package com.petcare.dao;

import java.util.ArrayList;
import java.util.List;

import com.petcare.config.DBConnPool;
import com.petcare.dto.PetDTO;


/**
 * 반려동물(PET) 테이블을 조회하는 DAO입니다.
 *
 * 초보자 설명:
 * - DAO는 데이터베이스에 필요한 SQL을 보내고 결과를 받아 자바 객체로 바꿔줍니다.
 * - 이 클래스는 반려동물 정보를 가져오는 메서드(getPetById, getPetsByOwner)를 제공합니다.
 * - DB 연결은 상위 클래스(DBConnPool)가 처리하므로 con, psmt, rs를 바로 사용합니다.
 */
public class PetDAO extends DBConnPool {
    public PetDAO() { super(); }

    /**
     * pet_id로 반려동물 1건을 조회합니다.
     *
     * 초보자 포인트:
     * - SQL 결과가 있으면 ResultSet에서 값을 꺼내서 PetDTO에 채워 넣습니다.
     * - DB 연결이 실패(con == null)하면 바로 빈(null) 반환으로 안전하게 처리합니다.
     *
     * @param petId 반려동물 ID
     * @return 존재하면 PetDTO, 없으면 null
     */
    public PetDTO getPetById(int petId) {
        PetDTO pet = null;
        // Guard: if DB connection failed during DBConnPool construction, avoid NPE and give clear log
        if (con == null) {
            System.err.println("[PetDAO] DB connection is null. Check Tomcat DataSource (JNDI name 'jdbc/oracle') and context.xml");
            return null;
        }
        String sql = "SELECT pet_id, owner_id, pet_name, breed_code, sex, birth_date, weight_kg FROM PET WHERE pet_id = ?";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, petId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                pet = new PetDTO();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setOwnerId(rs.getInt("owner_id"));
                pet.setPetName(rs.getString("pet_name"));
                pet.setBreedCode(rs.getString("breed_code"));
                pet.setSex(rs.getString("sex"));
                try { pet.setBirthDate(rs.getString("birth_date")); } catch(Exception ignore){}
                pet.setWeightKg(rs.getDouble("weight_kg"));
            }
        } catch(Exception e) {
            // 개발 중엔 에러 로그를 보고 문제를 찾습니다.
            e.printStackTrace();
        } finally { close(); }
        return pet;
    }

    /**
     * 소유자(ownerId)가 등록한 반려동물 목록을 조회합니다.
     *
     * 초보자 포인트:
     * - 여러 행이 나올 수 있으므로 while(rs.next())로 반복해서 리스트에 추가합니다.
     * - 결과가 없으면 빈 리스트를 반환하므로 호출하는 쪽에서 null 체크 불필요합니다.
     *
     * @param ownerId 소유자 ID
     * @return 등록된 반려동물 목록 (없으면 빈 리스트)
     */
    public List<PetDTO> getPetsByOwner(int ownerId) {
        List<PetDTO> list = new ArrayList<>();
        if (con == null) {
            System.err.println("[PetDAO] DB connection is null. Cannot fetch pets by owner. Check DataSource 'jdbc/urdb'.");
            return list;
        }
        String sql = "SELECT pet_id, owner_id, pet_name, breed_code, sex, birth_date, weight_kg FROM PET WHERE owner_id=? ORDER BY pet_id";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, ownerId);
            rs = psmt.executeQuery();
            while(rs.next()) {
                PetDTO pet = new PetDTO();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setOwnerId(rs.getInt("owner_id"));
                pet.setPetName(rs.getString("pet_name"));
                pet.setBreedCode(rs.getString("breed_code"));
                pet.setSex(rs.getString("sex"));
                try { pet.setBirthDate(rs.getString("birth_date")); } catch(Exception ignore){}
                pet.setWeightKg(rs.getDouble("weight_kg"));
                list.add(pet);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally { close(); }
        return list;
    }
}