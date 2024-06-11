package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable{

    private Socket socket ;
    private BufferedReader input ;
    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket ;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

    }

    public void run() {
        try {
            while (true){
                String serverResponse = input.readLine() ;
                if (serverResponse == null) break;
                System.out.println(serverResponse);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
