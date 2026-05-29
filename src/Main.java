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
        TextFormatter.printInfo("Enter a seed to start your run or 0 for a random seed.");
        TextFormatter.printPrompt("Seed >");

        int seed = Integer.parseInt(s.next());

        Random random;

        if (seed != 0) {
            random = new Random(seed);
        } else {
            random = new Random((int) ((Math.random() - 0.5) * 2000000000));
        }

        Room[] rooms;

        rooms = new RoomGenerator().generateRooms(random);

        int currRoom = 0;
        rooms[currRoom].setUnlocked(true);
        rooms[currRoom].setVisited(true);

        boolean first = true;

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

            if (first) {
                TextFormatter.printSection("Start Room");
                TextFormatter.printInfo("You are in the starting room.");
                first = false;
            }

            currRoom = MapMovement.Move(s, rooms, currRoom);

            if (rooms[currRoom].getType() == RoomType.BATTLE) {

                Game.Battle.BattleScene(s, rooms[currRoom], player, random, false);

            } else if (rooms[currRoom].getType() == RoomType.ITEM) {

                Game.ItemRoom.ItemRoom(rooms[currRoom], player, random, s);
                    
            } else if (rooms[currRoom].getType() == RoomType.BOSS) {

                TextFormatter.printSection("BOSS BATTLE");
                Game.Battle.BattleScene(s, rooms[currRoom], player, random, true);
                    
            } else if (rooms[currRoom].getType() == RoomType.FINAL_BOSS) {

                TextFormatter.printSection("FINAL BOSS BATTLE");
                Game.Battle.BattleScene(s, rooms[currRoom], player, random, true);
                break;
                    
            } else if (rooms[currRoom].getType() == RoomType.START) {

                TextFormatter.printSection("Start Room");
                TextFormatter.printInfo("You are in the starting room.");

            }

        }

        if (player.getHealth() >= 0) {
            System.out.println("You win!");
            System.out.println("Blue banner is yours!");
        }
    }
}
