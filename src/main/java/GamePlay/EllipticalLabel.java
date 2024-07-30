package GamePlay;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EllipticalLabel extends JLabel {
    public EllipticalLabel(String text) {
        super(text); // Set default text
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create an elliptical shape
        Ellipse2D ellipse = new Ellipse2D.Double(0, 0, getWidth(), getHeight());

        // Fill the ellipse with red color
        g2d.setColor(new Color(70, 128, 116));
        g2d.fill(ellipse);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));

        // Draw the text
        g2d.setColor(new Color(157, 76, 76)); // Set text color to green
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(getText(), x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Elliptical Label");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EllipticalLabel label = new EllipticalLabel("jjj");
        label.setText("Hello, World!"); // Change the text later

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setSize(200, 100);
        frame.setVisible(true);
    }
}