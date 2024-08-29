package src.Client.TCP;

import src.Client.Other.ClientFrame;
import src.Client.TCP.View.GameRequestPanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private int id;
    private static int lastId = 0;
    public int serverPort;
    private boolean ready = true;
    private String username;
    public String serverIP;
    public Socket socket;
    public ObjectInputStream sInput;
    public ObjectOutputStream sOutput;
    private GameRequestPanel gameRequestPanel;
    public ClientListenThread listenThread;
    private ClientFrame clientFrame;
    public Client(String name , ClientFrame clientFrame){
        this.username = name;
        this.clientFrame = clientFrame;
        ready = true;
    }
    public void Connect(String serverIP, int port) {
        try {
            this.serverIP = serverIP;
            this.serverPort = port;
            this.socket = new Socket(this.serverIP, this.serverPort);
            sOutput = new ObjectOutputStream(this.socket.getOutputStream());
            sInput = new ObjectInputStream(this.socket.getInputStream());
            listenThread = new ClientListenThread(this);
            this.listenThread.start();
            Send(username);
            id = lastId;
            lastId++;
        } catch (IOException ex) {
            System.out.println("Can not connected to the Server.");
        }
    }
    public void Send(Object message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public String getUsername() {
        return username;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    public int getId() {
        return id;
    }
    public GameRequestPanel getGameRequestPanel() {
        return gameRequestPanel;
    }
    public void setGameRequestPanel(GameRequestPanel gameRequestPanel) {
        this.gameRequestPanel = gameRequestPanel;
    }
    public ClientFrame getClientFrame() {
        return clientFrame;
    }
}
