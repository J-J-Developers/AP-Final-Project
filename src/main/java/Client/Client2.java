package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {
    // آدرس سرور چت
    private static final String SERVER_ADDRESS = "127.0.0.1";
    // پورتی که سرور چت بر روی آن گوش می‌دهد
    private static final int SERVER_PORT = 1234;

    private String name;
    private String id;

    public Client2(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static void main(String[] args) throws Exception {
        new Client(" " , " ").startClient();
    }

    // متد startClient برای شروع اتصال به سرور چت
    public void startClient() throws Exception {
        // ایجاد یک سوکت برای اتصال به سرور
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        // برای خواندن پیام‌های دریافتی از سرور
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // برای نوشتن پیام‌ها به سرور
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // برای خواندن ورودی از کاربر
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connected to chat server");

        // ایجاد یک رشته جدید برای خواندن ورودی کاربر و ارسال آن به سرور
        new Thread(() -> {
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
            }
        }).start();

        // خواندن پیام‌های دریافتی از سرور و چاپ آن‌ها
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println(message);
        }
    }
}

