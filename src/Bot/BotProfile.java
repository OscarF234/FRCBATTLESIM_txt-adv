package Bot;

public class BotProfile {

    private int health;
    private String teamNumber;
    private String teamName;
    private Item drive;
    private Item weapon;
    private Item armor;
    private Item power;
    private int[]pos;

    public BotProfile(String teamNumber, String teamName, Item drive, Item weapon, Item armor, Item power) {
        this.health = 100;
        this.teamNumber = teamNumber;
        this.teamName = teamName;
        this.drive = drive;
        this.weapon = weapon;
        this.armor = armor;
        this.power = power;
        this.pos = new int[2];
    }

    public String getTeamNumber() {
        return this.teamNumber;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Item getDrive() {
        return this.drive;
    }

    public Item getWeapon() {
        return this.weapon;
    }

    public Item getArmor() {
        return this.armor;
    }

    public Item getPower() {
        return this.power;
    }

    public int getHealth() {
        return this.health;
    }

    public void damageBot(int damage) {
        this.health-=damage;
    }

    public int[] getPos() {
        return this.pos;
    }

    public void setPos(int x, int y) {
        this.pos[0]=x;
        this.pos[1]=y;
    }

    public void changePos(int dx, int dy) {
        this.pos[0]+=dx;
        this.pos[1]+=dy;
    }

}
