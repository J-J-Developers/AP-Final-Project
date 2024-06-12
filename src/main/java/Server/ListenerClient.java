//package Server;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
//
//public class ListenerClient implements Runnable{
//    private final Socket client;
//    private final BufferedReader reader;
//    private final PrintWriter writer;
//    private static final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("ruler" , "secondNumber" , "thirdNumber" , "fourthNumber")) ;
//    private static final ArrayList<String> arrayList1 = new ArrayList<>();
//
//    public ListenerClient(Socket socket, ArrayList<ListenerClient> clients) throws IOException {
//        this.client = socket;
//        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        this.writer = new PrintWriter(client.getOutputStream(), true);
//        generateUniqueList();
//    }
//
//    private void generateUniqueList() {
//        for (int i = 0; i < Server.arrayList1.size(); i++) {
//            for (int j = 0; j < Server.arrayList2.size(); j++) {
//                Server.arrayList3.add(Server.arrayList1.get(i) + " " + Server.arrayList2.get(j));
//            }
//        }
//    }
//
//
//    @Override
//    public void run() {
//        Random random = new Random() ;
//        for (int i =0 ; i < 4 ; i++) {
//            try {
//                    Server.outToAll(arrayList.get(i));
//
//                        String input = reader.readLine();
//                        if (input.equals(String.valueOf(i+1))) {
//                            for (int j = 0 ; j< 5 ; j++) {
//                                Server.outToAll(String.valueOf(i+1));
//                                int turn = random.nextInt(Server.arrayList3.size()- j);
//                                Server.outToAll(arrayList.get(turn));
//                                Server.arrayList3.remove(turn);
//                                Server.outToAll(String.valueOf(i + 1));
//                                try {
//                                    Thread.sleep(500);
//                                } catch (InterruptedException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//
//                        }
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//
//    }
//
//
//    public PrintWriter getWriter() {
//        return writer;
//    }
//}


//
//package Server;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
//public class ListenerClient implements Runnable {
//    private final Socket client;
//    private final BufferedReader reader;
//    private final PrintWriter writer;
//    private static final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("ruler", "secondNumber", "thirdNumber", "fourthNumber"));
//    private static final ArrayList<String> uniqueList = new ArrayList<>();
//    int turn = 0 ;
//    int x = 0 ;
//
//    public ListenerClient(Socket socket , ArrayList<ListenerClient> clients) throws IOException {
//        this.client = socket;
//        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        this.writer = new PrintWriter(client.getOutputStream(), true);
//        generateUniqueList();
//    }
//
//    private void generateUniqueList() {
//        for (String number : Server.arrayList1) {
//            for (String suit : Server.arrayList2) {
//                uniqueList.add(number + " " + suit);
//            }
//        }
//    }
//
//    public PrintWriter getWriter() {
//        return writer;
//    }
//
//    @Override
//    public void run() {
//        Random random = new Random();
//
//        try {
//                if (x == 0){
//                Server.outToAll(arrayList.get(turn));
//                turn++ ;
//                }
//                    int index = random.nextInt(uniqueList.size());
//                    String card = uniqueList.get(index);
//                    Server.outToAll(card);
//                    uniqueList.remove(index);
//                x++ ;
//        } finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


//package Server;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
//public class ListenerClient implements Runnable {
//    private final Socket client;
//    private final PrintWriter writer;
//    private static final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("ruler", "secondNumber", "thirdNumber", "fourthNumber"));
//    private static final ArrayList<String> uniqueList = new ArrayList<>();
//    private int turn = 0;
//    private int x = 0;
//
//    public ListenerClient(Socket socket, ArrayList<ListenerClient> clients) throws IOException {
//        this.client = socket;
//        this.writer = new PrintWriter(client.getOutputStream(), true);
//        synchronized (uniqueList) {
//            if (uniqueList.isEmpty()) {
//                generateUniqueList();
//            }
//        }
//    }
//
//    private void generateUniqueList() {
//        // فرض می‌کنیم که Server.arrayList1 و Server.arrayList2 به درستی پر شده‌اند
//        for (String number : Server.arrayList1) {
//            for (String suit : Server.arrayList2) {
//                uniqueList.add(number + " " + suit);
//            }
//        }
//    }
//
//    public PrintWriter getWriter() {
//        return writer;
//    }
//
//    @Override
//    public void run() {
//        Random random = new Random();
//        try {
//            if (x == 0) {
//                Server.outToAll(arrayList.get(turn));
//                turn++;
//                Thread.sleep(500); // اضافه کردن وقفه بین ارسال turn و ارسال کارت‌ها
//            }
//            for (int i = 0; i < 5; i++) { // ارسال 5 کارت با وقفه 0.5 ثانیه‌ای
//                int index = random.nextInt(uniqueList.size());
//                String card = uniqueList.get(index);
//                Server.outToAll(card);
//                uniqueList.remove(index);
//                Thread.sleep(500);
//            }
//            x++;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ListenerClient implements Runnable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private static final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("ruler", "secondNumber", "thirdNumber", "fourthNumber"));
    private static final ArrayList<String> uniqueList = new ArrayList<>();

    private static final Object lock = new Object();

    public ListenerClient(Socket socket, ArrayList<ListenerClient> clients) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void run() {
        try {
            String text;
            // حلقه while برای اطمینان از اینکه داده‌ای برای خواندن وجود دارد
            while ((text = reader.readLine()) != null) {
                // اکنون می‌توانید با اطمینان text را چاپ کنید
                System.out.println(text);
                // ارسال پیام به همه کلاینت‌ها
                Server.outToAll(text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}