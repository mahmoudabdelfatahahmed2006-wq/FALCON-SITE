package gui;

import model.Club;
import util.DataStore;
import util.SortingAlgorithms;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class ClubsPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public ClubsPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(Theme.NAVY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        refreshTable();
    }

    // ── Top Bar ──────────────────────────────────
    private JPanel buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.NAVY);

        JLabel title = new JLabel("🏟  Clubs");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.GOLD);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(Theme.SMALL_FONT);
        statusLabel.setForeground(Theme.GRAY_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        p.add(title,       BorderLayout.WEST);
        p.add(statusLabel, BorderLayout.EAST);
        return p;
    }

    // ── Table ─────────────────────────────────────
    private JScrollPane buildTable() {
        String[] cols = {"Name", "Branches", "Manager", "Location"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.NAVY_LIGHT);
        sp.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 1));
        return sp;
    }

    // ── Action Buttons ────────────────────────────
    private JPanel buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(Theme.NAVY);

        StyledButton addBtn    = new StyledButton("+ Add Club",        true);
        StyledButton sortBtn   = new StyledButton("Sort by Name (Bubble)", false);
        StyledButton searchBtn = new StyledButton("Search by Name",    false);

        addBtn.addActionListener(e -> showAddDialog());
        sortBtn.addActionListener(e -> sortClubs());
        searchBtn.addActionListener(e -> searchClub());

        p.add(addBtn);
        p.add(sortBtn);
        p.add(searchBtn);
        return p;
    }

    // ── Refresh Table ─────────────────────────────
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Club c : DataStore.getInstance().getClubs()) {
            tableModel.addRow(new Object[]{c.getName(), c.getBranches(), c.getManager(), c.getLocation()});
        }
        statusLabel.setText("Total: " + DataStore.getInstance().getClubs().size() + " clubs");
    }

    // ── Add Dialog ───────────────────────────────
    private void showAddDialog() {
        JTextField nameField     = styledField();
        JTextField branchField   = styledField();
        JTextField managerField  = styledField();
        JTextField locationField = styledField();

        JPanel form = buildForm(
            new String[]{"Name:", "Branches:", "Manager:", "Location:"},
            new JTextField[]{nameField, branchField, managerField, locationField}
        );

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Club",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { showError("Name cannot be empty."); return; }
            DataStore.getInstance().getClubs().add(
                new Club(name, branchField.getText().trim(),
                         managerField.getText().trim(), locationField.getText().trim())
            );
            refreshTable();
            setStatus("✔  Club '" + name + "' added.", true);
        }
    }

    // ── Sort ─────────────────────────────────────
    private void sortClubs() {
        ArrayList<Club> list = DataStore.getInstance().getClubs();
        SortingAlgorithms.bubbleSort(list);
        refreshTable();
        setStatus("✔  Clubs sorted by name using Bubble Sort — O(n²)", true);
    }

    // ── Search ───────────────────────────────────
    private void searchClub() {
        String query = JOptionPane.showInputDialog(this, "Enter club name to search:");
        if (query == null || query.trim().isEmpty()) return;

        // Sort first so binary search works
        ArrayList<Club> list = DataStore.getInstance().getClubs();
        SortingAlgorithms.bubbleSort(list);

        // Binary search using a dummy Club
        Club dummy = new Club(query.trim(), "", "", "");
        int idx = SortingAlgorithms.binarySearch(list, dummy);

        if (idx >= 0) {
            table.setRowSelectionInterval(idx, idx);
            table.scrollRectToVisible(table.getCellRect(idx, 0, true));
            setStatus("✔  Found '" + query.trim() + "' at row " + (idx + 1) + "  — Binary Search O(log n)", true);
        } else {
            setStatus("✘  Club '" + query.trim() + "' not found.", false);
        }
        refreshTable();
    }

    // ── Helpers ───────────────────────────────────
    private void setStatus(String msg, boolean ok) {
        statusLabel.setForeground(ok ? Theme.SUCCESS : Theme.ERROR_COLOR);
        statusLabel.setText(msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void styleTable(JTable t) {
        t.setBackground(Theme.TABLE_ROW1);
        t.setForeground(Theme.WHITE);
        t.setFont(Theme.TABLE_FONT);
        t.setRowHeight(28);
        t.setGridColor(new Color(30, 55, 110));
        t.setSelectionBackground(Theme.GOLD);
        t.setSelectionForeground(Theme.NAVY);
        t.setShowVerticalLines(true);

        JTableHeader header = t.getTableHeader();
        header.setBackground(Theme.NAVY_PANEL);
        header.setForeground(Theme.GOLD);
        header.setFont(Theme.HEADER_FONT);
        header.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 1));

        // Alternating rows renderer
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (sel) {
                    setBackground(Theme.GOLD);
                    setForeground(Theme.NAVY);
                } else {
                    setBackground(row % 2 == 0 ? Theme.TABLE_ROW1 : Theme.TABLE_ROW2);
                    setForeground(Theme.WHITE);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    static JTextField styledField() {
        JTextField f = new JTextField(18);
        f.setBackground(new Color(20, 40, 90));
        f.setForeground(Theme.WHITE);
        f.setCaretColor(Theme.GOLD);
        f.setFont(Theme.BODY_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.GOLD, 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return f;
    }

    static JPanel buildForm(String[] labels, JTextField[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.NAVY_LIGHT);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Theme.GOLD);
            lbl.setFont(Theme.BODY_FONT);
            p.add(lbl, gc);
            gc.gridx = 1;
            p.add(fields[i], gc);
        }
        return p;
    }
}
