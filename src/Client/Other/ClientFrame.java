package src.Client.Other;

import src.Client.TCP.Client;
import src.Client.TCP.View.SignUpClientPanel;

import javax.swing.*;
import java.awt.event.*;
public class ClientFrame extends JFrame implements ActionListener {
    private SignUpClientPanel signUpClientPanel;
    private Client client;
    public ClientFrame() {
        this.setSize(1080, 771);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(client != null) {
                    client.Send("endType");
                }else {
                    System.exit(0);
                }
            }
        };
        this.addWindowListener(windowListener);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        signUpClientPanel = new SignUpClientPanel(this);
        this.setContentPane(signUpClientPanel);
        this.setVisible(true);
    }
        @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void setClient(Client client) {
        this.client = client;
    }
}
