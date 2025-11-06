package com.petcare.dto;

import java.util.Date;

public class ObesityDTO {
    private int obesityId;
    private int petId;
    private String check1;
    private String check2;
    private String check3;
    private String check4;
    private String check5;
    private int totalScore;    // 트리거로 계산
    private String bcsGrade;   // 트리거로 계산
    private Date createdAt;

    public int getObesityId() { return obesityId; }
    public void setObesityId(int obesityId) { this.obesityId = obesityId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public String getCheck1() { return check1; }
    public void setCheck1(String check1) { this.check1 = check1; }

    public String getCheck2() { return check2; }
    public void setCheck2(String check2) { this.check2 = check2; }

    public String getCheck3() { return check3; }
    public void setCheck3(String check3) { this.check3 = check3; }

    public String getCheck4() { return check4; }
    public void setCheck4(String check4) { this.check4 = check4; }

    public String getCheck5() { return check5; }
    public void setCheck5(String check5) { this.check5 = check5; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public String getBcsGrade() { return bcsGrade; }
    public void setBcsGrade(String bcsGrade) { this.bcsGrade = bcsGrade; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
