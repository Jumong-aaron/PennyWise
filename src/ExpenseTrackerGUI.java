import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class ExpenseTrackerGUI extends JFrame {

    private final ExpensesManager manager = new ExpensesManager();
    private final FileManager fileManager = new FileManager(
            "data/pennywise_expenses.txt", "data/pennywise_goal.txt");
    private String goalName = "";
    private double goalTarget = 0;
    private double goalSaved = 0;

    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryBox;

    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel foodLabel;
    private JLabel transportLabel;
    private JLabel schoolLabel;
    private JLabel othersLabel;
    private JLabel statusLabel;

    private JTextField goalNameField;
    private JTextField goalTargetField;
    private JTextField goalAddField;
    private JLabel goalTitleLabel;
    private JLabel goalSavedLabel;
    private JLabel goalStatusLabel;
    private GoalProgressBar progressBar;

    private ScrollablePanel dashboardBody;
    private JScrollPane dashboardScroll;
    private JPanel formCard;
    private JPanel tableCard;
    private JPanel totalCard;
    private JPanel goalCard;
    private JPanel categoryCard;

    private Timer statusTimer;


    public ExpenseTrackerGUI() {
        loadData();

        setTitle("Penny Wise - Personal Expense Tracker");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        refreshTable();
        refreshGoal();
    }
  

    private void loadData() {
        manager.setExpenses(fileManager.loadExpenses());

        SavingsGoal goal = fileManager.loadGoal();
        goalName = goal.name;
        goalTarget = goal.target;
        goalSaved = goal.saved;
    }

    private void saveExpenses() {
        try {
            fileManager.saveExpenses(manager.getExpenses());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save your expenses: " + ex.getMessage(),
                    "Penny Wise", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveGoal() {
        try {
            fileManager.saveGoal(goalName, goalTarget, goalSaved);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save your savings goal: " + ex.getMessage(),
                    "Penny Wise", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void createGUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);
        add(createSidebar(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);
    }


    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Theme.LIGHT_GREEN);
        sidebar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, Theme.BORDER_COLOR));

       
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(12, 10, 8, 0));
        AnimatedLogo logo = new AnimatedLogo(loadLogo(), 180);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(logo);
        sidebar.add(top, BorderLayout.NORTH);

        
        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        nav.add(navButton("Home", () -> scrollToTop()));
        nav.add(Box.createVerticalStrut(6));

        nav.add(navButton("View Expenses", () -> {
            scrollTo(tableCard);
            expenseTable.requestFocusInWindow();
        }));
        nav.add(Box.createVerticalStrut(6));
        nav.add(navButton("Savings Goal", () -> {
            scrollTo(goalCard);
            goalNameField.requestFocusInWindow();
        }));
        nav.add(Box.createVerticalStrut(6));
        nav.add(navButton("Category Summary", () -> scrollTo(categoryCard)));
        nav.add(Box.createVerticalStrut(6));
        nav.add(navButton("Delete Expense", () -> deleteExpense()));

        sidebar.add(nav, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "<html><center>Small steps<br>build big savings.</center></html>",
                SwingConstants.CENTER);
        footer.setForeground(Theme.DARK_GREEN);
        footer.setFont(Theme.bubble(Font.PLAIN, 15));
        footer.setBorder(BorderFactory.createEmptyBorder(15, 10, 30, 10));
        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private BufferedImage loadLogo() {
        try {
            File f = new File(Theme.LOGO_FILE);
            if (f.exists()) {
                return ImageIO.read(f);
            }
        } catch (IOException ex) {
      
        }
        return null;
    }

    private BubbleButton navButton(String text, Runnable action) {
        BubbleButton b = new BubbleButton(text, Theme.LIGHT_GREEN,
                new Color(200, 232, 214), Theme.DARK_GREEN);
        b.setFont(Theme.bubble(Font.BOLD, 16));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 10));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setPreferredSize(new Dimension(200, 46));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.addActionListener(e -> action.run());
        return b;
    }


    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(Theme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 20));

        JPanel welcome = new JPanel();
        welcome.setBackground(Theme.BACKGROUND);
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to Penny Wise!");
        welcomeLabel.setFont(Theme.bubble(Font.BOLD, 34));
        welcomeLabel.setForeground(Theme.DARK_GREEN);

        JLabel welcomeSub = new JLabel(
                "Manage your expenses and reach your savings goals.");
        welcomeSub.setFont(Theme.bubble(Font.PLAIN, 19));
        welcomeSub.setForeground(Theme.DARK_GREEN);

        welcome.add(welcomeLabel);
        welcome.add(Box.createVerticalStrut(4));
        welcome.add(welcomeSub);
        welcome.add(Box.createVerticalStrut(10));
        content.add(welcome, BorderLayout.NORTH);

        // cards
        formCard = createExpenseForm();
        tableCard = createExpenseTable();
        totalCard = createTotalPanel();
        goalCard = createGoalPanel();
        categoryCard = createCategoryPanel();

        JPanel left = stack(560, formCard, tableCard);
        JPanel right = stack(370, totalCard, goalCard, categoryCard);

        dashboardBody = new ScrollablePanel(new GridBagLayout());
        dashboardBody.setBackground(Theme.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 18);
        dashboardBody.add(left, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        dashboardBody.add(right, gbc);

        dashboardScroll = new JScrollPane(dashboardBody);
        dashboardScroll.setBorder(null);
        dashboardScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dashboardScroll.getViewport().setBackground(Theme.BACKGROUND);
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(dashboardScroll, BorderLayout.CENTER);
        return content;
    }


    private JPanel stack(final int minWidth, JComponent... cards) {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(Math.max(d.width, minWidth), d.height);
            }
        };
        p.setBackground(Theme.BACKGROUND);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 16, 0);

        int row = 0;
        for (JComponent card : cards) {
            c.gridy = row++;
            p.add(card, c);
        }

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        c.gridy = row;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(filler, c);
        return p;
    }

    private void scrollToTop() {
        dashboardScroll.getViewport().setViewPosition(new Point(0, 0));
    }

    private void scrollTo(JComponent target) {
        Rectangle r = SwingUtilities.convertRectangle(
                target.getParent(), target.getBounds(), dashboardBody);
        int maxY = Math.max(0, dashboardBody.getHeight()
                - dashboardScroll.getViewport().getHeight());
        int y = Math.min(Math.max(0, r.y - 8), maxY);
        dashboardScroll.getViewport().setViewPosition(new Point(0, y));
    }


 

    private JPanel card(String title, JComponent body) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(panelBorder());
        p.add(createSectionTitle(title), BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }


    private JPanel createExpenseForm() {
        descriptionField = styledField();
        descriptionField.setToolTipText("Example: Lunch, Jeepney, Notebook");

        amountField = styledField();
        amountField.setToolTipText("Example: 150.50");
        amountField.addActionListener(e -> addExpense());

        categoryBox = new JComboBox<>(new String[]{
            "Food", "Transportation", "School", "Others"});
        categoryBox.setFont(Theme.bubble(Font.PLAIN, 15));
        categoryBox.setBackground(Color.WHITE);

        dateField = styledField();
        dateField.setText(LocalDate.now().toString());

        JPanel fields = new JPanel(new GridLayout(2, 2, 15, 12));
        fields.setBackground(Color.WHITE);
        fields.setBorder(BorderFactory.createEmptyBorder(14, 14, 6, 14));
        fields.add(createInputGroup("Description:", descriptionField));
        fields.add(createInputGroup("Amount (" + Theme.PESO + "):", amountField));
        fields.add(createInputGroup("Category:", categoryBox));
        fields.add(createInputGroup("Date (YYYY-MM-DD):", dateField));

        BubbleButton addButton = bubbleButton("Add Expense", Theme.BUTTON_GREEN,
                Theme.BUTTON_GREEN_HOVER, Color.WHITE, 150, 42);
        BubbleButton clearButton = bubbleButton("Clear",
                new Color(232, 240, 238), new Color(214, 228, 224),
                Theme.DARK_GREEN, 100, 42);

        addButton.addActionListener(e -> addExpense());
        clearButton.addActionListener(e -> clearForm());

        statusLabel = new JLabel(" ");
        statusLabel.setFont(Theme.bubble(Font.BOLD, 14));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttons.setBackground(Color.WHITE);
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));
        buttons.add(addButton);
        buttons.add(clearButton);
        buttons.add(statusLabel);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(fields, BorderLayout.CENTER);
        body.add(buttons, BorderLayout.SOUTH);

        return card("Add Expense", body);
    }


    private JPanel createExpenseTable() {
        tableModel = new DefaultTableModel(new String[]{
            "No.", "Description", "Amount", "Category", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(36);
        expenseTable.setFont(Theme.money(Font.PLAIN, 14));
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setGridColor(new Color(225, 238, 234));
        expenseTable.setShowGrid(true);
        expenseTable.setFillsViewportHeight(true);

        JTableHeader header = expenseTable.getTableHeader();
        header.setFont(Theme.bubble(Font.BOLD, 15));
        header.setBackground(new Color(235, 243, 242));
        header.setForeground(Theme.DARK_GREEN);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 38));

        int[] widths = {45, 170, 110, 120, 105};
        int[] aligns = {SwingConstants.CENTER, SwingConstants.LEFT,
            SwingConstants.RIGHT, SwingConstants.CENTER,
            SwingConstants.CENTER};
        for (int i = 0; i < widths.length; i++) {
            expenseTable.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
            expenseTable.getColumnModel().getColumn(i)
                    .setCellRenderer(new RowRenderer(aligns[i]));
        }

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(0, 320));
        scrollPane.getViewport().setBackground(Color.WHITE);

        return card("Expense Records", scrollPane);
    }


    private JPanel createTotalPanel() {
        totalLabel = new JLabel(Utils.peso(0));
        totalLabel.setFont(Theme.money(Font.BOLD, 36));
        totalLabel.setForeground(Theme.DARK_GREEN);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 22, 10));
        return card("Total Expenses", totalLabel);
    }


    private JPanel createGoalPanel() {
        goalNameField = styledField();
        goalNameField.setToolTipText(
                "Example: New laptop, Emergency fund, Trip to Cebu");
        goalTargetField = styledField();
        goalTargetField.setToolTipText("Example: 25000");
        goalAddField = styledField();
        goalAddField.setToolTipText("How much are you adding today?");

        BubbleButton setGoalButton = bubbleButton("Set Goal", Theme.BUTTON_GREEN,
                Theme.BUTTON_GREEN_HOVER, Color.WHITE, 105, 40);
        BubbleButton addSavingsButton = bubbleButton("Add Savings", Theme.GOLD,
                new Color(225, 155, 10), Theme.DARK_GREEN, 130, 40);
        BubbleButton resetButton = bubbleButton("Reset Goal",
                new Color(232, 240, 238), new Color(214, 228, 224),
                Theme.DARK_GREEN, 120, 36);

        setGoalButton.addActionListener(e -> setGoal());
        addSavingsButton.addActionListener(e -> addSavings());
        goalAddField.addActionListener(e -> addSavings());
        resetButton.addActionListener(e -> resetGoal());

        goalTitleLabel = new JLabel("No goal yet");
        goalTitleLabel.setFont(Theme.bubble(Font.BOLD, 18));
        goalTitleLabel.setForeground(Theme.DARK_GREEN);

        progressBar = new GoalProgressBar();

        goalSavedLabel = new JLabel(" ");
        goalSavedLabel.setFont(Theme.money(Font.BOLD, 15));
        goalSavedLabel.setForeground(Theme.DARK_GREEN);

        goalStatusLabel = new JLabel(" ");
        goalStatusLabel.setFont(Theme.money(Font.PLAIN, 14));
        goalStatusLabel.setForeground(new Color(70, 110, 100));

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));

        body.add(fullWidth(createInputGroup(
                "What are you saving for?", goalNameField)));
        body.add(Box.createVerticalStrut(8));
        body.add(fullWidth(inputWithButton(
                "Goal amount (" + Theme.PESO + "):", goalTargetField, setGoalButton)));
        body.add(Box.createVerticalStrut(16));
        body.add(fullWidth(goalTitleLabel));
        body.add(Box.createVerticalStrut(6));
        body.add(fullWidth(progressBar));
        body.add(Box.createVerticalStrut(6));
        body.add(fullWidth(goalSavedLabel));
        body.add(fullWidth(goalStatusLabel));
        body.add(Box.createVerticalStrut(14));
        body.add(fullWidth(inputWithButton(
                "Add to your savings (" + Theme.PESO + "):",
                goalAddField, addSavingsButton)));
        body.add(Box.createVerticalStrut(10));

        JPanel resetRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        resetRow.setOpaque(false);
        resetRow.add(resetButton);
        body.add(fullWidth(resetRow));

        return card("Savings Goal", body);
    }

    private JPanel inputWithButton(String label, JComponent field,
            JComponent button) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.add(createInputGroup(label, field), BorderLayout.CENTER);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(button, BorderLayout.SOUTH);
        row.add(wrap, BorderLayout.EAST);
        return row;
    }

    private <T extends JComponent> T fullWidth(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                c.getPreferredSize().height));
        return c;
    }


    private JPanel createCategoryPanel() {
        foodLabel = new JLabel(Utils.peso(0));
        transportLabel = new JLabel(Utils.peso(0));
        schoolLabel = new JLabel(Utils.peso(0));
        othersLabel = new JLabel(Utils.peso(0));

        JPanel rows = new JPanel(new GridLayout(4, 1, 5, 8));
        rows.setBackground(Color.WHITE);
        rows.setBorder(BorderFactory.createEmptyBorder(14, 14, 16, 14));
        rows.add(createCategoryRow("Food",
                new Color(55, 180, 85), foodLabel));
        rows.add(createCategoryRow("Transportation",
                new Color(35, 145, 235), transportLabel));
        rows.add(createCategoryRow("School",
                new Color(255, 180, 20), schoolLabel));
        rows.add(createCategoryRow("Others",
                new Color(155, 40, 210), othersLabel));

        return card("Category Summary", rows);
    }

    private JPanel createCategoryRow(String name, Color color,
            JLabel amountLabel) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(name, new DotIcon(color, 18),
                SwingConstants.LEFT);
        nameLabel.setIconTextGap(10);
        nameLabel.setFont(Theme.bubble(Font.PLAIN, 16));
        nameLabel.setForeground(Theme.DARK_GREEN);

        amountLabel.setFont(Theme.money(Font.BOLD, 15));
        amountLabel.setForeground(Theme.DARK_GREEN);
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(nameLabel, BorderLayout.CENTER);
        row.add(amountLabel, BorderLayout.EAST);
        return row;
    }

    private JPanel createSectionTitle(String title) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setBackground(Theme.LIGHT_GREEN);
        JLabel label = new JLabel(title);
        label.setFont(Theme.bubble(Font.BOLD, 20));
        label.setForeground(Theme.DARK_GREEN);
        header.add(label);
        return header;
    }

    private JPanel createInputGroup(String labelText, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(5, 5));
        group.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(Theme.bubble(Font.BOLD, 14));
        label.setForeground(new Color(35, 85, 80));

        field.setPreferredSize(new Dimension(100, 40));

        group.add(label, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(Theme.money(Font.PLAIN, 15));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return f;
    }

    private BubbleButton bubbleButton(String text, Color base, Color hover,
            Color foreground, int width, int height) {
        BubbleButton b = new BubbleButton(text, base, hover, foreground);
        b.setFont(Theme.bubble(Font.BOLD, 15));
        b.setPreferredSize(new Dimension(width, height));
        return b;
    }

    private Border panelBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(0, 0, 4, 0));
    }

    private void showStatus(String message, Color color) {
        statusLabel.setForeground(color);
        statusLabel.setText(message);
        if (statusTimer != null) {
            statusTimer.stop();
        }
        statusTimer = new Timer(3000, e -> statusLabel.setText(" "));
        statusTimer.setRepeats(false);
        statusTimer.start();
    }



    private void clearForm() {
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText(LocalDate.now().toString());
        categoryBox.setSelectedIndex(0);
        descriptionField.requestFocusInWindow();
    }

    public void addExpense() {
        String description = Utils.clean(descriptionField.getText());
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an expense description.");
            return;
        }

        Double amount = Utils.parseMoney(amountField.getText());
        if (amount == null) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount greater than zero "
                    + "(example: 150.50).");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid date (YYYY-MM-DD).");
            return;
        }

        String category = (String) categoryBox.getSelectedItem();
        manager.addExpense(new Expenses(description, amount, category, date));
        saveExpenses();
        refreshTable();
        clearForm();
        showStatus("Expense added!", Theme.BUTTON_GREEN);
    }

    public void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an expense from the table first.");
            scrollTo(tableCard);
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this expense?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            int modelRow = expenseTable.convertRowIndexToModel(selectedRow);
            manager.deleteExpense(modelRow);
            saveExpenses();
            refreshTable();
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        int number = 1;
        for (Expenses e : manager.getExpenses()) {
            tableModel.addRow(new Object[]{
                number++, e.getDescription(), Utils.peso(e.getAmount()),
                e.getCategory(), e.getDate()});
        }

        displayTotal();
    }

    public void displayTotal() {
        totalLabel.setText(Utils.peso(manager.calculateTotal()));
        foodLabel.setText(Utils.peso(manager.calculateTotal("Food")));
        transportLabel.setText(Utils.peso(manager.calculateTotal("Transportation")));
        schoolLabel.setText(Utils.peso(manager.calculateTotal("School")));
        othersLabel.setText(Utils.peso(manager.calculateTotal("Others")));
    }


  
    private void setGoal() {
        String name = Utils.clean(goalNameField.getText());
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter what you are saving for.");
            return;
        }
        Double target = Utils.parseMoney(goalTargetField.getText());
        if (target == null) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a goal amount greater than zero "
                    + "(example: 25000).");
            return;
        }

        goalName = name;
        goalTarget = target;
        saveGoal();
        refreshGoal();
    }

    private void addSavings() {
        if (goalTarget <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Set a savings goal first, then add your savings.");
            return;
        }
        Double amount = Utils.parseMoney(goalAddField.getText());
        if (amount == null) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid savings amount greater than zero.");
            return;
        }

        boolean alreadyReached = goalSaved >= goalTarget;
        goalSaved = Math.round((goalSaved + amount) * 100) / 100.0;
        goalAddField.setText("");
        saveGoal();
        refreshGoal();

        if (!alreadyReached && goalSaved >= goalTarget) {
            JOptionPane.showMessageDialog(this,
                    "Congratulations! You reached your goal:\n" + goalName,
                    "Goal reached!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetGoal() {
        if (goalTarget <= 0 && goalSaved <= 0) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Reset your savings goal and saved amount?",
                "Reset Goal", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            goalName = "";
            goalTarget = 0;
            goalSaved = 0;
            goalNameField.setText("");
            goalTargetField.setText("");
            saveGoal();
            refreshGoal();
        }
    }

    private void refreshGoal() {
        if (goalTarget <= 0) {
            progressBar.setProgress(0);
            goalTitleLabel.setText("No goal yet");
            goalSavedLabel.setText("Set a goal to start tracking.");
            goalStatusLabel.setText(" ");
            return;
        }

        if (goalNameField.getText().isEmpty()) {
            goalNameField.setText(goalName);
        }
        if (goalTargetField.getText().isEmpty()) {
            goalTargetField.setText(String.format(
                    Locale.US, "%.2f", goalTarget));
        }

        progressBar.setProgress(goalSaved / goalTarget);
        goalTitleLabel.setText(goalName);
        goalSavedLabel.setText(Utils.peso(goalSaved) + " saved of "
                + Utils.peso(goalTarget));

        if (goalSaved >= goalTarget) {
            goalStatusLabel.setForeground(new Color(190, 120, 0));
            goalStatusLabel.setText("Goal reached! Great job!");
        } else {
            goalStatusLabel.setForeground(new Color(70, 110, 100));
            goalStatusLabel.setText(Utils.peso(goalTarget - goalSaved)
                    + " to go");
        }
    }


    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
           
        }
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }
}
