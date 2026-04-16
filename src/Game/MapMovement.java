package Game;

import java.util.Scanner;
import Rooms.Room;

public class MapMovement {

    public static int Move(Scanner s, Room[] rooms, int currRoom) {
        
        TextFormatter.printPrompt("Map move [W/A/S/D] >");
        String input = s.next().toLowerCase();

        while (true) {
            
            boolean valid = false;

            if (input.equals("d")) {

                for (String i: rooms[currRoom].getConnectedRooms()) {
                    if (i.equals("room_" + (currRoom + 1))) {
                        valid = true;
                    }
                }

                if (currRoom % 5 != 4 && valid) {
                    currRoom = currRoom + 1;
                    rooms[currRoom].setUnlocked(true);
                    rooms[currRoom].setVisited(true);
                    break;
                }
            }

            if (input.equals("a")) {

                for (String i: rooms[currRoom].getConnectedRooms()) {
                    if (i.equals("room_" + (currRoom - 1))) {
                        valid = true;
                    }
                }

                if (currRoom % 5 != 0 && valid) {
                    currRoom = currRoom - 1;
                    rooms[currRoom].setUnlocked(true);
                    rooms[currRoom].setVisited(true);
                    break;
                }
            }

            if (input.equals("s")) {

                for (String i: rooms[currRoom].getConnectedRooms()) {
                    if (i.equals("room_" + (currRoom + 5))) {
                        valid = true;
                    }
                }

                if (currRoom / 5 != 4 && valid) {
                    currRoom = currRoom + 5;
                    rooms[currRoom].setUnlocked(true);
                    rooms[currRoom].setVisited(true);
                    break;
                }
            }

            if (input.equals("w")) {

                for (String i: rooms[currRoom].getConnectedRooms()) {
                    if (i.equals("room_" + (currRoom - 5))) {
                        valid = true;
                    }
                }

                if (currRoom / 5 != 0 && valid) {
                    currRoom = currRoom - 5;
                    rooms[currRoom].setUnlocked(true);
                    rooms[currRoom].setVisited(true);
                    break;
                }
            }
                            
            TextFormatter.printWarning("Invalid move. Pick a connected room.");
            TextFormatter.printPrompt("Map move [W/A/S/D] >");
            input = s.next().toLowerCase();

            }

        return currRoom;
    }
}
