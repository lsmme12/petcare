package com.pet.dao;

import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;
import com.pet.dto.OwnerVO;

public class OwnerDAO {
    private OwnerDAO() {}
    private static final OwnerDAO instance = new OwnerDAO();
    public static OwnerDAO getInstance() { return instance; }

    private Connection getConnection() throws Exception {
        Context initCtx = new InitialContext();
        Context envCtx  = (Context) initCtx.lookup("java:comp/env");
        DataSource ds   = (DataSource) envCtx.lookup("jdbc/myoracle");
        return ds.getConnection();
    }

    /** 아이디 존재 여부 (true=존재) */
    public boolean confirmID(String userid) {
        String sql = "SELECT 1 FROM OWNER WHERE user_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /** 아이디로 한 명 조회 (로그인용) */
    public OwnerVO getOwnerById(String userid) {
        String sql = "SELECT owner_id, name, user_id, nickname, email, password " +
                     "FROM OWNER WHERE user_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OwnerVO vo = new OwnerVO();
                    // owner_id는 세션 사용을 위해 별도 헬퍼에서 꺼내도 되지만, 여기서는 VO에 담지 않고 반환
                    vo.setUserid(rs.getString("user_id"));
                    vo.setName(rs.getString("name"));
                    vo.setNickname(rs.getString("nickname"));
                    vo.setEmail(rs.getString("email"));
                    vo.setPwd(rs.getString("password"));
                    return vo;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /** user_id로 owner_id(PK) 조회 */
    public Integer getOwnerIdByUserId(String userid) {
        String sql = "SELECT owner_id FROM OWNER WHERE user_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("owner_id");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /** 회원가입 (성공 시 1 반환) */
    public int insertOwner(OwnerVO vo) {
        String sql = "INSERT INTO OWNER (name, user_id, nickname, email, password) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vo.getName());
            ps.setString(2, vo.getUserid());
            ps.setString(3, vo.getNickname());
            ps.setString(4, vo.getEmail());
            ps.setString(5, vo.getPwd());
            return ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException dup) {
            // 유니크 제약(아이디/닉네임/이메일 중복 등)
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** 회원정보 수정 (PK는 user_id 기준) */
    public int updateOwner(OwnerVO vo) {
        String sql = "UPDATE OWNER " +
                     "   SET name     = ?, " +
                     "       nickname = ?, " +
                     "       email    = ?, " +
                     "       password = ? " +
                     " WHERE user_id  = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vo.getName());
            ps.setString(2, vo.getNickname());
            ps.setString(3, vo.getEmail());
            ps.setString(4, vo.getPwd());
            ps.setString(5, vo.getUserid());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
