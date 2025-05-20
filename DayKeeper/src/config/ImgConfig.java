package config;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * 작성자 : 김관호
 * 작성일 : 2025.05.20
 * 파일명 : ImgConfig.java
 * 설명 : Image를 불러오거나 ImgPanel 을 생성할 수 있는 클래스
 */

public class ImgConfig {

    // 이미지 파일 경로를 받아 Image 객체 반환
    public static Image imgComponent(String url) {
        // 확장자 확인 및 경로 설정
        String extension = getExtention(url);
        String filePath = "";

        if (extension != null && extension.equals(".png")) {
            filePath = System.getProperty("user.dir") + "/DayKeeper/img/" + url;
        } else {
            filePath = System.getProperty("user.dir") + "/DayKeeper/img/" + url + ".png";
        }

        // 경로 유효성 검사 후 이미지 반환
        if (pathCheck(filePath)) {
            return new ImageIcon(filePath).getImage();
        } else {
            return null;
        }
    }

    // Image 객체를 기반으로 ImageIcon 반환
    public static ImageIcon imgIconComponent(String url) {
        Image image = imgComponent(url);
        return (image == null) ? null : new ImageIcon(image);
    }

    // 크기 지정된 JLabel 생성
    public static JLabel imgLabelComponent(String url, int width, int height) {
        Image scaledImage = imgComponent(url).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        return iconLabel;
    }

    // 기본 크기 (100x100) JLabel 생성
    public static JLabel imgLabelComponent(String url) {
        return imgLabelComponent(url, 100, 100);
    }

    // 파일명에서 확장자 추출 (.png 등)
    private static String getExtention(String url) {
        int index = -1;
        for (int i = url.length() - 1; i >= 0; i--) {
            char c = url.charAt(i);
            if (c == '.') {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        return url.substring(index);
    }

    // 파일 경로 존재 여부 확인
    private static boolean pathCheck(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("파일 경로가 유효하지 않습니다: " + filePath);
            return false;
        }

        return true;
    }
}
