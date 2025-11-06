package com.petcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.petcare.dto.PetDTO;

public class PetDAO {
	public static void main(String[] args) {
	    try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        System.out.println("✅ 연결 성공: " + con);
	        
	        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM PET");
	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        System.out.println("🐾 PET 테이블 행 수 = " + rs.getInt(1));
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public PetDTO getPetById(int petId) {
        String sql = "SELECT pet_id, owner_id, pet_name, breed_code, sex, birth_date, weight_kg FROM PET WHERE pet_id = ?";
        PetDTO pet = null;

        try (Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql)) {

            psmt.setInt(1, petId);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    pet = new PetDTO();
                    pet.setPetId(rs.getInt("pet_id"));
                    pet.setOwnerId(rs.getInt("owner_id"));
                    pet.setPetName(rs.getString("pet_name"));
                    pet.setBreedCode(rs.getString("breed_code"));
                    pet.setSex(rs.getString("sex"));
                    java.sql.Date sqlDate = rs.getDate("birth_date");
                    if (sqlDate != null) pet.setBirthDate(new Date(sqlDate.getTime()));
                    pet.setWeightKg(rs.getDouble("weight_kg"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pet;
    }

    public List<PetDTO> getPetsByOwner(int ownerId) {
        String sql = "SELECT pet_id, owner_id, pet_name, breed_code, sex, birth_date, weight_kg FROM PET WHERE owner_id=? ORDER BY pet_id";
        List<PetDTO> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql)) {

            psmt.setInt(1, ownerId);
            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    PetDTO pet = new PetDTO();
                    pet.setPetId(rs.getInt("pet_id"));
                    pet.setOwnerId(rs.getInt("owner_id"));
                    pet.setPetName(rs.getString("pet_name"));
                    pet.setBreedCode(rs.getString("breed_code"));
                    pet.setSex(rs.getString("sex"));
                    java.sql.Date sqlDate = rs.getDate("birth_date");
                    if (sqlDate != null) pet.setBirthDate(new Date(sqlDate.getTime()));
                    pet.setWeightKg(rs.getDouble("weight_kg"));
                    list.add(pet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean nextPetExists(int petId) {
        String sql = "SELECT 1 FROM PET WHERE pet_id = ?";
        boolean exists = false;

        try (Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql)) {

            psmt.setInt(1, petId + 1);
            try (ResultSet rs = psmt.executeQuery()) {
                exists = rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }
}
