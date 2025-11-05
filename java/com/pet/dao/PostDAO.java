package com.pet.dao;

import java.sql.*;
import java.util.*;
import com.pet.config.DBConnPool;
import com.pet.dto.PostDTO;

/**
 * 📘 PostDAO
 * - 게시글 CRUD + 조회수 + 좋아요 + 댓글 목록
 */
public class PostDAO extends DBConnPool {

    /** ✅ 생성자 : DBConnPool 연결 자동 실행 */
    public PostDAO() {
    	
        super(); // ⚙️ DBConnPool 생성자 호출
        System.out.println("✅ PostDAO 연결 성공 (con=" + con + ")");
        System.out.println("📡 현재 커넥션 객체: " + con);
    }

    /** ✅ 게시글 목록 */
    public List<PostDTO> selectAll() {
        List<PostDTO> list = new ArrayList<>();
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
            WHERE NVL(p.is_deleted, 'N') = 'N'
            ORDER BY p.post_id DESC
        """;
        try {
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                PostDTO dto = new PostDTO();
                dto.setPostId(rs.getInt("post_id"));
                dto.setOwnerId(rs.getInt("owner_id"));
                dto.setOwnerNickname(rs.getString("owner_nickname"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getDate("created_at"));
                dto.setViewCount(rs.getInt("view_count"));
                dto.setLikeCount(rs.getInt("like_count"));
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("❌ 게시글 목록 조회 실패");
            e.printStackTrace();
        } 
        return list;
    }

    /** ✅ 단일 게시글 조회 */
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
            WHERE p.post_id = ? AND p.is_deleted = 'N'
        """;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, postId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                dto = new PostDTO();
                dto.setPostId(rs.getInt("post_id"));
                dto.setOwnerId(rs.getInt("owner_id"));
                dto.setOwnerNickname(rs.getString("owner_nickname"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getDate("created_at"));
                dto.setViewCount(rs.getInt("view_count"));
                dto.setLikeCount(rs.getInt("like_count"));
            }
        } catch (Exception e) {
            System.out.println("❌ 게시글 상세 조회 실패");
            e.printStackTrace();
        } 
        return dto;
    }

    /** 👁 조회수 기록 */
    public void insertView(int postId, Integer ownerId) {
        String sql = "INSERT INTO post_view (post_id, owner_id) VALUES (?, ?)";
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, postId);
            if (ownerId == null) psmt.setNull(2, Types.INTEGER);
            else psmt.setInt(2, ownerId);
            psmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // 이미 조회한 경우 무시
        } catch (Exception e) {
            System.out.println("❌ 조회수 기록 실패");
            e.printStackTrace();
        } 
    }

    /** ❤️ 좋아요 토글 */
    public boolean toggleLike(int postId, int ownerId) {
        boolean liked = false;
        try {
            // 1️⃣ 현재 좋아요 했는지 확인
            String checkSql = "SELECT 1 FROM post_like WHERE post_id=? AND owner_id=?";
            psmt = con.prepareStatement(checkSql);
            psmt.setInt(1, postId);
            psmt.setInt(2, ownerId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                // 좋아요 취소
                String delSql = "DELETE FROM post_like WHERE post_id=? AND owner_id=?";
                psmt = con.prepareStatement(delSql);
                psmt.setInt(1, postId);
                psmt.setInt(2, ownerId);
                psmt.executeUpdate();
                liked = false;
            } else {
                // 좋아요 등록
                String insSql = "INSERT INTO post_like (post_id, owner_id) VALUES (?, ?)";
                psmt = con.prepareStatement(insSql);
                psmt.setInt(1, postId);
                psmt.setInt(2, ownerId);
                psmt.executeUpdate();
                liked = true;
            }
        } catch (Exception e) {
            System.out.println("❌ 좋아요 토글 실패");
            e.printStackTrace();
        } 
        return liked;
    }

    /** 💬 댓글 목록 */
    public List<Map<String, Object>> selectComments(int postId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT c.comment_id, c.content, c.created_at, c.owner_id, o.nickname
            FROM post_comment c
            JOIN owner o ON c.owner_id = o.owner_id
            WHERE c.post_id = ? AND c.is_deleted = 'N'
            ORDER BY c.comment_id ASC
        """;
        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, postId);
            rs = psmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment_id", rs.getInt("comment_id"));
                map.put("content", rs.getString("content"));
                map.put("created_at", rs.getDate("created_at"));
                map.put("owner_id", rs.getInt("owner_id"));
                map.put("nickname", rs.getString("nickname"));
                list.add(map);
            }
        } catch (Exception e) {
            System.out.println("❌ 댓글 목록 조회 실패");
            e.printStackTrace();
        } 
        return list;
    }

    /** 🛠 게시글 수정 */
    public int updatePost(int postId, String title, String content) {
        int result = 0;
        String sql = """
            UPDATE post
            SET title = ?, content = ?, updated_at = SYSDATE
            WHERE post_id = ? AND is_deleted = 'N'
        """;

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, title);
            psmt.setString(2, content);
            psmt.setInt(3, postId);
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ 게시글 수정 실패");
            e.printStackTrace();
        } 
        return result;
    }

    /** 🗑 게시글 삭제 (논리삭제) */
    public int deletePost(int postId) {
        int result = 0;
        String sql = "UPDATE post SET is_deleted = 'Y' WHERE post_id = ?";

        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, postId);
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ 게시글 삭제 실패");
            e.printStackTrace();
        } 
        return result;
    }
    /** 📝 게시글 등록 (DB 구조에 딱 맞게 단순화) */
    public int insertPost(PostDTO dto) {
        int result = 0;
        // ⚙️ DB 구조에 맞는 INSERT문 (불필요한 컬럼 제거)
        String sql = """
            INSERT INTO POST (OWNER_ID, TITLE, CONTENT)
            VALUES (?, ?, ?)
        """;

        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, dto.getOwnerId());
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getContent());

            result = psmt.executeUpdate();
            System.out.println("✅ SQL 실행 완료, 결과=" + result);
        } catch (Exception e) {
            System.out.println("❌ 게시글 등록 실패 (insertPost)");
            e.printStackTrace(); // ← 꼭 추가! 콘솔에 Oracle 오류 코드 출력
        } 
        return result;
    }
 // ✅ 총 게시글 수 (페이징용)
    public int countPosts() {
        String sql = "SELECT COUNT(*) FROM post p WHERE NVL(p.is_deleted,'N')='N'";
        try {
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ 페이지별 게시글 조회 (10개씩)
    public List<PostDTO> selectPaged(int page, int limit) {
        List<PostDTO> list = new ArrayList<>();
        int offset = (page - 1) * limit;

        String sql = """
            SELECT 
                p.post_id, p.owner_id, o.nickname AS owner_nickname,
                p.title, p.content, p.created_at,
                NVL((SELECT COUNT(*) FROM post_view v WHERE v.post_id=p.post_id),0) AS view_count,
                NVL((SELECT COUNT(*) FROM post_like l WHERE l.post_id=p.post_id),0) AS like_count,
                NVL((SELECT COUNT(*) FROM post_comment c WHERE c.post_id=p.post_id AND c.is_deleted='N'),0) AS comment_count
            FROM post p
            JOIN owner o ON p.owner_id = o.owner_id
            WHERE NVL(p.is_deleted,'N')='N'
            ORDER BY p.post_id DESC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

        try {
            psmt = con.prepareStatement(sql);
            psmt.setInt(1, offset);
            psmt.setInt(2, limit);
            rs = psmt.executeQuery();

            while (rs.next()) {
                PostDTO dto = new PostDTO();
                dto.setPostId(rs.getInt("post_id"));
                dto.setOwnerId(rs.getInt("owner_id"));
                dto.setOwnerNickname(rs.getString("owner_nickname"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getDate("created_at"));
                dto.setViewCount(rs.getInt("view_count"));
                dto.setLikeCount(rs.getInt("like_count"));
                dto.setCommentCount(rs.getInt("comment_count"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



}

