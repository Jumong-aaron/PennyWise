package gui;

import components.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

 
public class SidebarPanel extends JPanel {

    public SidebarPanel(Runnable onHome, Runnable onViewExpenses,
            Runnable onSavingsGoal, Runnable onCategorySummary,
            Runnable onDeleteExpense) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBackground(Theme.LIGHT_GREEN);
        setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, Theme.BORDER_COLOR));


        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(12, 10, 8, 0));
        AnimatedLogo logo = new AnimatedLogo(loadLogo(), 180);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(logo);
        add(top, BorderLayout.NORTH);

       
        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        nav.add(UIComponents.navButton("Home", onHome));
        nav.add(Box.createVerticalStrut(6));
        nav.add(UIComponents.navButton("View Expenses", onViewExpenses));
        nav.add(Box.createVerticalStrut(6));
        nav.add(UIComponents.navButton("Savings Goal", onSavingsGoal));
        nav.add(Box.createVerticalStrut(6));
        nav.add(UIComponents.navButton("Category Summary", onCategorySummary));
        nav.add(Box.createVerticalStrut(6));
        nav.add(UIComponents.navButton("Delete Expense", onDeleteExpense));
        add(nav, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "<html><center>Small steps<br>build big savings.</center></html>",
                SwingConstants.CENTER);
        footer.setForeground(Theme.DARK_GREEN);
        footer.setFont(Theme.bubble(Font.PLAIN, 15));
        footer.setBorder(BorderFactory.createEmptyBorder(15, 10, 30, 10));
        add(footer, BorderLayout.SOUTH);
    }

    private static BufferedImage loadLogo() {
        try {
            File f = new File(Theme.LOGO_FILE);
            if (f.exists()) {
                return ImageIO.read(f);
            }
        } catch (IOException ex) {
          
        }
        return null;
    }
}
