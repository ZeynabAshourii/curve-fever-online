package src.Client.TCP;

import src.Client.TCP.View.GameRequestPanel;
import src.Client.UDP.UDPClient;
import src.Client.UDP.View.GamePanel;
import src.Message.Message;
import src.Message.MyString;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
public class ClientListenThread extends Thread {
    private Client client;
    public ClientListenThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!this.client.socket.isClosed()) {
            try {
                Object object = client.sInput.readObject();
                if(object instanceof HashMap){
                    HashMap<MyString, Boolean> hashMap = (HashMap<MyString , Boolean>) object;
                    client.getGameRequestPanel().setDate(hashMap);
                } else if (object instanceof MyString) {
                    receiveMyString((MyString) object);
                } else if (object instanceof Message) {
                    receiveMessage((Message) object);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void receiveMyString(MyString myString){
        int result = JOptionPane.showConfirmDialog(client.getGameRequestPanel() , "do you want to play game with :" + myString.username);
        switch (result){
            case JOptionPane.YES_OPTION -> client.Send(new Message(Message.MessageTypes.YES , myString));
            case JOptionPane.NO_OPTION -> client.Send(new Message(Message.MessageTypes.NO , myString));
        }
    }
    public void receiveMessage(Message message){
        if (message.getType() == Message.MessageTypes.NO) {
            JOptionPane.showMessageDialog(client.getGameRequestPanel(), message.getOtherSideClient().username + " doesn't want to play with you");
        } else if (message.getType() == Message.MessageTypes.YES) {
            UDPClient udpClient = new UDPClient(client.getUsername() , 39 , 37);
            GamePanel gamePanel = new GamePanel(udpClient);
            client.getClientFrame().setContentPane(gamePanel);
            client.setReady(false);

        } else if (message.getType() == Message.MessageTypes.END) {
            client.setReady(true);
            client.getClientFrame().setContentPane(new GameRequestPanel(client));
            if (message.isEndType()) {
                client.Send(new Message(Message.MessageTypes.END));
            }
        }
    }
}
