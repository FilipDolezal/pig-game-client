package controller;

public interface ViewToNetworkInterface {
    void connect(String ip, int port, String nickname);
    void sendListRooms();
    void sendJoinRoom(int roomId);
    void sendQuitGame();
    void sendLeaveRoom();
    void sendRoll();
    void sendHold();
    void sendExit();
    void sendResume();
}
