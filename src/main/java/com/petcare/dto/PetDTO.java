package com.petcare.dto;

/**
 * 반려동물 정보 DTO.
 *
 * 초보자 설명:
 * - DTO(Data Transfer Object)는 데이터(정보)를 담아서 다른 계층(예: DAO, 컨트롤러, JSP)에
 *   전달할 때 사용하는 단순한 자바 객체입니다.
 * - 보통 변수(필드)와 getter/setter 메서드만 가집니다.
 * - 이 클래스는 반려동물 한 마리의 정보를 표현합니다.
 *
 * 사용 예:
 * - DAO가 DB에서 반려동물 정보를 읽어와 이 객체에 넣고 컨트롤러로 반환합니다.
 * - 컨트롤러는 이 객체를 JSP에 전달하여 화면에 보여줍니다.
 */
public class PetDTO {
    /** 반려동물 ID (데이터베이스의 기본키) */
    private int petId;
    /** 소유자 ID (반려동물을 등록한 사람의 ID) */
    private int ownerId;
    /** 품종 코드 (예: DOG_MALTESE) - 내부에서 품종을 구분할 때 사용 */
    private String breedCode;
    /** 반려동물 이름 */
    private String petName;
    /** 성별 (예: "M" 또는 "F") */
    private String sex;
    /** 생일 (간단한 예제에서는 문자열로 보관) - 실제 서비스면 Date 타입도 고려 */
    private java.util.Date birthDate;
    /** 몸무게(킬로그램 단위) */
    private double weightKg;

    // 아래 getter/setter는 필드에 값을 넣거나 꺼낼 때 사용합니다.
    // 예: pet.setPetName("콩이"); String name = pet.getPetName();
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getBreedCode() { return breedCode; }
    public void setBreedCode(String breedCode) { this.breedCode = breedCode; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public java.util.Date getBirthDate() { return birthDate; }
    public void setBirthDate(java.util.Date birthDate) { this.birthDate = birthDate; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
}