package common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TitleLabel extends JLabel {

    public TitleLabel() {
        super("DAY-KEEPER", SwingConstants.CENTER);
        setFont(new Font("SansSerif", Font.BOLD, 40));
        setForeground(new Color(30, 100, 180));
    }
}
