package pill;

import java.sql.Date;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.12
 * 파일명 : PillYnDTO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : PillYnDTO
 */

/**
 * PillYnDTO 클래스는 특정 영양제의 섭취 여부와 날짜를 저장하는 데이터 전송 객체입니다.
 * 필드: id(영양제 ID), pillYn(섭취 여부), date(섭취 날짜)
 */
public class PillYnDTO {
    
    // 영양제 ID
    private String id;

    // 영양제 섭취 여부 ("Y" 또는 "N")
    private String pillYn;

    // 섭취 날짜 (SQL Date 타입)
    private Date date;

    /**
     * 영양제 ID를 반환합니다.
     * @return id - 영양제 ID
     */
    public String getId() {
        return id;
    }

    /**
     * 영양제 ID를 설정합니다.
     * @param id - 영양제 ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 섭취 여부를 반환합니다.
     * @return pillYn - 섭취 여부 ("Y" 또는 "N")
     */
    public String getPillYn() {
        return pillYn;
    }

    /**
     * 섭취 여부를 설정합니다.
     * @param pillYn - 섭취 여부 ("Y" 또는 "N")
     */
    public void setPillYn(String pillYn) {
        this.pillYn = pillYn;
    }

    /**
     * 섭취 날짜를 반환합니다.
     * @return date - 섭취 날짜
     */
    public Date getDate() {
        return date;
    }

    /**
     * 섭취 날짜를 설정합니다.
     * @param date - 섭취 날짜
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 객체 정보를 문자열로 반환합니다.
     * @return 객체 정보를 포함한 문자열
     */
    @Override
    public String toString() {
        return "PillYnDTO [id=" + id + ", pillYn=" + pillYn + ", date=" + date + "]";
    }
}