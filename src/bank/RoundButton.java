package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {
    private int radius;

    public RoundButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false); // Убираем стандартный фон
        setFocusPainted(false);      // Убираем рамку фокуса
        setBorderPainted(false);     // Убираем стандартную рамку
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Цвет при нажатии и наведении
        if (getModel().isPressed()) g2.setColor(getBackground().darker());
        else if (getModel().isRollover()) g2.setColor(getBackground().brighter());
        else g2.setColor(getBackground());

        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        g2.dispose();
        super.paintComponent(g);
    }
}