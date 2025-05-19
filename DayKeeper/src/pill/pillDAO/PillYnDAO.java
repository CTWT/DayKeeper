package pill.pillDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import common.Session;
import dbConnection.DBManager;

/*
 * 작성자 : 김관호
 * 작성일 : 2025.05.16
 * 파일명 : PillYnDAO.java
 * 설명 : PillYn 에 대한 DAO
 */

public class PillYnDAO {
    /**
     * db에 YN데이터를 삽입합니다.
     *
     * @param YN 삽입할 값
     */
    public void insertYnToDB(String YN) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "INSERT INTO PILLYN(date, id, pillYn) values (?,?,?)";
            PreparedStatement psmt = con.prepareStatement(sql);
            LocalDateTime time = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(time);
            psmt.setTimestamp(1, timestamp);
            psmt.setString(2, Session.getUserId()); //Login.UserSearch.curUserId;
            psmt.setString(3, YN);
            psmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 오늘날짜의 yn이 등록되어 있다면 update를 실행시킵니다.
     *
     * @param YN 영양제 섭취 YN
     */
    public void changeYnToDB(String YN) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT date from PILLYN where id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, Session.getUserId()); //Login.UserSearch.curUserId;
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                Timestamp tstamp = rs.getTimestamp(1);
                Date ts = new Date(tstamp.getTime());
                LocalDate curTime = LocalDate.now();
                if(ts.toString().equals(curTime.toString())){
                    updateYnToDB(YN, tstamp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 현재 등록되어 있는 YN 데이터를 변경합니다.
     *
     * @param YN 영양제 섭취 YN
     */
    public void updateYnToDB(String YN, Timestamp tstamp) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE PillYN SET pillYn = ? WHERE date = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, YN); // Login.UserSearch.curUserId;
            psmt.setTimestamp(2, tstamp);
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 오늘 영양제를 섭취했는지 확인합니다.
     *
     * @return boolean
     */
    public boolean checkConsume(){
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT date, pillYn from PILLYN where id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, Session.getUserId()); //Login.UserSearch.curUserId;
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                Date ts = rs.getDate(1);
                LocalDate curTime = LocalDate.now();
                String Yn = rs.getString(2);
                if(ts.toString().equals(curTime.toString())){
                    if(Yn.equals("Y")){
                        return true;
                    }
                }                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 오늘 날짜의 yn이 없다면 n을 삽입합니다.
     *
     */
    public void insertInitialYNData(){
         try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT date from PILLYN where id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, Session.getUserId()); //Login.UserSearch.curUserId;
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                Date ts = rs.getDate(1);
                LocalDate curTime = LocalDate.now();
                if(ts.toString().equals(curTime.toString())){
                    return;
                }                
            }

            insertYnToDB("N");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
