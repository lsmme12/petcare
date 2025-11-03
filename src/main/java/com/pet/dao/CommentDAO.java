// 📦 com.pet.dao 패키지에 속한 파일이에요.
//     → DAO(Data Access Object): 실제로 데이터베이스(DB)랑 직접 대화하는 클래스예요.
package com.pet.dao;

import java.sql.*;  // 💾 DB 연결, SQL 실행할 때 필요한 클래스들 (Connection, PreparedStatement, ResultSet 등)
import java.util.*; // 💡 리스트(List)와 같은 컬렉션을 사용하기 위해 불러옴
import com.pet.config.DBConnPool; // ⚙️ DB 연결을 도와주는 부모 클래스 (JNDI DBCP 기반)
import com.pet.dto.CommentDTO;    // 🧱 댓글 데이터를 담을 상자 클래스 (Data Transfer Object)

/**
 * 📘 CommentDAO
 * --------------------------------------------------------
 * 💡 하는 일:
 *   1️⃣ 댓글 목록 불러오기 (selectByPostId)
 *   2️⃣ 댓글 등록 (insertComment)
 *   3️⃣ 댓글 삭제 (deleteComment)
 * 
 * ⚙️ DBConnPool을 상속받아서 이미 DB 연결(con), psmt, rs 등을 바로 쓸 수 있음
 */
public class CommentDAO extends DBConnPool {

    // 📝 [1] 댓글 목록 조회
    public List<CommentDTO> selectByPostId(int postId) {
        // 📦 댓글 정보를 담을 빈 상자 리스트 만들기
        List<CommentDTO> list = new ArrayList<>();

        // 📋 SQL 문: 특정 게시글(post_id)에 달린 댓글을 모두 가져오기
        String sql = """
            SELECT c.*, o.nickname
            FROM comment c
            JOIN owner o ON c.owner_id = o.owner_id
            WHERE c.post_id = ?
            ORDER BY c.comment_id ASC
        """;
        // 💡 위 SQL 설명:
        //   - comment 테이블(c)과 owner 테이블(o)을 합쳐서 댓글 작성자 닉네임도 함께 가져와요.
        //   - WHERE로 게시글 번호(post_id)가 같은 댓글만 찾음.
        //   - ORDER BY로 오래된 댓글부터 순서대로 정렬(오름차순).

        try {
            psmt = con.prepareStatement(sql); // 💾 SQL 실행 준비
            psmt.setInt(1, postId);           // ? 자리에 게시글 번호 넣기
            rs = psmt.executeQuery();         // 🏃 SQL 실행 후 결과(ResultSet) 받기

            // 📋 결과가 여러 줄일 수 있으니까 while로 한 줄씩 꺼내기
            while (rs.next()) {
                // ✨ CommentDTO(댓글 한 건의 데이터 저장용 상자) 생성
                CommentDTO dto = new CommentDTO();

                // 🔸 각 컬럼의 값을 DTO에 저장
                dto.setCommentId(rs.getInt("comment_id")); // 댓글 번호
                dto.setPostId(rs.getInt("post_id"));       // 게시글 번호
                dto.setOwnerId(rs.getInt("owner_id"));     // 작성자 번호
                dto.setNickname(rs.getString("nickname")); // 작성자 닉네임 (owner 테이블에서 JOIN)
                dto.setContent(rs.getString("content"));   // 댓글 내용
                dto.setCreatedAt(rs.getDate("created_at"));// 작성 날짜

                // 🧺 완성된 댓글 하나를 리스트에 추가
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("❌ 댓글 목록 조회 실패");
            e.printStackTrace(); // 🚨 오류가 났을 때 콘솔에 표시
        }

        // 📦 댓글이 담긴 리스트 반환
        return list;
    }

    // 🧱 [2] 댓글 등록 (DB에 새 댓글 추가)
    public int insertComment(CommentDTO dto) {
        int result = 0; // ✅ 성공 여부를 담을 변수 (0=실패, 1=성공)

        // 🧾 SQL 문: 새 댓글 한 줄 추가하기
        String sql = "INSERT INTO comment (comment_id, post_id, owner_id, content, created_at) "
                   + "VALUES (seq_comment_id.NEXTVAL, ?, ?, ?, SYSDATE)";
        // 💡 설명:
        //   - comment_id: 댓글 고유번호 (시퀀스 seq_comment_id로 자동 증가)
        //   - post_id: 어떤 게시글의 댓글인지
        //   - owner_id: 누가 썼는지
        //   - content: 댓글 내용
        //   - created_at: 작성일(SYSDATE = 현재 날짜)

        try {
            psmt = con.prepareStatement(sql);   // 💾 SQL 준비
            psmt.setInt(1, dto.getPostId());    // 첫 번째 ? → 게시글 번호
            psmt.setInt(2, dto.getOwnerId());   // 두 번째 ? → 작성자 번호
            psmt.setString(3, dto.getContent()); // 세 번째 ? → 댓글 내용

            // 🚀 SQL 실행 (INSERT)
            result = psmt.executeUpdate(); // 1이면 성공, 0이면 실패
        } catch (Exception e) {
            System.out.println("❌ 댓글 등록 실패");
            e.printStackTrace();
        }

        return result; // 💬 성공(1) 또는 실패(0) 결과 반환
    }

    // 🗑 [3] 댓글 삭제
    public int deleteComment(int commentId) {
        int result = 0; // ✅ 삭제 결과 (1이면 성공)

        // 📋 SQL 문: 특정 댓글 번호(comment_id)에 해당하는 댓글 삭제
        String sql = "DELETE FROM comment WHERE comment_id=?";

        try {
            psmt = con.prepareStatement(sql); // 💾 SQL 실행 준비
            psmt.setInt(1, commentId);        // ? 자리에 삭제할 댓글 번호 넣기

            // 🚀 SQL 실행 (DELETE)
            result = psmt.executeUpdate(); // 1이면 성공, 0이면 실패
        } catch (Exception e) {
            System.out.println("❌ 댓글 삭제 실패");
            e.printStackTrace();
        }

        return result; // 💬 삭제 결과 반환
    }
}
