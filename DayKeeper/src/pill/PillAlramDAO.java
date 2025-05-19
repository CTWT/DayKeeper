package pill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

import dbConnection.DBManager;

public class PillAlramDAO {

    private int nextInt;

    public void registerAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT alramTime from PILL_ALRAM where id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, "12345"); // Login.UserSearch.curUserId;
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                updateAlarm(selectedHour);
                return;
            }
            insertAlarm(selectedHour);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE PILL_ALRAM set AlramTime = ? where id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            LocalTime time = LocalTime.of(selectedHour, 0, 0);
            psmt.setTime(1, Time.valueOf(time));
            psmt.setString(2, "12345"); // Login.UserSearch.curUserId;
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "INSERT INTO PILL_ALRAM(alram_id,id,alramTime,date) values(?,?,?,now())";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1, nextInt);
            psmt.setString(2, "12345"); // Login.UserSearch.curUserId;

            LocalTime time = LocalTime.of(selectedHour, 0, 0);
            psmt.setTime(3, Time.valueOf(time));
            psmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void settingNextInt() {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT alram_id FROM PILL_ALRAM";
            PreparedStatement psmt = con.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();

            while(rs.next()){
                int x = rs.getInt(1);
                nextInt = Math.max(x,nextInt);
            }
            nextInt++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextInt() {
        return nextInt;
    }

}
