package com.pet.dto;

import java.util.Date;

/**
 * 투약기록 데이터 전송 객체(VO/DTO).
 *
 * 초보자 설명:
 * - DTO(Data Transfer Object)는 데이터베이스에서 읽어온 값을 컨트롤러나 화면에 전달할 때
 *   쓰이는 단순한 자바 객체입니다. 보통 필드(변수)와 getter/setter만 가집니다.
 * - 이 클래스는 투약 기록 1건(record)을 표현합니다.
 *
 * 필드 설명:
 * - recordId: DB의 기본키(고유번호)
 * - medicine: 약품명
 * - dosageTime: 투약 시간(java.util.Date로 저장)
 *
 * 사용 예:
 * - DAO가 ResultSet에서 값을 꺼내어 이 DTO에 담아 컨트롤러로 반환합니다.
 */
public class MediRecordDTO {
    /** DB PK */
    private int recordId;
    /** 약품명 */
    private String medicine;
    /** 투약 시각 (java.util.Date 사용) */
    private Date dosageTime;

    // 아래 getter/setter들은 필드 값을 읽고(set)/설정(get)하는 표준 메서드입니다.
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }

    public Date getDosageTime() { return dosageTime; }
    public void setDosageTime(Date dosageTime) { this.dosageTime = dosageTime; }
    
    public String getDosageTimeFormatted() {
        if (dosageTime == null) return "알 수 없음";
        String s = dosageTime.toString(); // 예: 2025-11-04 09:43:00.0
        try {
            // "2025-11-04 09:43:00.0" → "2025-11-04 09:43"
            return s.substring(0, 16);
        } catch (Exception ignore) {
            return s;
        }
    }
}