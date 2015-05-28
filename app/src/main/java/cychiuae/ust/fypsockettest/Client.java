package cychiuae.ust.fypsockettest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by yinyinchiu on 29/5/15.
 */
public class Client implements Serializable {

    public static Client sClient;

    private String address;
    private int port;
    private Socket socket;
    private Thread send;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
        sClient = this;
    }

    public void connect() {
        try {
            socket = new Socket(address, port);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public boolean isConnect() {
        return socket.isConnected();
    }

    public void send(final String msg) {
        send = new Thread() {
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf8")), true);
                    out.println(msg);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }
}
