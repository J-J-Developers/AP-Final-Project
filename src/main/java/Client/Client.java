//package Client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client {
//
//    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        Socket socket = new Socket("127.0.0.1", 5000);
//
//        ServerConnection serverConn = new ServerConnection(socket);
//
//        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//        try {
//            for (int i = 0; i < 4; i++) {
//                String turn = input.readLine();
//                System.out.println(turn);
//                for (int j = 0; j < 5; j++) {
//                    int number = Integer.parseInt(input.readLine());
//                    System.out.println(turn);
//                    if ((turn.equals("ruler") && i == number) || (turn.equals("secondNumber") && i == number) || (turn.equals("thirdNumber") && i == number) || (turn.equals("fourthNumber") && i == number)) {
//                        System.out.println(input.readLine());
//                        System.out.println(number);
//                        output.println(number);
//                    }
//                }
//            }
//        } finally {
//            input.close();
//            output.close();
//        }
//        socket.close();
//        System.exit(0);
//    }
//}






package Client;

import GamePlay.Swing;
import GamePlay.Token;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
static int x = 0 ;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("127.0.0.1", 5000);

        // فرض می‌کنیم که کلاس ServerConnection به درستی کار می‌کند
        ServerConnection serverConn = new ServerConnection(socket);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        new Thread(serverConn).start();
        try {
            System.out.println(input.readLine());
        }
        finally {
            input.close();
            output.close();
            socket.close();
        }
//        System.exit(0);
    }
}


