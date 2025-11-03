package com.pet.dao; // 📦 DAO들이 모여있는 패키지 경로

import java.sql.*;      // ✅ JDBC(자바로 DB 쓰는 기술)에서 쓰는 클래스들 임포트
import java.util.*;     // ✅ List, Map 같은 자료구조 임포트

import com.pet.config.DBConnPool; // ✅ 톰캣 DBCP(JNDI) 연결을 상속받아 쓰기 위함
import com.pet.dto.PostDTO;       // ✅ 게시글 한 건을 담는 데이터 상자

/**
 * 📘 PostDAO (게시글 DAO)
 * - 게시판의 DB 작업을 전담 (CRUD, 조회수, 좋아요, 댓글목록)
 * - DBConnPool을 상속받아 con/psmt/rs를 바로 사용
 */
public class PostDAO extends DBConnPool { // 🔧 DBConnPool을 상속받아 커넥션 풀 사용

    /** ✅ Controller 등에서 직접 커넥션을 써야 할 때 꺼내주기 */
    public Connection getConnection() { // 🔍 외부에서 con에 접근해야 할 때 사용
        return con; // ▶ 현재 열려있는 DB 연결(con) 반환
    }

    /* ===============================
     * 📜 게시글 목록 조회 (삭제되지 않은 글만)
     * =============================== */
    public List<PostDTO> selectAll() { // 🔎 모든 게시글 목록 불러오기(삭제 안 된 것만)
        List<PostDTO> list = new ArrayList<>(); // 🧺 결과를 담을 리스트 준비

        // 🧾 SQL: 게시글 + 작성자 닉네임 + 조회수/좋아요/댓글수 집계까지 함께 조회
        String sql = """
            SELECT 
                p.post_id,                                   -- 게시글 번호
                p.owner_id,                                  -- 작성자 회원번호
                o.nickname AS owner_nickname,                -- 작성자 닉네임(조인)
                p.title,                                     -- 제목
                p.content,                                   -- 내용
                p.created_at,                                -- 작성일
                p.updated_at,                                -- 수정일
                NVL((SELECT COUNT(*) FROM post_view v 
                     WHERE v.post_id = p.post_id), 0) AS view_count,  -- 조회수
                NVL((SELECT COUNT(*) FROM post_like l 
                     WHERE l.post_id = p.post_id), 0) AS like_count,  -- 좋아요 수
                NVL((SELECT COUNT(*) FROM post_comment c 
                     WHERE c.post_id = p.post_id AND c.is_deleted = 'N'), 0) AS comment_count -- 댓글 수(삭제 제외)
            FROM post p
            JOIN owner o ON p.owner_id = o.owner_id          -- 작성자 닉네임 얻기
            WHERE p.is_deleted = 'N'                         -- 삭제되지 않은 글만
            ORDER BY p.post_id DESC                          -- 최신글부터
        """;

        try { // 🚀 DB 작업 시작
            psmt = con.prepareStatement(sql); // ▶ SQL 준비
            rs = psmt.executeQuery();         // ▶ SQL 실행하고 결과 받기

            while (rs.next()) {               // ▶ 결과 행(row) 하나씩 꺼내기
                PostDTO dto = new PostDTO();  // 🧱 한 행을 담을 DTO 준비
                dto.setPostId(rs.getInt("post_id"));                  // ▷ 글번호
                dto.setOwnerId(rs.getInt("owner_id"));                // ▷ 작성자번호
                dto.setOwnerNickname(rs.getString("owner_nickname")); // ▷ 닉네임
                dto.setTitle(rs.getString("title"));                  // ▷ 제목
                dto.setContent(rs.getString("content"));              // ▷ 내용
                dto.setCreatedAt(rs.getDate("created_at"));           // ▷ 작성일
                dto.setUpdatedAt(rs.getDate("updated_at"));           // ▷ 수정일
                dto.setViewCount(rs.getInt("view_count"));            // ▷ 조회수
                dto.setLikeCount(rs.getInt("like_count"));            // ▷ 좋아요 수
                dto.setCommentCount(rs.getInt("comment_count"));      // ▷ 댓글 수
                list.add(dto);                                        // 🧺 리스트에 추가
            }

        } catch (Exception e) {               // ❗ 예외 발생 시
            System.out.println("❌ 게시글 목록 조회 실패");
            e.printStackTrace();              // ▶ 오류 출력
        } finally {
            close();                          // 🔒 사용한 DB 자원 정리(반납)
        }

        return list;                          // 📤 최종 결과 반환
    }

