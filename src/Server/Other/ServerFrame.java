package src.Server.Other;

import src.Server.TCP.Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServerFrame extends JFrame {
    private final String[] columnNames = {"Username" , "Is ready?"};
    private final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    private JTable table;
    public ServerFrame(){
        this.setSize(1080 , 771);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        table = new JTable(tableModel);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        this.add(scrollPane);

        this.setVisible(true);
    }
    public void retrieveData() {
        tableModel.setRowCount(0);
        for (int i = 0; i < Server.getClients().size(); i++) {
            Object[] rowData = new Object[columnNames.length];
            rowData[0] = Server.getClients().get(i).getUsername();
            rowData[1] = Server.getClients().get(i).isReady();
            tableModel.addRow(rowData);
        }
    }
}
