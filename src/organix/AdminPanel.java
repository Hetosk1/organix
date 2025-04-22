package organix;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminPanel extends javax.swing.JFrame {
    private JTable organTable;
    private JButton approveButton, rejectButton, logoutButton;
    private JDBC jdbc;

    public AdminPanel() {
        initComponents();
        this.jdbc = new JDBC();
        loadOrganData();
    }

    private void initComponents() {
        setTitle("Organix - Admin Panel");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        organTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(organTable);
        
        approveButton = new JButton("Approve Selected");
        rejectButton = new JButton("Reject Selected");
        logoutButton = new JButton("Logout");
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(logoutButton);
        
        
        setLayout(new BorderLayout());
        add(new JLabel("Organ Donation Approvals", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedOrgans("approved");
            }
        });
        
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedOrgans("not_approved");
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminLogin().setVisible(true);
            }
        });
    }
    
    private void loadOrganData() {
        try {
            
            String query = "SELECT d.did, u.uid, d.oid, o.organ_name, u.email, u.name, u.blood_type, u.gender, d.status " +
                         "FROM donation d " +
                         "JOIN user u ON d.uid = u.uid " +
                         "JOIN organ o ON d.oid = o.oid " +
                         "ORDER BY d.status, o.organ_name";
            
            jdbc.rs = jdbc.stmt.executeQuery(query);
            
            
            ResultSetMetaData metaData = jdbc.rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            
            
            Vector<Vector<Object>> data = new Vector<>();
            while (jdbc.rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(jdbc.rs.getObject(i));
                }
                data.add(row);
            }
            
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2) { // did, uid, oid columns
                        return Integer.class;
                    }
                    return super.getColumnClass(columnIndex);
                }
            };
            
            organTable.setModel(model);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading organ data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateSelectedOrgans(String newStatus) {
        int[] selectedRows = organTable.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one organ to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            for (int row : selectedRows) {
                int did = (Integer) organTable.getValueAt(row, 0); // did is in first column
                String updateQuery = "UPDATE donation SET status = '" + newStatus + "' WHERE did = " + did;
                jdbc.stmt.executeUpdate(updateQuery);
            }
            
            JOptionPane.showMessageDialog(this, "Successfully updated " + selectedRows.length + " records.",
                    "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            
            
            loadOrganData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating organ status: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