    /* ===============================
     * 🔍 단일 게시글 조회 (조회수/좋아요 포함)
     * =============================== */
    public PostDTO selectOne(int postId) { // 🔎 글번호로 한 건 조회
        PostDTO dto = null;                // 🧱 결과 담을 DTO(없으면 null)

        // 🧾 SQL: 한 건 + 닉네임 + 조회/좋아요 집계
        String sql = """
            SELECT 
                p.post_id,
                p.owner_id,
                o.nickname AS owner_nickname,
                p.title,
                p.content,
                p.created_at,
                p.updated_at,
                NVL((SELECT COUNT(*) FROM post_view v WHERE v.post_id = p.post_id), 0) AS view_count,
                NVL((SELECT COUNT(*) FROM post_like l WHERE l.post_id = p.post_id), 0) AS like_count
            FROM post p
            JOIN owner o ON p.owner_id = o.owner_id
            WHERE p.post_id = ? AND p.is_deleted = 'N'
        """;

        try {
            psmt = con.prepareStatement(sql); // ▶ SQL 준비
            psmt.setInt(1, postId);           // ▶ 첫 번째 물음표에 postId 바인딩
            rs = psmt.executeQuery();         // ▶ 실행

            if (rs.next()) {                  // ▶ 결과가 있으면
                dto = new PostDTO();          // ▷ DTO 생성
                dto.setPostId(rs.getInt("post_id"));                  // ▷ 글번호
                dto.setOwnerId(rs.getInt("owner_id"));                // ▷ 작성자번호
                dto.setOwnerNickname(rs.getString("owner_nickname")); // ▷ 닉네임
                dto.setTitle(rs.getString("title"));                  // ▷ 제목
                dto.setContent(rs.getString("content"));              // ▷ 내용
                dto.setCreatedAt(rs.getDate("created_at"));           // ▷ 작성일
                dto.setUpdatedAt(rs.getDate("updated_at"));           // ▷ 수정일
                dto.setViewCount(rs.getInt("view_count"));            // ▷ 조회수
                dto.setLikeCount(rs.getInt("like_count"));            // ▷ 좋아요 수
            }

        } catch (Exception e) {
            System.out.println("❌ 게시글 상세 조회 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 자원 반납
        }

        return dto; // 📤 결과(DTO 또는 null) 반환
    }

    /* ===============================
     * ✏️ 게시글 작성
     * =============================== */
    public int insertPost(PostDTO dto) { // 📝 새 글 저장
        int result = 0;                  // ▶ 영향받은 행 수(성공 시 1)
        String sql = """
            INSERT INTO post (owner_id, title, content, created_at, is_deleted)
            VALUES (?, ?, ?, SYSDATE, 'N')
        """; // 💡 SYSDATE: 현재 날짜/시간, is_deleted 기본 'N'

        try {
            psmt = con.prepareStatement(sql); // ▶ SQL 준비
            psmt.setInt(1, dto.getOwnerId()); // ▷ 1: 작성자 번호
            psmt.setString(2, dto.getTitle()); // ▷ 2: 제목
            psmt.setString(3, dto.getContent()); // ▷ 3: 내용
            result = psmt.executeUpdate();     // ▶ 실행 (성공 시 1)

        } catch (Exception e) {
            System.out.println("❌ 게시글 등록 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }
        return result; // 📤 결과 반환
    }

    /* ===============================
     * 🛠 게시글 수정
     * =============================== */
    public int updatePost(int postId, String title, String content) { // 🛠 제목/내용 바꾸기
        int result = 0; // ▶ 성공 시 1
        String sql = """
            UPDATE post
            SET title = ?, content = ?, updated_at = SYSDATE
            WHERE post_id = ? AND is_deleted = 'N'
        """; // 💡 수정 시간(updated_at) 자동 갱신

        try {
            psmt = con.prepareStatement(sql); // ▶ 준비
            psmt.setString(1, title);         // ▷ 1: 새 제목
            psmt.setString(2, content);       // ▷ 2: 새 내용
            psmt.setInt(3, postId);           // ▷ 3: 대상 글번호
            result = psmt.executeUpdate();    // ▶ 실행
        } catch (Exception e) {
            System.out.println("❌ 게시글 수정 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }
        return result; // 📤 결과
    }

    /* ===============================
     * 🗑 게시글 삭제 (논리삭제)
     * =============================== */
    public int deletePost(int postId) { // 🗑 실제 삭제 대신 is_deleted='Y'
        int result = 0; // ▶ 성공 시 1
        String sql = "UPDATE post SET is_deleted = 'Y' WHERE post_id = ?"; // 🧾 논리삭제

        try {
            psmt = con.prepareStatement(sql); // ▶ 준비
            psmt.setInt(1, postId);           // ▷ 글번호 바인딩
            result = psmt.executeUpdate();    // ▶ 실행
        } catch (Exception e) {
            System.out.println("❌ 게시글 삭제 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }
        return result; // 📤 결과
    }

    /* ===============================
     * 👁 조회 기록 추가 (중복 방지)
     * =============================== */
    public void insertView(int postId, Integer ownerId) { // 👁 조회수용 기록 남기기
        String sql = "INSERT INTO post_view (post_id, owner_id) VALUES (?, ?)"; // 🧾 단순 INSERT

        try {
            psmt = con.prepareStatement(sql);  // ▶ 준비
            psmt.setInt(1, postId);            // ▷ 글번호
            if (ownerId == null) {             // ▷ 비로그인 방문자일 수도 있으니
                psmt.setNull(2, Types.INTEGER); //   owner_id 를 NULL로 저장
            } else {
                psmt.setInt(2, ownerId);       // ▷ 로그인 사용자면 번호 저장
            }
            psmt.executeUpdate();               // ▶ 실행

        } catch (SQLIntegrityConstraintViolationException e) {
            // ✅ 유니크 제약조건으로 "이미 본 기록"이면 자동 예외 → 그냥 무시(중복 방지)
        } catch (Exception e) {
            System.out.println("❌ 조회수 기록 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }
    }

    /* ===============================
     * ❤️ 좋아요 토글 (있으면 삭제, 없으면 추가)
     * =============================== */
    public boolean toggleLike(int postId, int ownerId) { // ❤️ 눌렀다 또 누르면 취소
        boolean liked = false; // ▶ true: 좋아요 상태, false: 취소 상태

        try {
            // 1) 현재 좋아요 했는지 조회
            String checkSql = "SELECT 1 FROM post_like WHERE post_id=? AND owner_id=?";
            psmt = con.prepareStatement(checkSql); // ▶ 준비
            psmt.setInt(1, postId);                // ▷ 글번호
            psmt.setInt(2, ownerId);               // ▷ 사용자
            rs = psmt.executeQuery();              // ▶ 실행

            if (rs.next()) { // 🔁 이미 좋아요가 존재하면
                String delSql = "DELETE FROM post_like WHERE post_id=? AND owner_id=?"; // 🧾 삭제
                psmt = con.prepareStatement(delSql); // ▶ 준비
                psmt.setInt(1, postId);             // ▷ 글번호
                psmt.setInt(2, ownerId);            // ▷ 사용자
                psmt.executeUpdate();                // ▶ 실행
                liked = false;                       // ▶ 취소 상태
            } else { // 🔁 존재하지 않으면 새로 추가
                String insSql = "INSERT INTO post_like (post_id, owner_id) VALUES (?, ?)"; // 🧾 추가
                psmt = con.prepareStatement(insSql); // ▶ 준비
                psmt.setInt(1, postId);             // ▷ 글번호
                psmt.setInt(2, ownerId);            // ▷ 사용자
                psmt.executeUpdate();                // ▶ 실행
                liked = true;                        // ▶ 좋아요 상태
            }

        } catch (Exception e) {
            System.out.println("❌ 좋아요 토글 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }
        return liked; // 📤 최종 상태 반환
    }

    /* ===============================
     * 💬 댓글 목록 조회
     * =============================== */
    public List<Map<String, Object>> selectComments(int postId) { // 💬 댓글 리스트 불러오기
        List<Map<String, Object>> list = new ArrayList<>(); // 🧺 결과 담을 리스트

        // 🧾 SQL: 댓글 + 작성자 닉네임 (삭제되지 않은 것만)
        String sql = """
            SELECT c.comment_id, c.content, c.created_at, c.owner_id, o.nickname
            FROM post_comment c
            JOIN owner o ON c.owner_id = o.owner_id
            WHERE c.post_id = ? AND c.is_deleted = 'N'
            ORDER BY c.comment_id ASC
        """;

        try {
            psmt = con.prepareStatement(sql); // ▶ 준비
            psmt.setInt(1, postId);           // ▷ 대상 글번호
            rs = psmt.executeQuery();         // ▶ 실행

            while (rs.next()) {               // ▶ 한 줄씩 꺼내서
                Map<String, Object> map = new HashMap<>(); // ▷ 맵에 담고
                map.put("comment_id", rs.getInt("comment_id")); // ▷ 댓글번호
                map.put("content", rs.getString("content"));    // ▷ 내용
                map.put("created_at", rs.getDate("created_at"));// ▷ 작성일
                map.put("owner_id", rs.getInt("owner_id"));     // ▷ 작성자번호
                map.put("nickname", rs.getString("nickname"));  // ▷ 닉네임
                list.add(map);                                  // 🧺 리스트에 추가
            }
        } catch (Exception e) {
            System.out.println("❌ 댓글 목록 조회 실패");
            e.printStackTrace();
        } finally {
            close(); // 🔒 정리
        }

        return list; // 📤 결과 반환
    }
}
