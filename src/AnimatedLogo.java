import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedLogo extends JPanel {
    private final BufferedImage scaled;
    private final int logoSize;
    private double t = 0;
    private double hopY = 0;
    private double hopVel = 0;

    public AnimatedLogo(BufferedImage source, int logoSize) {
        this.logoSize = logoSize;
        this.scaled = source == null ? null
                : scaleSmooth(source, logoSize * 2);

        setOpaque(false);
        Dimension size = new Dimension(logoSize + 44, logoSize + 64);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText("Click me!");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (hopY == 0 && hopVel == 0) {
                    hopVel = -5.0; // little jump
                }
            }
        });

        Timer timer = new Timer(25, e -> {
            t += 0.05;
            if (hopVel != 0 || hopY != 0) {
                hopVel += 0.7;
                hopY += hopVel;
                if (hopY >= 0) {
                    hopY = 0;
                    hopVel = hopVel > 2.5 ? -hopVel * 0.35 : 0;
                }
            }
            repaint();
        });
        timer.start();
    }

    private static BufferedImage scaleSmooth(BufferedImage src,
            int targetWidth) {
        int targetHeight = Math.max(1, (int) Math.round(
                src.getHeight() * (double) targetWidth / src.getWidth()));
        BufferedImage cur = src;
        int w = cur.getWidth();
        int h = cur.getHeight();
        while (w / 2 >= targetWidth) {
            w /= 2;
            h /= 2;
            cur = drawScaled(cur, w, h);
        }
        return drawScaled(cur, targetWidth, targetHeight);
    }

    private static BufferedImage drawScaled(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return out;
    }

    private void drawSparkle(Graphics2D g2, double x, double y,
            double r, int alpha) {
        double k = r * 0.28;
        Path2D.Double star = new Path2D.Double();
        star.moveTo(x, y - r);
        star.lineTo(x + k, y - k);
        star.lineTo(x + r, y);
        star.lineTo(x + k, y + k);
        star.lineTo(x, y + r);
        star.lineTo(x - k, y + k);
        star.lineTo(x - r, y);
        star.lineTo(x - k, y - k);
        star.closePath();
        g2.setColor(new Color(255, 196, 40,
                Math.max(0, Math.min(255, alpha))));
        g2.fill(star);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();
        double cx = w / 2.0;
        double cy = h / 2.0 - 6;

        double bob = Math.sin(t) * 6;
        double offset = bob + hopY;
        double lift = -offset;

        // soft shadow that shrinks while the logo floats up
        double shadowW = logoSize * 0.62 * (1 - lift * 0.008);
        int shadowAlpha = (int) Math.max(12, Math.min(80, 60 - lift * 1.2));
        g2.setColor(new Color(15, 96, 70, shadowAlpha));
        g2.fill(new Ellipse2D.Double(cx - shadowW / 2, h - 18,
                shadowW, 9));

        AffineTransform old = g2.getTransform();
        g2.translate(cx, cy + offset);
        g2.rotate(Math.toRadians(2.2 * Math.sin(t * 0.8)));
        double s = 1 + 0.02 * Math.sin(t * 1.6);
        g2.scale(s, s);

        if (scaled != null) {
            int dh = (int) Math.round(logoSize
                    * (double) scaled.getHeight() / scaled.getWidth());
            g2.drawImage(scaled, -logoSize / 2, -dh / 2,
                    logoSize, dh, null);
        } else {
            g2.setColor(new Color(247, 190, 190));
            g2.fillOval(-logoSize / 2, -logoSize / 2, logoSize, logoSize);
            g2.setColor(Theme.DARK_GREEN);
            g2.setFont(new Font("Dialog", Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("Penny Wise",
                    -fm.stringWidth("Penny Wise") / 2, 8);
        }
        g2.setTransform(old);

        for (int i = 0; i < 5; i++) {
            double angle = t * 0.6 + i * (2 * Math.PI / 5);
            double sx = cx + Math.cos(angle) * logoSize * 0.55;
            double sy = cy + offset * 0.5
                    + Math.sin(angle) * logoSize * 0.47;
            double twinkle = 0.5 + 0.5 * Math.sin(t * 3 + i * 1.7);
            drawSparkle(g2, sx, sy, 3 + 4.5 * twinkle,
                    (int) (60 + 190 * twinkle));
        }

        g2.dispose();
    }
}

