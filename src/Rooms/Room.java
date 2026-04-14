package Rooms;
import Bot.BotProfile;
import Bot.Item;

public class Room {

    private String id;
    private RoomType type;
    private String[] connectedRooms;
    private Item[] items;
    private BotProfile botProfile;
    private boolean unlocked;
    private boolean visited;
    private boolean clear;

    public Room(String id, String[] connectedRooms, Item[] items) {

        this(id, RoomType.BATTLE, connectedRooms, items);
    }

    public Room(String id, RoomType type, String[] connectedRooms, Item[] items) {

        this(id, type, connectedRooms, items, null);
    }

    public Room(String id, RoomType type, String[] connectedRooms, Item[] items, BotProfile botProfile) {

        this.id = id;
        this.type = type;
        this.connectedRooms = connectedRooms;
        this.items = items;
        this.botProfile = botProfile;
        this.unlocked = false;
        this.visited = false;
        this.clear = false;

    }

    public String getRoomId() {
        return this.id;
    }

    public String[] getConnectedRooms() {
        return this.connectedRooms;
    }

    public Item[] getItems() {
        return this.items;
    }

    public BotProfile getBotProfile() {
        return this.botProfile;
    }

    public String getTeamNumber() {
        if (this.botProfile == null) {
            return "";
        }

        return this.botProfile.getTeamNumber();
    }

    public String getTeamName() {
        if (this.botProfile == null) {
            return "";
        }

        return this.botProfile.getTeamName();
    }

    public RoomType getType() {
        return this.type;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isClear() {
        return this.clear;
    }

    public void setCleared(boolean clear) {
        this.clear = clear;
    }
}
