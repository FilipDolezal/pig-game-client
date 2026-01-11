package net;

import net.msg.ClientMessage;
import net.msg.MsgPing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final long PING_INTERVAL = 8000; // 8 seconds, safely under 20s timeout

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    private volatile long lastSentTime;
    private Thread heartbeatThread;
    private volatile boolean running;
    private Runnable onPingSent;

    public void setOnPingSent(Runnable callback) {
        this.onPingSent = callback;
    }

    public void connect(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    public void sendMessage(ClientMessage msg) {
        System.out.println(msg);
        out.println(msg);
        lastSentTime = System.currentTimeMillis();
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void startHeartbeat() {
        running = true;
        lastSentTime = System.currentTimeMillis();
        heartbeatThread = new Thread(this::heartbeatLoop);
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }

    public void stopHeartbeat() {
        running = false;
        if (heartbeatThread != null) {
            heartbeatThread.interrupt();
            heartbeatThread = null;
        }
    }

    private void heartbeatLoop() {
        while (running) {
            try {
                Thread.sleep(1000); // Check every second
                if (System.currentTimeMillis() - lastSentTime > PING_INTERVAL) {
                    sendMessage(new MsgPing());
                    if (onPingSent != null) {
                        onPingSent.run();
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void close() throws IOException {
        stopHeartbeat();
        in.close();
        out.close();
        sock.close();
    }
}
