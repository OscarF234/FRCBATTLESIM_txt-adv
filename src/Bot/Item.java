package Bot;

public class Item {

    private String id;
    private String subsystem;
    private int magnitude;

    public Item(String id, String subsystem, int magnitude) {
        this.id = id;
        this.subsystem = subsystem;
        this.magnitude = magnitude;
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

}
