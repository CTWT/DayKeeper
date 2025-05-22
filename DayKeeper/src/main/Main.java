package main;

import config.BaseFrame;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.14
 * 파일명 : Main.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 에플리케이션 실행
 */

public class Main {

    /*
     * 프로젝트 조건:
     * - BaseFrame은 CardLayout 기반으로 화면을 전환함
     * - Main.java에서 실행 시 첫 화면으로 로그인(Login) 화면이 보여져야 함
     * - Login은 JPanel을 상속하며, BaseFrame.showScreen()으로 삽입됨
     */
    public static void main(String[] args) {
        new BaseFrame();
    }
}
