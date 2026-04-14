package Rooms;

public class RoomDisplay {

    private static final String START_ROOM_COLOR = "\u001B[36m";
    private static final String BATTLE_ROOM_COLOR = "\u001B[31m";
    private static final String ITEM_ROOM_COLOR = "\u001B[32m";
    private static final String BOSS_ROOM_COLOR = "\u001B[33m";
    private static final String FINAL_BOSS_ROOM_COLOR = "\u001B[35m";
    private static final String PATH_COLOR = "\u001B[92m";
    private static final String UNDERLINE = "\u001B[4m";
    private static final String RESET = "\u001B[0m";

    public static void displayRooms(Room[] rooms) {
        displayRooms(rooms, -1);
    }

    public static void displayRooms(Room[] rooms, int currentRoom) {

        boolean[]vertical = new boolean[5];
        boolean[]highlightVertical = new boolean[5];

        for (int i = 0; i < rooms.length; i++) {
            boolean roomVisible = isVisible(rooms, i);

            if (i%5 == 0 && i != 0) {
                String v = "";

                System.out.println();
                
                for (int j = 0; j < vertical.length; j++) {
                    if (vertical[j]) {
                        v = v + colorPath(" ||    ", highlightVertical[j]);
                    } else {
                        v = v + "       ";
                    }
                }

                System.out.println(v);
                System.out.println(v);

                vertical[0] = false;
                vertical[1] = false;
                vertical[2] = false;
                vertical[3] = false;
                vertical[4] = false;
                highlightVertical[0] = false;
                highlightVertical[1] = false;
                highlightVertical[2] = false;
                highlightVertical[3] = false;
                highlightVertical[4] = false;

            }

            String rmDisplay = "";

            if (rooms[i].getType() == RoomType.START) {
                rmDisplay = rmDisplay + "ST";
            }
             
            if (rooms[i].getType() == RoomType.BATTLE) {
                rmDisplay = rmDisplay + "BA";
            }

            if (rooms[i].getType() == RoomType.ITEM) {
                rmDisplay = rmDisplay + "IT";
            }

            if (rooms[i].getType() == RoomType.BOSS) {
                rmDisplay = rmDisplay + "BO";
            }

            if (rooms[i].getType() == RoomType.FINAL_BOSS) {
                rmDisplay = rmDisplay + "FB";
            }

            if (roomVisible) {
                System.out.print("[" + formatRoomText(rmDisplay, rooms[i], i, currentRoom) + "]");
            } else {
                System.out.print("[  ]");
            }

            if (rooms[i].getConnectedRooms().length > 0) {
                boolean none = true;
                for (String j: rooms[i].getConnectedRooms()) {
                    int connectedRoom = Integer.parseInt(j.substring(5));
                    boolean connectedRoomVisible = isVisible(rooms, connectedRoom);

                    if (connectedRoom == i + 5 && roomVisible && connectedRoomVisible) {
                        vertical[i%5] = true;
                        highlightVertical[i%5] = currentRoom == i || currentRoom == connectedRoom;
                    }

                    if (connectedRoom == i+1 && roomVisible && connectedRoomVisible) {
                        System.out.print(colorPath("===", currentRoom == i || currentRoom == connectedRoom));
                        none = false;
                    }
                }

                if (none){
                    System.out.print("   ");
                }
            }

        }

        System.out.println();

    }

    public static String formatNum(int num) {
        return formatNum(num, -1, null);
    }

    public static String formatNum(int num, int currentRoom) {
        return formatNum(num, currentRoom, null);
    }

    public static String formatNum(int num, int currentRoom, Room[] rooms) {
        String formattedNum;
        String roomColor;

        if (num < 10) {
            formattedNum = "0" + Integer.toString(num);
        } else {
            formattedNum = Integer.toString(num);
        }

        if (rooms != null && num >= 0 && num < rooms.length) {
            roomColor = getRoomColor(rooms[num]);
        } else {
            roomColor = BATTLE_ROOM_COLOR;
        }

        if (num == currentRoom) {
            return roomColor + UNDERLINE + formattedNum + RESET;
        }

        return roomColor + formattedNum + RESET;
    }

    private static String formatRoomText(String roomText, Room room, int roomIndex, int currentRoom) {
        String roomColor = getRoomColor(room);

        if (roomIndex == currentRoom) {
            return roomColor + UNDERLINE + roomText + RESET;
        }

        return roomColor + roomText + RESET;
    }

    private static String getRoomColor(Room room) {
        if (room.getType() == RoomType.START) {
            return START_ROOM_COLOR;
        }

        if (room.getType() == RoomType.BATTLE) {
            return BATTLE_ROOM_COLOR;
        }

        if (room.getType() == RoomType.ITEM) {
            return ITEM_ROOM_COLOR;
        }

        if (room.getType() == RoomType.BOSS) {
            return BOSS_ROOM_COLOR;
        }

        return FINAL_BOSS_ROOM_COLOR;
    }

    private static boolean isVisible(Room[] rooms, int roomIndex) {
        if (rooms[roomIndex].isVisited()) {
            return true;
        }

        for (String connectedRoom : rooms[roomIndex].getConnectedRooms()) {
            int connectedRoomIndex = Integer.parseInt(connectedRoom.substring(5));

            if (rooms[connectedRoomIndex].isVisited()) {
                return true;
            }
        }

        return false;
    }

    private static String colorPath(String text, boolean highlight) {
        if (highlight) {
            return PATH_COLOR + text + RESET;
        }

        return text;
    }
}
