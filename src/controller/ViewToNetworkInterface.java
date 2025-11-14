package controller;

public interface ViewToNetworkInterface {
    boolean connect(String ip, int port, String nickname);
    void sendListRooms();
}
