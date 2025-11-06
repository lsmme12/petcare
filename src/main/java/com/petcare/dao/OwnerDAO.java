package com.petcare.dao;

import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;
import com.petcare.dto.OwnerVO;

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

    /** ?꾩씠??議댁옱 ?щ? (true=議댁옱) */
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

    /** ?꾩씠?붾줈 ??紐?議고쉶 (濡쒓렇?몄슜) */
    public OwnerVO getOwnerById(String userid) {
        String sql = "SELECT owner_id, name, user_id, nickname, email, password " +
                     "FROM OWNER WHERE user_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OwnerVO vo = new OwnerVO();
                    vo.setOwnerId(rs.getInt("owner_id"));
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

    /** ?뚯썝媛??(?깃났 ??1 諛섑솚) */
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
            // ?좊땲???쒖빟(?꾩씠???됰꽕???대찓??以묐났 ??
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** ?뚯썝?뺣낫 ?섏젙 (PK??user_id 湲곗?) */
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
