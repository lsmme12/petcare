package com.pet.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class ExerRecordDTO {
	
    private int exerId;
    private int petId;
    private java.sql.Date exerDate;

    private int exerTime;
    private String exerLevel;
    private String memo;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ExerRecordDTO() {}

    public int getExerId() { return exerId; }
    public void setExerId(int exerId) { this.exerId = exerId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public Date getExerDate() { return exerDate; }
    public void setExerDate(Date exerDate) { this.exerDate = exerDate; }

    public int getExerTime() { return exerTime; }
    public void setExerTime(int exerTime) { this.exerTime = exerTime; }

    public String getExerLevel() { return exerLevel; }
    public void setExerLevel(String exerLevel) { this.exerLevel = exerLevel; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
}
