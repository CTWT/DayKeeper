package common;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.19
 * 파일명 : Session.java
 * 수정자 : 
 * 수정일 :
 * 설명 : login후 id 저장해 호출
 */
public class Session {

    private static String userId;

    public static void setUserId(String id) {
        userId = id;
    }

    public static String getUserId() {
        return userId;
    }

    public static void clear() {
        userId = null;
    }
}
