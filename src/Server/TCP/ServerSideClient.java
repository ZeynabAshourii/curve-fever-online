package src.Server.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ServerSideClient {
    private boolean ready;
    private String username;
    private Socket socket;
    private ObjectInputStream cInput;
    private ObjectOutputStream cOutput;
    private ClientListenThread clientListenThread;
    private Server server;
    public ServerSideClient(Socket socket , Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.cOutput = new ObjectOutputStream(this.socket.getOutputStream());
            this.cInput = new ObjectInputStream(this.socket.getInputStream());
            this.clientListenThread = new ClientListenThread(this);
            this.ready = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void Send(Object msg)
    {
        try {
            this.cOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(ServerSideClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Listen() {
        this.clientListenThread.start();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getcInput() {
        return cInput;
    }
}
