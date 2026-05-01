package gui;

import model.Member;
import util.DataStore;
import util.SortingAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MembersPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public MembersPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(Theme.NAVY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.NAVY);

        JLabel title = new JLabel("👤  Members");
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

    private JScrollPane buildTable() {
        String[] cols = {"ID", "Name", "Phone", "Children"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        ClubsPanel.styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.NAVY_LIGHT);
        sp.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 1));
        return sp;
    }

    private JPanel buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(Theme.NAVY);

        StyledButton addBtn    = new StyledButton("+ Add Member",        true);
        StyledButton sortBtn   = new StyledButton("Sort by ID (Merge)", false);
        StyledButton searchBtn = new StyledButton("Search by ID",       false);

        addBtn.addActionListener(e -> showAddDialog());
        sortBtn.addActionListener(e -> sortMembers());
        searchBtn.addActionListener(e -> searchMember());

        p.add(addBtn); p.add(sortBtn); p.add(searchBtn);
        return p;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Member m : DataStore.getInstance().getMembers()) {
            tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getPhone(), m.getNumberOfChildren()});
        }
        statusLabel.setText("Total: " + DataStore.getInstance().getMembers().size() + " members");
    }

    private void showAddDialog() {
        JTextField idField       = ClubsPanel.styledField();
        JTextField nameField     = ClubsPanel.styledField();
        JTextField phoneField    = ClubsPanel.styledField();
        JTextField childrenField = ClubsPanel.styledField();

        JPanel form = ClubsPanel.buildForm(
            new String[]{"ID:", "Name:", "Phone:", "No. of Children:"},
            new JTextField[]{idField, nameField, phoneField, childrenField}
        );

        int res = JOptionPane.showConfirmDialog(this, form, "Add New Member",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                int ch = Integer.parseInt(childrenField.getText().trim());
                DataStore.getInstance().getMembers().add(
                    new Member(id, nameField.getText().trim(), phoneField.getText().trim(), ch)
                );
                refreshTable();
                setStatus("✔  Member added.", true);
            } catch (NumberFormatException ex) {
                setStatus("✘  ID and Children must be numbers.", false);
            }
        }
    }

    private void sortMembers() {
        ArrayList<Member> list = DataStore.getInstance().getMembers();
        SortingAlgorithms.mergeSort(list, 0, list.size() - 1);
        refreshTable();
        setStatus("✔  Members sorted by ID using Merge Sort — O(n log n)", true);
    }

    private void searchMember() {
        String input = JOptionPane.showInputDialog(this, "Enter Member ID to search:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int id = Integer.parseInt(input.trim());

            ArrayList<Member> list = DataStore.getInstance().getMembers();
            SortingAlgorithms.mergeSort(list, 0, list.size() - 1);

            Member dummy = new Member(id, "", "", 0);
            int idx = SortingAlgorithms.binarySearch(list, dummy);

            if (idx >= 0) {
                refreshTable();
                table.setRowSelectionInterval(idx, idx);
                table.scrollRectToVisible(table.getCellRect(idx, 0, true));
                setStatus("✔  Member ID " + id + " found at row " + (idx + 1) + " — Binary Search O(log n)", true);
            } else {
                refreshTable();
                setStatus("✘  Member ID " + id + " not found.", false);
            }
        } catch (NumberFormatException ex) {
            setStatus("✘  Please enter a valid number.", false);
        }
    }

    private void setStatus(String msg, boolean ok) {
        statusLabel.setForeground(ok ? Theme.SUCCESS : Theme.ERROR_COLOR);
        statusLabel.setText(msg);
    }
}
