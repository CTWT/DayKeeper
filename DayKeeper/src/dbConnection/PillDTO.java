package dbConnection;

import java.time.LocalDateTime;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.15
 * 파일명 : PillDTO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 영양제의 DTO
 */

public class PillDTO {

    // 영양제 고유번호
    private int pill_id;

    // 사용자 아이디
    private String id;

    // 영양제 이름
    private String pillName;

    // 영양제 총수량
    private int pillAmount;

    // 영양제 일자
    private LocalDateTime date;

    public int getPill_id() {
        return pill_id;
    }

    public void setPill_id(int pill_id) {
        this.pill_id = pill_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getPillAmount() {
        return pillAmount;
    }

    public void setPillAmount(int pillAmount) {
        this.pillAmount = pillAmount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PillDTO [pill_id=" + pill_id + ", id=" + id + ", pillName=" + pillName
                + ", pillAmount=" + pillAmount + ", date=" + date + "]";
    }
}
