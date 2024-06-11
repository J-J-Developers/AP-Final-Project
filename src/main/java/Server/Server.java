package Server;

import GamePlay.Game;
import GamePlay.Swing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final ArrayList<ClientHandel>clients= new ArrayList<>() ;
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public ArrayList<Game> allGames = new ArrayList<>();


    public static void main(String[]args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        while (true) {
            Socket client = server.accept();
            new Swing() ;
            ClientHandel clientThread = new ClientHandel(Swing.text , Swing.text2 ,  client, clients);
            clients.add(clientThread);
            pool.execute(clientThread);
        }
    }

    public static synchronized void outToAll(String message) {
        for (ClientHandel client : clients) {
            client.getWriter().println(message);
        }
    }
}


