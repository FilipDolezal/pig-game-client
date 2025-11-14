package controller;

import model.room.GameRoomStatus;

public interface NetworkToViewInterface {
    void showErrorMessage(String title, String message);

    /** response to connection */
    void initializeGameRooms(int maxRooms);

    /** a single-room update response to LIST_ROOMS */
    void syncGameRoom(int id, int count, GameRoomStatus status);

    /** response to JOIN_ROOM */
    void joinGameRoom(int id);
}
