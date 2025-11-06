package com.pet.dto;

public class PetDTO {
	
    private int petId;
    private String petName;
    private String sex;
    private java.util.Date birthDate;  // fully qualified
    private double weightKg;

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public java.util.Date getBirthDate() { return birthDate; }
    public void setBirthDate(java.util.Date birthDate) { this.birthDate = birthDate; }
    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
}
