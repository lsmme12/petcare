package com.pet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pet.dto.PetDTO;

public class PetDAO {
	public static void main(String[] args) {
	    PetDAO dao = new PetDAO();
	    PetDTO pet = dao.getPetById(1);
	    if (pet != null) {
	        System.out.println("Pet found: " + pet.getPetName() + ", " + pet.getSex());
	    } else {
	        System.out.println("Pet not found!");
	    }
	}


    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "petcare";
    private static final String PASSWORD = "1234";

    // pet_id로 단일 조회
    public PetDTO getPetById(int petId) {
    	String sql = "SELECT PET_ID, PET_NAME, SEX, BIRTH_DATE, WEIGHT_KG FROM PET WHERE PET_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Found pet_id=" + rs.getInt("PET_ID"));  // 디버그 출력
                PetDTO pet = new PetDTO();
                pet.setPetId(rs.getInt("PET_ID"));                           // 대문자 컬럼명
                pet.setPetName(rs.getString("PET_NAME"));                   // 대문자 컬럼명
                pet.setSex(rs.getString("SEX"));
                pet.setBirthDate(rs.getDate("BIRTH_DATE"));
                pet.setWeightKg(rs.getDouble("WEIGHT_KG"));
                return pet;
            } else {
                System.out.println("No pet found for pet_id=" + petId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 다음 pet_id 존재 여부 확인
    public boolean nextPetExists(int petId) {
        String sql = "SELECT 1 FROM PET WHERE pet_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, petId + 1);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
