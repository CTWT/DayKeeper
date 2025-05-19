package DetailTodoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : DatabaseUtil.java  
 * 수정자 : 
 * 수정일 :
 * 설명 : DB 연결을 관리하는 유틸 클래스
 */


public class DatabaseUtil {

    private static final String URL = "jdbc:mariadb://localhost:3306/daykeeper";
    private static final String USER = "daykeeper";
    private static final String PASSWORD = "keeperpass";

    /**
     * DB 연결을 반환하는 메서드
     * 
     * @return Connection 객체
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
