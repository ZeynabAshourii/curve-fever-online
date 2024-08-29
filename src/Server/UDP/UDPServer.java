package src.Server.UDP;

import src.GameLogic.Controller.Handler;
import src.Message.Message;
import src.Server.TCP.Server;
import src.Server.TCP.ServerSideClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {
    private int port;
    private boolean end = false;
    private Handler handler;
    private DatagramSocket datagramSocket;
    private int send = 0;
    public UDPServer(int port, Handler handler) {
        this.port = port;
        this.handler = handler;
        init();
        start();
    }
    public void init(){
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            while (!end) {
                byte[] receiveDate = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveDate, receiveDate.length);
                datagramSocket.receive(receivePacket);
                String string = new String((receiveDate));
                if (handler.isEnd()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(new Message(Message.MessageTypes.END));
                    byte[] data = byteArrayOutputStream.toByteArray();
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                    datagramSocket.send(datagramPacket);
                    end = true;
                } else if (string.contains(" turning is : ")) {
                    int startIndex = getSubString(string, " turning is : ");
                    String name = string.substring(0, startIndex);
                    handler.update(name, string.charAt(startIndex + 14));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    if (handler.isClear()) {
                        objectOutputStream.writeObject(new Message(Message.MessageTypes.CLEAR));
                    } else if(handler.isHitClearPowerUp()){
                        objectOutputStream.writeObject("clearPowerUp");
                        send++;
                        if(send %2 == 0) {
                            handler.setHitClearPowerUp(false);
                        }
                    }else {
                        if (handler.getPlayer1().getName().equals(name)) {
                            objectOutputStream.writeObject(handler.getPlayer1());
                        } else {
                            objectOutputStream.writeObject(handler.getPlayer2());
                        }
                    }
                    byte[] data = byteArrayOutputStream.toByteArray();
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                    datagramSocket.send(datagramPacket);

                } else if (string.contains("send gifts")) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(handler.getPowerUp());
                    byte[] data = byteArrayOutputStream.toByteArray();
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                    datagramSocket.send(datagramPacket);

                }
            }
            datagramSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(handler.isEnd()){
            endGame();
        }
    }
    public void endGame(){
        int id1 = getIdClient(handler.getPlayer1().getName());
        ServerSideClient client1 = Server.getClients().get(id1);
        int id2 = getIdClient(handler.getPlayer2().getName());
        ServerSideClient client2 = Server.getClients().get(id2);
        client1.setReady(true);
        client2.setReady(true);

        client1.Send(new Message(Message.MessageTypes.END , false));
        client2.Send(new Message(Message.MessageTypes.END , true));
    }
    public int getSubString(String orgString , String subString){
        int startIndex = 0;
        boolean contain = true;
        for(int i = 0 ; i <= orgString.length()-subString.length(); i++){
            for(int j = 0; j < subString.length(); j++){
                if(!(orgString.charAt(i+j) == subString.charAt(j))){
                    contain = false;
                    break;
                }
            }
            if(contain){
                startIndex = i;
            }
            contain = true;
        }
        return startIndex;
    }
    public int getIdClient(String string){
        int i = 0;
        for(int j = 0; j < Server.getClients().size(); j++){
            if(Server.getClients().get(j).getUsername().equals(string)){
                i = j;
                break;
            }
        }
        return i;
    }
}
