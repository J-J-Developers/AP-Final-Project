package Server;

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

    public static final ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10"));
    public static final ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList("del", "pike", "khesht", "gish"));
    public static final ArrayList<String> arrayList3 = new ArrayList<>();


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
