package Bot;

public class Item {

    private String id;
    private String subsystem;
    private int magnitude;
    private int range;
    private int price;

    public Item(String id, String subsystem, int magnitude, int price) {
        this(id, subsystem, magnitude, 0, price);
    }

    public Item(String id, String subsystem, int magnitude, int range, int price) {
        this.id = id;
        this.subsystem = subsystem;
        this.magnitude = magnitude;
        this.range = range;
        this.price = price;
    }

    public String getItemId() {
        return this.id;
    }

    public String getSubsystem() {
        return this.subsystem;
    }

    public int getMag() {
        return this.magnitude;
    }

    public int getRange() {
        return this.range;
    }

    public int getPrice() {
        return this.price;
    }

}
