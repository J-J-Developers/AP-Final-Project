package Server;

import GamePlay.Game;
import GamePlay.Swing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<ListenerClient>clients= new ArrayList<>() ;
    public ArrayList<Game> allGames = new ArrayList<>();


    public static void main(String[]args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        while (true) {
            Socket client = server.accept();
            new Swing() ;
            ListenerClient clientThread = new ListenerClient(client, clients);
            clients.add(clientThread);
        }
    }

    public static synchronized void outToAll(String message) throws IOException {
        for (ListenerClient client : clients) {
            client.getWriter().write(message);
        }
    }
}


