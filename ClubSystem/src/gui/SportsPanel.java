package gui;

import model.Sport;
import util.DataStore;
import util.SortingAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SportsPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public SportsPanel() {
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

        JLabel title = new JLabel("⚽  Sports");
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
        String[] cols = {"Name", "ID", "Number of Teams"};
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

        StyledButton addBtn    = new StyledButton("+ Add Sport",              true);
        StyledButton sortBtn   = new StyledButton("Sort by Name (Insertion)", false);
        StyledButton searchBtn = new StyledButton("Search by Name",           false);

        addBtn.addActionListener(e -> showAddDialog());
        sortBtn.addActionListener(e -> sortSports());
        searchBtn.addActionListener(e -> searchSport());

        p.add(addBtn); p.add(sortBtn); p.add(searchBtn);
        return p;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Sport s : DataStore.getInstance().getSports()) {
            tableModel.addRow(new Object[]{s.getName(), s.getId(), s.getNumberOfTeams()});
        }
        statusLabel.setText("Total: " + DataStore.getInstance().getSports().size() + " sports");
    }

    private void showAddDialog() {
        JTextField nameField  = ClubsPanel.styledField();
        JTextField idField    = ClubsPanel.styledField();
        JTextField teamsField = ClubsPanel.styledField();

        JPanel form = ClubsPanel.buildForm(
            new String[]{"Name:", "ID:", "Number of Teams:"},
            new JTextField[]{nameField, idField, teamsField}
        );

        int res = JOptionPane.showConfirmDialog(this, form, "Add New Sport",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                int id    = Integer.parseInt(idField.getText().trim());
                int teams = Integer.parseInt(teamsField.getText().trim());
                DataStore.getInstance().getSports().add(
                    new Sport(nameField.getText().trim(), id, teams)
                );
                refreshTable();
                setStatus("✔  Sport added.", true);
            } catch (NumberFormatException ex) {
                setStatus("✘  ID and Teams must be numbers.", false);
            }
        }
    }

    private void sortSports() {
        SortingAlgorithms.insertionSort(DataStore.getInstance().getSports());
        refreshTable();
        setStatus("✔  Sports sorted by name using Insertion Sort — O(n²) / O(n) best", true);
    }

    private void searchSport() {
        String query = JOptionPane.showInputDialog(this, "Enter sport name to search:");
        if (query == null || query.trim().isEmpty()) return;

        ArrayList<Sport> list = DataStore.getInstance().getSports();
        SortingAlgorithms.insertionSort(list);

        Sport dummy = new Sport(query.trim(), 0, 0);
        int idx = SortingAlgorithms.binarySearch(list, dummy);

        if (idx >= 0) {
            refreshTable();
            table.setRowSelectionInterval(idx, idx);
            table.scrollRectToVisible(table.getCellRect(idx, 0, true));
            setStatus("✔  Sport '" + query.trim() + "' found at row " + (idx + 1) + " — Binary Search O(log n)", true);
        } else {
            refreshTable();
            setStatus("✘  Sport '" + query.trim() + "' not found.", false);
        }
    }

    private void setStatus(String msg, boolean ok) {
        statusLabel.setForeground(ok ? Theme.SUCCESS : Theme.ERROR_COLOR);
        statusLabel.setText(msg);
    }
}
