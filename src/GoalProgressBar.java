import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.Timer;

public class GoalProgressBar extends JComponent {
    private double shown = 0;
    private double goal = 0;
    private final Timer animation;

    public GoalProgressBar() {
        setPreferredSize(new Dimension(100, 38));
        animation = new Timer(16, e -> {
            double diff = goal - shown;
            if (Math.abs(diff) < 0.002) {
                shown = goal;
                ((Timer) e.getSource()).stop();
            } else {
                shown += diff * 0.12;
            }
            repaint();
        });
    }

    public void setProgress(double ratio) {
        goal = Math.max(0, Math.min(1, ratio));
        animation.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(225, 236, 232));
        g2.fillRoundRect(0, 0, w, h, h, h);

        int fillW = (int) Math.round(w * shown);
        Shape fillShape = null;
        if (fillW > 0) {
            fillShape = new RoundRectangle2D.Double(0, 0, fillW, h, h, h);
            boolean done = shown >= 0.999;
            Color top = done ? new Color(255, 205, 70)
                    : new Color(80, 195, 120);
            Color bottom = done ? new Color(240, 160, 20)
                    : new Color(35, 140, 85);
            g2.setPaint(new GradientPaint(0, 0, top, 0, h, bottom));
            g2.fill(fillShape);

            if (fillW > 30) {
                g2.setColor(new Color(255, 255, 255, 70));
                g2.fill(new RoundRectangle2D.Double(
                        8, 4, fillW - 16, h / 3.0, h / 3.0, h / 3.0));
            }
        }

        String text = Math.round(shown * 100) + "%";
        g2.setFont(Theme.bubble(Font.BOLD, 17));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(Theme.DARK_GREEN);
        g2.drawString(text, tx, ty);
        if (fillShape != null) {
            g2.setClip(fillShape);
            g2.setColor(Color.WHITE);
            g2.drawString(text, tx, ty);
        }
        g2.dispose();
    }
}
