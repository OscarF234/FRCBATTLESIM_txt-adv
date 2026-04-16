package Game;

import Bot.Item;
import Rooms.Room;
import java.util.Random;
import java.util.Scanner;

public class ItemRoom {

    public static void ItemRoom(Room room, Player player, Random random, Scanner s) {

        TextFormatter.printSection("Item Room");
        TextFormatter.printInfo("Credits: " + player.getCredits());

        for (int i = 0; i < room.getItems().length; i++) {
            Item item = room.getItems()[i];
            TextFormatter.printInfo("[" + (i + 1) + "] " + item.getItemId() + " | " + item.getPrice() + " credits");
        }

        while (true) {
            TextFormatter.printPrompt("Choose item [1-3] or [Q] to leave >");
            String item = s.next();

            if (item.equalsIgnoreCase("q")) {
                break;
            }

            if (item.equals("1") || item.equals("2") || item.equals("3")) {
                int itemIndex = Integer.parseInt(item) - 1;
                Item chosenItem = room.getItems()[itemIndex];

                if (player.getCredits() >= chosenItem.getPrice()) {
                    player.reduceCredits(chosenItem.getPrice());
                    equipItem(player, chosenItem);
                    TextFormatter.printSuccess("You bought " + chosenItem.getItemId() + ".");
                    TextFormatter.printInfo("Credits left: " + player.getCredits());
                } else {
                    TextFormatter.printWarning("Not enough credits.");
                }

                continue;
            }

            TextFormatter.printWarning("Invalid choice.");
        }
    }

    private static void equipItem(Player player, Item item) {
        if (item.getSubsystem().equals("drive")) {
            player.setDrive(item);
        } else if (item.getSubsystem().equals("armor")) {
            player.setArmor(item);
        } else if (item.getSubsystem().equals("weapon")) {
            player.setWeapon(item);
        } else if (item.getSubsystem().equals("power")) {
            player.setPower(item);
        }
    }
}
