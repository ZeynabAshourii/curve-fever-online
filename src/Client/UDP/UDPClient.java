package src.Client.UDP;

import src.Client.UDP.View.GamePanel;
import src.GameLogic.Controller.Player;
import src.GameLogic.Model.Gift.PowerUp;
import src.Message.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.LinkedList;
public class UDPClient extends Thread{
    private int rightKey;
    private int leftKey;
    private boolean end = false;
    private String username;
    private GamePanel gamePanel;
    private DatagramSocket datagramSocket;
    public UDPClient( String name , int rightKey , int leftKey){
        this.username = name;
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        start();
    }
    @Override
    public void run() {
        try {
            while (!end) {
                byte[] receiveDate = new byte[2048];

                DatagramPacket receivePacket = new DatagramPacket(receiveDate, receiveDate.length);
                datagramSocket.receive(receivePacket);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveDate);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                if(object instanceof Player){
                    gamePanel.setPlayer((Player) object);
                } else if (object instanceof Message) {
                    Message message = (Message) object;
                    if(message.getType() == Message.MessageTypes.CLEAR){
                        gamePanel.clear();
                    } else if (message.getType() == Message.MessageTypes.END) {
                        gamePanel.stop();
                        end = true;
                    }
                } else if (object instanceof LinkedList) {
                    LinkedList<PowerUp> powerUp = (LinkedList<PowerUp>) object;
                    gamePanel.setPowerUp(powerUp);
                }else if(object instanceof String){
                    gamePanel.setClearPage(true);
                }
            }
            datagramSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void Send(byte[] sendData) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData , sendData.length , InetAddress.getLocalHost() , 9000);
            datagramSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getRightKey() {
        return rightKey;
    }
    public int getLeftKey() {
        return leftKey;
    }
    public String getUsername() {
        return username;
    }
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
