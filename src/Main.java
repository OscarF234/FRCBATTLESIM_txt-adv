import Game.MapMovement;
import Game.Player;
import Game.TextFormatter;
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

        TextFormatter.printTitle("FRC Battle Simulator");
        System.out.println();
        TextFormatter.printInfo("Enter a seed to start your run.");
        TextFormatter.printPrompt("Seed >");

        int seed = Integer.parseInt(s.next());

        Random random = new Random(seed);

        Room[] rooms;

        rooms = new RoomGenerator().generateRooms(random);
        // rooms = new RoomGenerator().generateRooms(new Random((int) ((Math.random() - 0.5) * 2000000000)));

        int currRoom = 0;
        rooms[currRoom].setUnlocked(true);
        rooms[currRoom].setVisited(true);

        while (true) {

            if (player.getHealth() <= 0) {
                TextFormatter.printSection("Run Over");
                TextFormatter.printWarning("You were defeated. Better luck next time.");
                break;
            }

            rooms[currRoom].setUnlocked(true);
            rooms[currRoom].setVisited(true);
            TextFormatter.printSection("Map");
            RoomDisplay.displayRooms(rooms, currRoom);
            TextFormatter.printInfo("\nST Start | BA Battle | IT Item | BO Boss | FB Final Boss");

            if (rooms[currRoom].getType() == RoomType.BATTLE) {

                Game.Battle.BattleScene(s, rooms[currRoom], player, random);

            } else if (rooms[currRoom].getType() == RoomType.ITEM) {

                Game.ItemRoom.ItemRoom(rooms[currRoom], player, random, s);
                    
            } else if (rooms[currRoom].getType() == RoomType.BOSS) {

                TextFormatter.printSection("BOSS BATTLE");
                Game.Battle.BattleScene(s, rooms[currRoom], player, random);
                    
            } else if (rooms[currRoom].getType() == RoomType.FINAL_BOSS) {

                TextFormatter.printSection("FINAL BOSS BATTLE");
                Game.Battle.BattleScene(s, rooms[currRoom], player, random);
                break;
                    
            } else if (rooms[currRoom].getType() == RoomType.START) {

                TextFormatter.printSection("Start Room");
                TextFormatter.printInfo("You are in the starting room.");

            }

            currRoom = MapMovement.Move(s, rooms, currRoom);

        }

        System.out.println("You win!");
        System.out.println("Blue banner is yours!");
    }
}
