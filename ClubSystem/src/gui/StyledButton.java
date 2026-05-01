package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StyledButton extends JButton {

    private boolean isGold;

    public StyledButton(String text, boolean isGold) {
        super(text);
        this.isGold = isGold;
        setFont(Theme.BUTTON_FONT);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        applyNormal();

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { applyHover(); }
            public void mouseExited(MouseEvent e)  { applyNormal(); }
        });
    }

    private void applyNormal() {
        if (isGold) {
            setBackground(Theme.GOLD);
            setForeground(Theme.NAVY);
        } else {
            setBackground(Theme.NAVY_PANEL);
            setForeground(Theme.WHITE);
        }
    }

    private void applyHover() {
        if (isGold) {
            setBackground(Theme.GOLD_LIGHT);
            setForeground(Theme.NAVY);
        } else {
            setBackground(new Color(30, 60, 130));
            setForeground(Theme.GOLD_LIGHT);
        }
    }
}
