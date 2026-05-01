package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private JPanel contentArea;
    private CardLayout cardLayout;

    // Nav buttons
    private StyledButton activeBtn;

    public MainFrame() {
        setTitle("Club Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Theme.NAVY);

        add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.NAVY);
        contentArea.add(new DashboardPanel(), "dashboard");
        contentArea.add(new ClubsPanel(),     "clubs");
        contentArea.add(new MembersPanel(),   "members");
        contentArea.add(new SportsPanel(),    "sports");

        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Theme.NAVY_PANEL,
                                                      0, getHeight(), Theme.NAVY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setOpaque(false);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(195, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 65, 130)));

        // Logo area
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("⚜", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));
        logo.setForeground(Theme.GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("CLUB SYSTEM", SwingConstants.CENTER);
        appName.setFont(new Font("Georgia", Font.BOLD, 13));
        appName.setForeground(Theme.GOLD);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 85, 150));
        sep.setMaximumSize(new Dimension(160, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logo);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(appName);
        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(sep);

        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(10));

        // Nav items
        String[][] navItems = {
            {"🏠", "  Dashboard", "dashboard"},
            {"🏟", "  Clubs",     "clubs"},
            {"👤", "  Members",   "members"},
            {"⚽", "  Sports",    "sports"},
        };

        for (String[] item : navItems) {
            JButton btn = makeNavButton(item[0] + item[1], item[2]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // Footer
        JLabel footer = new JLabel("Algorithms Project", SwingConstants.CENTER);
        footer.setFont(Theme.SMALL_FONT);
        footer.setForeground(new Color(80, 100, 150));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);
        sidebar.add(Box.createVerticalStrut(15));

        return sidebar;
    }

    private JButton makeNavButton(String label, String card) {
        JButton btn = new JButton(label) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(212, 175, 55, 60));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(212, 175, 55, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setForeground(Theme.GRAY_TEXT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(180, 42));
        btn.setPreferredSize(new Dimension(180, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addActionListener(e -> {
            cardLayout.show(contentArea, card);
            // Update active style
            btn.setForeground(Theme.GOLD);
            if (activeBtn != null && activeBtn != btn) {
                // reset previous - handled via repaint
            }
        });

        // Activate dashboard by default
        if (card.equals("dashboard")) {
            btn.setForeground(Theme.GOLD);
        }

        return btn;
    }
}
