package controller;

import model.GameRoom.GameRoomStatus;

public interface NetworkToViewInterface {
    void initializeGameRooms(int maxRooms);
    void syncGameRoom(int id, int count, GameRoomStatus status);
    void showErrorMessage(String title, String message);
}
