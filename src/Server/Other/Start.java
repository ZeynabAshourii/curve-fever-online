package src.Server.Other;

import src.Server.TCP.Server;

public class Start {
    public static void main(String[] args) {
        Server server = new Server(5000);
        server.ListenClientConnectionRequests();
    }
}
