package pill;
import java.sql.Date;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.14
 * 파일명 : SupplementDTO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 영양제의 DTO
 */

public class PillDTO {

    // 영양제 아이디
    private String id;

    // 영양제 이름
    private String pillName;

    // 영양제 설명
    private String pillDetail;

    // 영양제 총수량
    private int pillAmount;

    // 영양제 일자
    private Date date;

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

    public String getPillDetail() {
        return pillDetail;
    }

    public void setPillDetail(String pillDetail) {
        this.pillDetail = pillDetail;
    }

    public int getPillAmount() {
        return pillAmount;
    }

    public void setPillAmount(int pillAmount) {
        this.pillAmount = pillAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PillDTO [id=" + id + ", pillName=" + pillName + ", pillDetail=" + pillDetail + ", pillAmount="
                + pillAmount + ", date=" + date + "]";
    }

}
