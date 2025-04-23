package organix;

import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.*;

public class OrganCard extends javax.swing.JFrame {

    private int uid;
    private JDBC jdbc;

    public OrganCard(int _uid) {
        initComponents();
        this.uid = _uid;

        try {
            jdbc = new JDBC();
            jdbc.rs = jdbc.stmt.executeQuery("SELECT donation.did, organ.oid, organ.organ_name, donation.status FROM donation INNER JOIN organ ON donation.oid = organ.oid AND donation.uid=" + this.uid);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (jdbc.rs.next()) {
                int did = jdbc.rs.getInt("did");
                int oid = jdbc.rs.getInt("oid");
                String organName = jdbc.rs.getString("organ_name");
                String status = jdbc.rs.getString("status");

                model.addRow(new Object[]{did, oid, organName, status});
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButtonBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Organ Card");

        jButtonBack.setText("Back");
        jButtonBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonBackMouseClicked(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Donor Id", "Organ Id", "Organ Name", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonDelete.setText("Delete Selected Row");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedRow();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonBack))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jButtonDelete)))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButtonBack)
                        .addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButtonDelete)
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButtonBackMouseClicked(java.awt.event.MouseEvent evt) {
        this.setVisible(false);
        DonorProfile dp = new DonorProfile(this.uid);
        dp.setVisible(true);
    }

    private void deleteSelectedRow() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            int did = (int) jTable1.getValueAt(selectedRow, 0); // Donor ID is in column 0
            try {
                jdbc.stmt.executeUpdate("DELETE FROM donation WHERE did = " + did);
               
                ((DefaultTableModel) jTable1.getModel()).removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Row deleted successfully.");
                
                this.jdbc.rs = this.jdbc.stmt.executeQuery("SELECT COUNT(*) as count FROM donation WHERE uid="+uid);
                this.jdbc.rs.next();
                if(this.jdbc.rs.getInt("count") == 0){
                    this.jdbc.stmt.executeUpdate("DELETE FROM user WHERE uid=" + uid);
                    Home home = new Home();
                    home.setVisible(true);
                    this.setVisible(false);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting row.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // Example: new OrganCard(1).setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}

