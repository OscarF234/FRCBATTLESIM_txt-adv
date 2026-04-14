package Bot;

public class Items {

    public static final Item[] DRIVE_ITEMS = {
        new Item("Tank Drive", "drive", 1),
        new Item("Mechanum Drive", "drive", 2),
        new Item("Swerve Drive", "drive", 3)
    };

    public static final Item[] ARM_ITEMS = {
        new Item("Hammer Arm", "weapon", 20),
        new Item("Sawblade Arm", "weapon", 30),
        new Item("Laser Arm", "weapon", 40)
    };

    public static final Item[] ARMOR_ITEMS = {
        new Item("Wedge Plow", "armor", 10),
        new Item("Shock Plating", "armor", 15),
        new Item("Titanium Shell", "armor", 25)
    };

    public static final Item[] POWER_ITEMS = {
        new Item("4V Battery", "power", 50),
        new Item("8V Battery", "power", 70),
        new Item("12V Battery", "power", 100)
    };

    public static final Item[] ALL_ITEMS = {
        DRIVE_ITEMS[0],
        DRIVE_ITEMS[1],
        DRIVE_ITEMS[2],
        ARM_ITEMS[0],
        ARM_ITEMS[1],
        ARM_ITEMS[2],
        ARMOR_ITEMS[0],
        ARMOR_ITEMS[1],
        ARMOR_ITEMS[2],
        POWER_ITEMS[0],
        POWER_ITEMS[1],
        POWER_ITEMS[2]
    };
}
