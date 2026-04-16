package Game;
import Bot.Item;
import Bot.Items;
import java.util.HashMap;
import java.util.Random;

public class Player {

    private static final int MAX_HEALTH = 50;
    
    private int health;
    private HashMap<String, Item> subsystems;
    private int[] pos;
    private int credits;
    
    public Player() {
        this.health = MAX_HEALTH;
        this.subsystems = new HashMap<>();
        this.subsystems.put("drive", Items.DRIVE_ITEMS[0]);
        this.subsystems.put("weapon", Items.ARM_ITEMS[0]);
        this.subsystems.put("armor", Items.ARMOR_ITEMS[0]);
        this.subsystems.put("power", Items.POWER_ITEMS[0]);
        this.pos = new int[2];
        this.credits = 0;
    }

    public int getPlayerDamage() {
        return this.subsystems.get("weapon").getMag();
    }

    public int getPlayerRange() {
        return this.subsystems.get("weapon").getRange();
    }

    public int getPlayerArmor() {
        return this.subsystems.get("armor").getMag();
    }

    public int getPlayerMovement() {
        return this.subsystems.get("drive").getMag();
    }

    public boolean getSuccessfulHit(Random random) {
        return random.nextInt(100) < this.subsystems.get("power").getMag();
    }

    public void setWeapon(Item weapon) {
        this.subsystems.put("weapon", weapon);
    }

    public void setDrive(Item drive) {
        this.subsystems.put("drive", drive);
    }

    public void setArmor(Item armor) {
        this.subsystems.put("armor", armor);
    }

    public void setPower(Item power) {
        this.subsystems.put("power", power);
    }

    public void hitPlayer(int damage) {
        this.health-=damage;
    }

    public int heal(Random random) {
        int heal = random.nextInt(20);
        this.health = Math.min(this.health + heal, MAX_HEALTH);
        return heal;
    }

    public int getHealth() {
        return this.health;
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

    public void reduceCredits(int amt) {
        if (this.credits-amt >= 0) {
            this.credits-=amt;
        }
    }

    public void increaseCredits(int amt) {
        this.credits+=amt;
    }

    public int getCredits() {
        return this.credits;
    }

}
