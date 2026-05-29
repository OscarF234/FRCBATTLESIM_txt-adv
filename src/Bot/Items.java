package Bot;

public class Items {

    public static final Item[] DRIVE_ITEMS = {
        new Item("Tank Drive", "drive", 1, 10),
        new Item("Mechanum Drive", "drive", 2, 40),
        new Item("Swerve Drive", "drive", 3, 70)
    };

    public static final Item[] EXTRA_DRIVE_ITEMS = {
        new Item("Toughbox Drive", "drive", 1, 15),
        new Item("Kitbot Chassis", "drive", 1, 18),
        new Item("West Coast Drive", "drive", 2, 28),
        new Item("NEO Drive Train", "drive", 2, 35),
        new Item("SDS Mk4i Swerve", "drive", 3, 60),
        new Item("Falcon Swerve Drive", "drive", 3, 80)
    };

    public static final Item[] ARM_ITEMS = {
        new Item("Hammer Arm", "weapon", 28, 1, 10),
        new Item("Sawblade Arm", "weapon", 40, 2, 40),
        new Item("Laser Arm", "weapon", 52, 3, 70)
    };

    public static final Item[] EXTRA_WEAPON_ITEMS = {
        new Item("Pneumatic Puncher", "weapon", 24, 1, 15),
        new Item("Roller Intake", "weapon", 26, 1, 18),
        new Item("Cube Catapult", "weapon", 34, 2, 30),
        new Item("Coral Claw", "weapon", 38, 1, 35),
        new Item("Note Launcher", "weapon", 44, 2, 45),
        new Item("Speaker Shooter", "weapon", 48, 3, 60),
        new Item("T-Shirt Cannon", "weapon", 36, 3, 32)
    };

    public static final Item[] ARMOR_ITEMS = {
        new Item("Wedge Plow", "armor", 5, 10),
        new Item("Shock Plating", "armor", 10, 40),
        new Item("Titanium Shell", "armor", 15, 70)
    };

    public static final Item[] EXTRA_ARMOR_ITEMS = {
        new Item("Polycarbonate Shield", "armor", 6, 12),
        new Item("Bumper Guard Set", "armor", 9, 20),
        new Item("Bellypan Armor", "armor", 12, 30),
        new Item("VersaFrame Rails", "armor", 14, 40),
        new Item("Gusset Reinforcement", "armor", 16, 50),
        new Item("Lexan Wrap", "armor", 18, 60)
    };

    public static final Item[] POWER_ITEMS = {
        new Item("4V Battery", "power", 50, 10),
        new Item("8V Battery", "power", 70, 40),
        new Item("12V Battery", "power", 100, 70)
    };

    public static final Item[] EXTRA_POWER_ITEMS = {
        new Item("Brownout Bypass", "power", 55, 15),
        new Item("PDH Upgrade", "power", 65, 25),
        new Item("CANivore Bus", "power", 75, 35),
        new Item("Dual Battery Harness", "power", 85, 50),
        new Item("Supercapacitor Pack", "power", 95, 60),
        new Item("RIO Overclock", "power", 90, 55)
    };

    public static final Item[] ALL_ITEMS = combineItems(
        DRIVE_ITEMS,
        EXTRA_DRIVE_ITEMS,
        ARM_ITEMS,
        EXTRA_WEAPON_ITEMS,
        ARMOR_ITEMS,
        EXTRA_ARMOR_ITEMS,
        POWER_ITEMS,
        EXTRA_POWER_ITEMS
    );

    private static Item[] combineItems(Item[]... itemGroups) {
        int totalItems = 0;

        for (Item[] itemGroup : itemGroups) {
            totalItems += itemGroup.length;
        }

        Item[] allItems = new Item[totalItems];
        int index = 0;

        for (Item[] itemGroup : itemGroups) {
            for (Item item : itemGroup) {
                allItems[index] = item;
                index++;
            }
        }

        return allItems;
    }
}
