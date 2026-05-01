package gui;

import util.DataStore;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.NAVY);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCards(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.NAVY);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel title = new JLabel("CLUB MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 30));
        title.setForeground(Theme.GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Algorithms & Data Structures Project", SwingConstants.CENTER);
        sub.setFont(new Font("Tahoma", Font.ITALIC, 14));
        sub.setForeground(Theme.GRAY_TEXT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Gold separator line
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.GOLD);
        sep.setBackground(Theme.GOLD);
        sep.setMaximumSize(new Dimension(400, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(6));
        p.add(sub);
        p.add(Box.createVerticalStrut(15));
        p.add(sep);
        return p;
    }

    private JPanel buildCards() {
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(Theme.NAVY);

        grid.add(makeCard("🏟", "Clubs",
                String.valueOf(DataStore.getInstance().getClubs().size()),
                "Sorted by Name", "Bubble Sort — O(n²)"));

        grid.add(makeCard("👤", "Members",
                String.valueOf(DataStore.getInstance().getMembers().size()),
                "Sorted by ID", "Merge Sort — O(n log n)"));

        grid.add(makeCard("⚽", "Sports",
                String.valueOf(DataStore.getInstance().getSports().size()),
                "Sorted by Name", "Insertion Sort — O(n²)"));

        return grid;
    }

    private JPanel makeCard(String icon, String title, String count, String sortLabel, String complexity) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // gradient background
                GradientPaint gp = new GradientPaint(0, 0, Theme.NAVY_PANEL,
                                                      0, getHeight(), Theme.NAVY_LIGHT);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                // gold border
                g2.setColor(Theme.GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 18, 18);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(Theme.HEADER_FONT);
        titleLbl.setForeground(Theme.GOLD);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLbl = new JLabel(count + " records", SwingConstants.CENTER);
        countLbl.setFont(new Font("Tahoma", Font.BOLD, 22));
        countLbl.setForeground(Theme.WHITE);
        countLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 70, 120));
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sortLbl = new JLabel(sortLabel, SwingConstants.CENTER);
        sortLbl.setFont(Theme.SMALL_FONT);
        sortLbl.setForeground(Theme.GRAY_TEXT);
        sortLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel compLbl = new JLabel(complexity, SwingConstants.CENTER);
        compLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
        compLbl.setForeground(Theme.GOLD_LIGHT);
        compLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(countLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(sep);
        card.add(Box.createVerticalStrut(8));
        card.add(sortLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(compLbl);

        return card;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.NAVY);
        p.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Algorithm summary table
        String[][] rows = {
            {"Bubble Sort",    "O(n²)",      "O(n²)",   "O(1)",  "Clubs — Sort by Name"},
            {"Merge Sort",     "O(n log n)", "O(n log n)", "O(n)", "Members — Sort by ID"},
            {"Insertion Sort", "O(n)",       "O(n²)",   "O(1)",  "Sports — Sort by Name"},
            {"Binary Search",  "O(log n)",   "O(log n)","O(1)",  "All — Search"},
        };
        String[] cols = {"Algorithm", "Best", "Worst", "Space", "Used For"};
        JTable algTable = new JTable(rows, cols);
        algTable.setEnabled(false);
        ClubsPanel.styleTable(algTable);
        algTable.setRowHeight(24);

        JScrollPane sp = new JScrollPane(algTable);
        sp.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Theme.GOLD, 1),
            "  Algorithm Complexity Reference",
            0, 0, Theme.HEADER_FONT, Theme.GOLD
        ));
        sp.getViewport().setBackground(Theme.NAVY_LIGHT);
        sp.setPreferredSize(new Dimension(0, 145));

        p.add(sp, BorderLayout.CENTER);
        return p;
    }
}
