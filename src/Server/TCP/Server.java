package src.Server.TCP;

import src.Message.MyString;
import src.Server.Other.ServerFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
public class Server {
    private int port;
    private ServerSocket socket;
    private ListenConnectionRequestThread listenConnectionRequestThread;
    private static ArrayList<ServerSideClient> clients;
    private ServerFrame serverGUI;
    private HashMap<MyString, Boolean> allClients = new HashMap<MyString , Boolean>();
    public Server(int port) {
        try {
            this.port = port;
            this.socket = new ServerSocket(this.port);
            this.listenConnectionRequestThread = new ListenConnectionRequestThread(this);
            this.clients = new ArrayList<ServerSideClient>();
            this.serverGUI = new ServerFrame();
        } catch (IOException ex) {
            System.out.println("There is an error occured when opening the Server on port:" + this.port);
        }
    }

    // starts the acceptance
    public void ListenClientConnectionRequests() {
        this.listenConnectionRequestThread.start();
    }

    public ServerSocket getSocket() {
        return socket;
    }
    public static ArrayList<ServerSideClient> getClients() {
        return clients;
    }
    public ServerFrame getServerGUI() {
        return serverGUI;
    }

    public HashMap<MyString, Boolean> getAllClients() {
        return allClients;
    }

    public void setAllClients(HashMap<MyString, Boolean> allClients) {
        this.allClients = allClients;
    }
}

