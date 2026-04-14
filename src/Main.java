import Game.MapMovement;
import Game.Player;
import Rooms.Room;
import Rooms.RoomDisplay;
import Rooms.RoomGenerator;
import Rooms.RoomType;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        Player player = new Player();

        System.out.println("Welcome to the FRC Battle Simulator!");
        System.out.println("To begin, input a seed to start playing or use a random one: ");

        int seed = Integer.parseInt(s.next());

        Random random = new Random(seed);

        Room[] rooms;

        rooms = new RoomGenerator().generateRooms(random);
        // rooms = new RoomGenerator().generateRooms(new Random((int) ((Math.random() - 0.5) * 2000000000)));

        int currRoom = 0;
        rooms[currRoom].setUnlocked(true);
        rooms[currRoom].setVisited(true);

        System.out.println();

        while (true) {

            rooms[currRoom].setUnlocked(true);
            rooms[currRoom].setVisited(true);
            RoomDisplay.displayRooms(rooms, currRoom);

            System.out.println();

            if (rooms[currRoom].getType() == RoomType.BATTLE) {

                Game.Battle.BattleScene(s, rooms[currRoom], player, random);

            } else if (rooms[currRoom].getType() == RoomType.ITEM) {


                    
            } else if (rooms[currRoom].getType() == RoomType.BOSS) {


                    
            } else if (rooms[currRoom].getType() == RoomType.FINAL_BOSS) {


                    
            } else if (rooms[currRoom].getType() == RoomType.START) {

                System.out.println("Current room is the starting room.");
                System.out.println();

            }

            currRoom = MapMovement.Move(s, rooms, currRoom);

        }
    }
}
