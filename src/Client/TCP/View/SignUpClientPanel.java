package src.Client.TCP.View;

import src.Client.Other.ClientFrame;
import src.Client.TCP.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpClientPanel extends JPanel implements ActionListener {
    private TextField textFieldUsername;
    private JButton buttonSaveUsername;
    private boolean saveUsername = false;
    private String username;
    private JButton start;
    private ClientFrame clientFrame;
    public SignUpClientPanel(ClientFrame clientFrame) {
        this.clientFrame = clientFrame;

        this.setSize(1080, 771);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();

        textFieldUsername = new TextField("username");
        this.add(textFieldUsername);
        textFieldUsername.setBounds(370, 200, 500, 60);
        buttonSaveUsername = new JButton("save");
        this.add(buttonSaveUsername);
        buttonSaveUsername.setBounds(200, 200, 100, 60);
        buttonSaveUsername.addActionListener(this);

        start = new JButton(" Start !");
        this.add(start);
        start.setBounds(460 , 590 , 160 , 80);
        start.addActionListener(this);

    }
        @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(buttonSaveUsername)){
            username = textFieldUsername.getText();
            saveUsername = true;
        }
        else if(e.getSource().equals(start)){
            if(!saveUsername){
                JOptionPane.showMessageDialog(this, "click on save for username");
            }
            else {
                register();
            }
        }
    }
    public void register(){
        Client client = new Client(username , clientFrame);
        client.Connect("127.0.0.1", 5000);
        clientFrame.setClient(client);
        if (client.socket == null) {
            JOptionPane.showMessageDialog(null, "There is no server");
            System.exit(0);
        }else {
            clientFrame.setContentPane(new GameRequestPanel(client));
        }
    }
}
