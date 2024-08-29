package src.Client.TCP.View;

import src.Client.TCP.Client;
import src.Message.MyString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
public class GameRequestPanel extends JPanel implements ActionListener {
    private Client client;
    private HashMap<MyString, Boolean> clients = new HashMap<>();
    private LinkedList<MyButton> buttons = new LinkedList<>();
    public GameRequestPanel(Client client){
        this.client = client;
        client.setGameRequestPanel(this);
        this.setSize(1080, 771);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font("Courier", Font.BOLD, 16));
        g.drawString("Client name : " , 900 , 80);
        g.drawString(client.getUsername(), 920 , 120 );
    }

    public void setDate(HashMap<MyString, Boolean> clients){
        clearButtons();
        this.clients = clients;
        addButtons();
    }
    public void clearButtons(){
        for(int i = 0; i <buttons.size(); i++){
            this.remove(buttons.get(i));
        }
        buttons.clear();
    }
    public void addButtons(){
        for(Map.Entry m : clients.entrySet()){
            MyButton myButton = new MyButton((MyString) m.getKey(), (Boolean) m.getValue());
            this.add(myButton);
            myButton.setBounds( 10 , 10 + 100*myButton.getI() , 40*4 , 20*4);
            myButton.setFocusable(false);
            myButton.addActionListener(this);
            buttons.add(myButton);
        }
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MyButton myButton = (MyButton)actionEvent.getSource();
        Integer i = myButton.getI();
        Boolean isReady = clients.get(myButton.getMyName());
        if(i == client.getId()){
            JOptionPane.showMessageDialog(this, "you can't play with yourself");
        } else if (!isReady) {
           JOptionPane.showMessageDialog(this, "this user is not ready");
        } else {
            client.Send(myButton.getMyName());
        }
    }

}
