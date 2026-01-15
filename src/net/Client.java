package net;

import net.msg.ClientMessage;
import net.msg.MsgPing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP client with automatic heartbeat (PING) to stay alive.
 * Stores connection info for reconnection attempts.
 */
public class Client {
    private static final long PING_INTERVAL = 5000;
    private static final int RECONNECT_ATTEMPTS = 10;
    private static final long RECONNECT_DELAY_MS = 2000;

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    private volatile long lastSentTime;
    private Thread heartbeatThread;
    private volatile boolean running;
    private Runnable onPingSent;

    // Connection info for reconnection
    private String serverIp;
    private int serverPort;
    private String nickname;

    public void setOnPingSent(Runnable callback) {
        this.onPingSent = callback;
    }

    public void connect(String ip, int port) throws IOException {
        this.serverIp = ip;
        this.serverPort = port;
        sock = new Socket(ip, port);
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Attempts to reconnect to the server using stored connection info.
     * @return true if reconnection succeeded, false otherwise
     */
    public boolean reconnect() {
        if (serverIp == null || serverPort == 0) {
            return false;
        }

        // Close old connection if it exists
        try {
            if (sock != null && !sock.isClosed()) {
                sock.close();
            }
        } catch (IOException ignored) {}

        for (int attempt = 1; attempt <= RECONNECT_ATTEMPTS; attempt++) {
            try {
                System.out.println("Reconnection attempt " + attempt + "/" + RECONNECT_ATTEMPTS);
                sock = new Socket(serverIp, serverPort);
                out = new PrintWriter(sock.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                System.out.println("Reconnected successfully");
                return true;
            } catch (IOException e) {
                System.err.println("Reconnection attempt " + attempt + " failed: " + e.getMessage());
                if (attempt < RECONNECT_ATTEMPTS) {
                    try {
                        Thread.sleep(RECONNECT_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        return false;
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
