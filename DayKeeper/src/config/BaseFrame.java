package config;

import javax.swing.JFrame;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.12
 * 파일명 : BaseFrame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : swing 기본 출력화면 사이즈 지정
 */
public class BaseFrame extends JFrame {

    public BaseFrame() {

        // 타이틀 설정
        setTitle("DayKeeper");

        // 고정 크기 설정
        setSize(800, 600);

        // 창 크기 고정
        setResizable(false);

        // 닫기 시 종료
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 가운데 정렬
        setLocationRelativeTo(null);
    }
}
