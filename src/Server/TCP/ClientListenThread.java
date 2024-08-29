package src.Server.TCP;

import src.GameLogic.Controller.Handler;
import src.GameLogic.Controller.Player;
import src.Message.Message;
import src.Message.MyString;
import src.Server.UDP.UDPServer;

import java.io.IOException;
import java.util.HashMap;

public class ClientListenThread extends Thread {
    private boolean end = true;
    private ServerSideClient client;
    private Server server;
    public ClientListenThread(ServerSideClient client) {
        this.client = client;
        this.server = client.getServer();
    }

    @Override
    public void run() {
        while (!this.client.getSocket().isClosed() && end) {
            try {
                Object object = client.getcInput().readObject();
                if(object instanceof String) {
                    receiveString((String) object);
                }else if (object instanceof MyString) {
                    MyString myString = (MyString) object;
                    ServerSideClient client1 = Server.getClients().get(myString.getId());
                    client1.Send(new MyString(getIdClient(client) , client.getUsername()));
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
    public int getIdClient(ServerSideClient serverSideClient){
        int i = 0;
        for(int j = 0; j < Server.getClients().size(); j++){
            if(Server.getClients().get(j).getUsername().equals(serverSideClient.getUsername())){
                i = j;
                break;
            }
        }
        return i;
    }
    public void receiveString(String string){
        if(string.contains("endType")){
            Server.getClients().remove(client);
            end = false;
        }else {
            client.setUsername(string);
            revaluedDate();
        }
    }
    public void revaluedDate(){
        HashMap<MyString, Boolean> hashMap = new HashMap<>();
        for (int i = 0; i < Server.getClients().size(); i++) {
            Boolean ready = Server.getClients().get(i).isReady();
            hashMap.put(new MyString(i, Server.getClients().get(i).getUsername()), ready);
        }

        server.setAllClients(hashMap);
        client.getServer().getServerGUI().retrieveData();

        for (int i = 0; i < Server.getClients().size(); i++) {
            ServerSideClient serverSideClient = Server.getClients().get(i);
            serverSideClient.Send(server.getAllClients());
        }
    }
    public void receiveMessage(Message message){
        if(message.getType() == Message.MessageTypes.NO){
            ServerSideClient client1 = Server.getClients().get(message.getOtherSideClient().id);
            client1.Send(new Message(Message.MessageTypes.NO , new MyString(getIdClient(client) , client.getUsername())));
        } else if (message.getType() == Message.MessageTypes.YES) {
            startNewGame(message);
        } else if (message.getType() == Message.MessageTypes.END) {
            revaluedDate();
        }
    }
    public void startNewGame(Message message){
        Player player1 = new Player(message.getOtherSideClient().username);
        Player player2 = new Player(client.getUsername());
        player1.setPairedPlayer(player2);
        player2.setPairedPlayer(player1);
        Handler handler = new Handler(player1 , player2);
        UDPServer udpServer = new UDPServer(9000 , handler);
        ServerSideClient client1 = Server.getClients().get(message.getOtherSideClient().id);

        client1.setReady(false);
        client.setReady(false);

        revaluedDate();

        client1.Send(new Message(Message.MessageTypes.YES));
        client.Send(new Message(Message.MessageTypes.YES));
    }
}
