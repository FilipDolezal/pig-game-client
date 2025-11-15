package model.room;

public class GameRoom {
    private final int id;
    private int count;
    private GameRoomStatus status;

    public GameRoom(int id, int count, GameRoomStatus status) {
        this.id = id;
        this.count = count;
        this.status = status;
    }

    public void updateStatus(GameRoomStatus newStatus) {
        this.status = newStatus;
    }

    public void updateCount(int newCount) {
        this.count = newCount;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public GameRoomStatus getStatus() {
        return status;
    }
}
