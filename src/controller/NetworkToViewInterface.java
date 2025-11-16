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

    /** response to GAME_START */
    void startGame(String oppNick, boolean turn);

    void gameState(int myScore, int oppScore, int turnScore, int roll, boolean turn);

    void login(String username);

    void showGameWon(String message);
    void showGameLost(String message);
}
