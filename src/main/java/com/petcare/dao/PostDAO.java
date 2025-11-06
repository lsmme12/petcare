package com.petcare.dao;

import java.sql.*;
import java.util.*;

import com.petcare.config.DBConnPool;
import com.petcare.dto.PostDTO;

public class PostDAO extends DBConnPool {

    public PostDAO() {
        super();
        System.out.println("[PostDAO] connection opened (con=" + con + ")");
    }

    public List<PostDTO> selectPaged(int page, int limit) {
        List<PostDTO> list = new ArrayList<>();
        int offset = (page - 1) * limit;
        int upperBound = offset + limit;

        String sql = """
            SELECT * FROM (
                SELECT inner_query.*, ROWNUM rn FROM (
                    SELECT 
                        p.post_id,
                        p.owner_id,
                        o.nickname AS owner_nickname,
                        p.title,
                        p.content,
                        p.created_at,
                        NVL((SELECT COUNT(*) FROM post_view v WHERE v.post_id = p.post_id), 0) AS view_count,
                        NVL((SELECT COUNT(*) FROM post_like l WHERE l.post_id = p.post_id), 0) AS like_count,
                        NVL((SELECT COUNT(*) FROM post_comment c WHERE c.post_id = p.post_id), 0) AS comment_count
                    FROM post p
                    JOIN owner o ON p.owner_id = o.owner_id
                    ORDER BY p.post_id DESC
                ) inner_query
                WHERE ROWNUM <= ?
            )
            WHERE rn > ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, upperBound);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostDTO dto = new PostDTO();
                    dto.setPostId(rs.getInt("post_id"));
                    dto.setOwnerId(rs.getInt("owner_id"));
                    dto.setOwnerNickname(rs.getString("owner_nickname"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setViewCount(rs.getInt("view_count"));
                    dto.setLikeCount(rs.getInt("like_count"));
                    dto.setCommentCount(rs.getInt("comment_count"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            System.out.println("[PostDAO] selectPaged 실패");
            e.printStackTrace();
        }
        return list;
    }

    public int countPosts() {
        String sql = "SELECT COUNT(*) FROM post";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("[PostDAO] countPosts 실패");
            e.printStackTrace();
        }
        return 0;
    }

    public PostDTO selectOne(int postId) {
        PostDTO dto = null;
        String sql = """
            SELECT 
                p.post_id,
                p.owner_id,
                o.nickname AS owner_nickname,
                p.title,
                p.content,
                p.created_at,
                NVL((SELECT COUNT(*) FROM post_view v WHERE v.post_id = p.post_id), 0) AS view_count,
                NVL((SELECT COUNT(*) FROM post_like l WHERE l.post_id = p.post_id), 0) AS like_count
            FROM post p
            JOIN owner o ON p.owner_id = o.owner_id
            WHERE p.post_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new PostDTO();
                    dto.setPostId(rs.getInt("post_id"));
                    dto.setOwnerId(rs.getInt("owner_id"));
                    dto.setOwnerNickname(rs.getString("owner_nickname"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setViewCount(rs.getInt("view_count"));
                    dto.setLikeCount(rs.getInt("like_count"));
                }
            }
        } catch (Exception e) {
            System.out.println("[PostDAO] selectOne 실패");
            e.printStackTrace();
        }
        return dto;
    }

    public void insertView(int postId, Integer ownerId) {
        String sql = "INSERT INTO post_view (post_id, owner_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, postId);
            if (ownerId == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, ownerId);
            }
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // 중복 조회는 무시
        } catch (Exception e) {
            System.out.println("[PostDAO] insertView 실패");
            e.printStackTrace();
        }
    }

    public boolean toggleLike(int postId, int ownerId) {
        String checkSql = "SELECT 1 FROM post_like WHERE post_id = ? AND owner_id = ?";
        try (PreparedStatement check = con.prepareStatement(checkSql)) {
            check.setInt(1, postId);
            check.setInt(2, ownerId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    String deleteSql = "DELETE FROM post_like WHERE post_id = ? AND owner_id = ?";
                    try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                        del.setInt(1, postId);
                        del.setInt(2, ownerId);
                        del.executeUpdate();
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("[PostDAO] toggleLike 확인 실패");
            e.printStackTrace();
        }

        String insertSql = "INSERT INTO post_like (post_id, owner_id) VALUES (?, ?)";
        try (PreparedStatement ins = con.prepareStatement(insertSql)) {
            ins.setInt(1, postId);
            ins.setInt(2, ownerId);
            ins.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("[PostDAO] toggleLike 등록 실패");
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> selectComments(int postId) {
        List<Map<String, Object>> comments = new ArrayList<>();
        String sql = """
            SELECT c.comment_id, c.post_id, c.owner_id, c.content, c.created_at, o.nickname
            FROM post_comment c
            JOIN owner o ON c.owner_id = o.owner_id
            WHERE c.post_id = ?
            ORDER BY c.comment_id ASC
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("comment_id", rs.getInt("comment_id"));
                    row.put("post_id", rs.getInt("post_id"));
                    row.put("owner_id", rs.getInt("owner_id"));
                    row.put("content", rs.getString("content"));
                    row.put("created_at", rs.getTimestamp("created_at"));
                    row.put("nickname", rs.getString("nickname"));
                    comments.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("[PostDAO] selectComments 실패");
            e.printStackTrace();
        }
        return comments;
    }

    public int insertPost(PostDTO dto) {
        int result = 0;
        try {
            int newId = nextPostId();
            dto.setPostId(newId);

            String sql = """
                INSERT INTO POST (POST_ID, OWNER_ID, TITLE, CONTENT, CREATED_AT)
                VALUES (?, ?, ?, ?, SYSDATE)
            """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setInt(2, dto.getOwnerId());
                ps.setString(3, dto.getTitle());
                ps.setString(4, dto.getContent());
                result = ps.executeUpdate();
            }
            System.out.println("[PostDAO] insertPost result=" + result);
        } catch (Exception e) {
            System.out.println("[PostDAO] 게시글 등록 실패 (insertPost)");
            e.printStackTrace();
        }
        return result;
    }

    public int updatePost(int postId, String title, String content) {
        String sql = """
            UPDATE post
               SET title = ?,
                   content = ?
             WHERE post_id = ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, postId);
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("[PostDAO] updatePost 실패");
            e.printStackTrace();
        }
        return 0;
    }

    public int deletePost(int postId) {
        String sql = "DELETE FROM post WHERE post_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, postId);
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("[PostDAO] deletePost 실패");
            e.printStackTrace();
        }
        return 0;
    }

    private int nextPostId() throws SQLException {
        final String seqSql = "SELECT POST_SEQ.NEXTVAL FROM dual";
        try (PreparedStatement ps = con.prepareStatement(seqSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ignore) {
            // 시퀀스가 없을 수도 있으니 아래 fallback 사용
        }

        final String maxSql = "SELECT COALESCE(MAX(post_id), 0) + 1 FROM POST";
        try (PreparedStatement ps = con.prepareStatement(maxSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 1;
    }
}
