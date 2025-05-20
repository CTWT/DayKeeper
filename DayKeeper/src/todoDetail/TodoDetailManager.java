package todoDetail;

import javax.swing.DefaultListModel;


/*
 * 생성자 : 유연우
 * 생성일 : 25.05.20
 * 파일명 : TodoDetailManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist Detail Manager 리스트에 할일 목록 저장 및 반환
 */

public class TodoDetailManager {
    private static TodoDetailManager instance;

    // 공유 데이터: 할일 제목 리스트 모델과 할일 내용 맵
    private DefaultListModel<String> todoListModel = new DefaultListModel<>();
    private java.util.Map<String, String> todoContentMap = new java.util.HashMap<>();
    private String selectedTitle = "";

    private TodoDetailManager(){

    }

    public static TodoDetailManager getInst(){
        if(instance == null) {
            instance = new TodoDetailManager();
        }

        return instance;
    }

    /**
     * 할일 제목 리스트 모델을 반환한다.
     * 
     * @return 할일 제목들이 저장된 DefaultListModel<String>
     */
    public DefaultListModel<String> getTodoListModel() {
        return todoListModel;
    }

    /**
     * 할일 제목과 내용이 매핑된 맵을 반환한다.
     * 
     * @return 할일 제목과 내용이 저장된 Map<String, String>
     */
    public java.util.Map<String, String> getTodoContentMap() {
        return todoContentMap;
    }

    /**
     * 현재 선택된 할일 제목을 설정한다.
     * 
     * @param title 선택할 할일 제목
     */
    public void setSelectedTitle(String title) {
        this.selectedTitle = title;
    }

    /**
     * 현재 선택된 할일 제목을 반환한다.
     * 
     * @return 선택된 할일 제목
     */
    public String getSelectedTitle() {
        return selectedTitle;
    }


}
