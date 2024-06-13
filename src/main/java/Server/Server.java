package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 1234;
    private static Set<PrintWriter> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ServerSocket listener = new ServerSocket(PORT);

        try {
            // حلقه بی‌پایان برای پذیرش اتصالات جدید
            while (true) {
                // برای هر اتصال جدید، یک نخ Handler جدید ایجاد و راه‌اندازی می‌شود
                new Handler(listener.accept()).start();
            }
        } finally {
            // بستن سوکت سرور در صورت خروج از حلقه
            listener.close();
        }
    }

    // کلاس داخلی Handler که برای مدیریت اتصالات کلاینت‌ها استفاده می‌شود
    private static class Handler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        // سازنده کلاس Handler که یک سوکت کلاینت را می‌گیرد
        public Handler(Socket socket) {
            this.socket = socket;
        }

        // متد run که کارهای مربوط به هر کلاینت را انجام می‌دهد
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // اضافه کردن خروجی کلاینت به مجموعه کلاینت‌ها
                clients.add(out);

                // حلقه بی‌پایان برای خواندن و ارسال پیام‌ها
                while (true) {
                    // خواندن یک خط پیام از کلاینت
                    String input = in.readLine();
                    // اگر پیامی وجود نداشت، اتصال را قطع کن
                    if (input == null) {
                        return;
                    }
                    // ارسال پیام دریافتی به تمام کلاینت‌های متصل
                    for (PrintWriter writer : clients) {
                        writer.println(input);
                    }
                }
            } catch (IOException e) {
                // چاپ خطا در صورت وجود مشکل
                System.out.println(e);
            } finally {
                // در صورت قطع اتصال، خروجی کلاینت را از مجموعه حذف کن
                if (out != null) {
                    clients.remove(out);
                }
                // سوکت کلاینت را ببند
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

