package config;

import javax.swing.JPanel;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : ScreenType.java
 * 수정자 : 문원주
 * 수정일 : 25.05.20
 * 설명 : SIGNUP ENUM문 추가
 */
public enum ScreenType {

    // 화면만 가지고 가는 enum 여기에 생성화면 추가시 자동적용
    LOGIN("login", login.Login.class),
    TODOLIST("todoList", todoList.TodoList.class),
    TODODETAIL("todoDetail", todoDetail.TodoDetail.class),
    STATISTICS("statistic", statistics.Statistics.class),
    PILL("Pill", pill.pillPanel.Pill.class),
    SIGNUP("Signup", login.Signup.class);
    // 필요시 추가

    private final String screenName;
    private final Class<? extends JPanel> panelClass;

    ScreenType(String screenName, Class<? extends JPanel> panelClass) {
        this.screenName = screenName;
        this.panelClass = panelClass;
    }

    public String getScreenName() {
        return screenName;
    }

    public Class<? extends JPanel> getPanelClass() {
        return panelClass;
    }
}
