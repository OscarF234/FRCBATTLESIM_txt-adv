package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import Bot.BotProfile;
import Rooms.Room;
import Rooms.RoomType;

public class Battle {

    private static final int FIELD_SIZE = 4;
    private static final Random BATTLE_RANDOM = new Random();

    public static void BattleScene(Scanner s, Room room, Player player, Random random) {

        if (!room.isClear()) {
            System.out.println("The Battle Begins!");
            System.out.println("Team 7558 ALT-F4 vs Team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + "!");

            player.setPos(3, 3);
            room.getBotProfile().setPos(0, 0);

            while (true) { 
                
                if (room.getBotProfile().getHealth() <= 0) {
                    break;
                }

                if (player.getHealth() <= 0) {
                    break;
                }

                System.out.println("Your health: " + player.getHealth());
                System.out.println("Enemy health: " + room.getBotProfile().getHealth());

                drawField(room.getBotProfile(), player);

                System.out.println("Make a move!");
                System.out.println("1) Move");
                System.out.println("2) Attack");

                System.out.print("Enter your next move: ");
                    
                String input = s.next();
                int playerAction = 0;
                int playerDirection = 0;
                 
                while (true) { 

                    if (input.equals("1")) {

                        while (true) {

                            System.out.println("1) left");
                            System.out.println("2) right");
                            System.out.println("3) up");
                            System.out.println("4) down");
                            System.out.print("Enter direction: ");

                            String dir = s.next();

                            if (dir.equals("1") && player.getPos()[0] - player.getPlayerMovement() >= 0) {

                                playerAction = 1;
                                playerDirection = 1;
                                player.changePos(-1 * player.getPlayerMovement(), 0);
                                int heal = player.heal(random);
                                System.out.println("You heal for " + heal + " health!");
                                break;
                            }

                            if (dir.equals("2") && player.getPos()[0] + player.getPlayerMovement() <= 3) {

                                playerAction = 1;
                                playerDirection = 2;
                                player.changePos(player.getPlayerMovement(), 0);
                                int heal = player.heal(random);
                                System.out.println("You heal for " + heal + " health!");
                                break;
                            }

                            if (dir.equals("3") && player.getPos()[1] - player.getPlayerMovement() >= 0) {

                                playerAction = 1;
                                playerDirection = 3;
                                player.changePos(0, -1 * player.getPlayerMovement());
                                int heal = player.heal(random);
                                System.out.println("You heal for " + heal + " health!");
                                break;
                            }

                            if (dir.equals("4") && player.getPos()[1] + player.getPlayerMovement() <= 3) {

                                playerAction = 1;
                                playerDirection = 4;
                                player.changePos(0, player.getPlayerMovement());
                                int heal = player.heal(random);
                                System.out.println("You heal for " + heal + " health!");
                                break;
                            }

                            System.out.println("Invalid direction.");
                        }

                        break;

                    } else if (input.equals("2")) {

                        while (true) {

                            System.out.println("1) left");
                            System.out.println("2) right");
                            System.out.println("3) up");
                            System.out.println("4) down");
                            System.out.print("Enter direction: ");

                            String dir = s.next();

                            if (dir.equals("1")) {

                                playerAction = 2;
                                playerDirection = 1;
                                break;
                            }

                            if (dir.equals("2")) {

                                playerAction = 2;
                                playerDirection = 2;
                                break;
                            }

                            if (dir.equals("3")) {

                                playerAction = 2;
                                playerDirection = 3;
                                break;
                            }

                            if (dir.equals("4")) {

                                playerAction = 2;
                                playerDirection = 4;
                                break;
                            }

                            System.out.println("Invalid direction.");
                        }

                        break;
                    }


                }

                int enemyAction = 0;
                int enemyDirection = 0;

                if (room.getBotProfile().getHealth() > 0 && player.getHealth() > 0) {
                    int skill = getBotSkill(room.getBotProfile(), room.getType());
                    enemyAction = chooseEnemyAction(room.getBotProfile(), player, skill);
                    enemyDirection = chooseEnemyDirection(room.getBotProfile(), player, enemyAction, skill);

                    if (enemyDirection == 0) {
                        System.out.println("Team " + room.getBotProfile().getTeamNumber() + " hesitates.");
                    } else if (enemyAction == 1) {
                        moveBot(room.getBotProfile(), enemyDirection);
                        System.out.println("Team " + room.getBotProfile().getTeamNumber() + " moves " + getDirectionName(enemyDirection) + ".");
                    }
                }

                if (Arrays.equals(room.getBotProfile().getPos(),player.getPos())) {

                    System.out.println("A collision happens!");

                    int dmg = random.nextInt(30);

                    if (room.getBotProfile().getDrive().getMag() > player.getPlayerMovement()) {
                        System.out.println("Team " + room.getBotProfile().getTeamNumber() + " has the superior drivetrain and rams 7558 for " + dmg + " damage!");
                        player.hitPlayer(dmg);
                    } else if (room.getBotProfile().getDrive().getMag() < player.getPlayerMovement()) {
                        System.out.println("Team 7558 has the superior drivetrain and rams team" + room.getBotProfile().getTeamNumber() + " for " + dmg + " damage!");
                        room.getBotProfile().damageBot(dmg);
                    } else {
                        if (random.nextInt(2) == 1) {
                            System.out.println("With evenly matched drivetrains, team " + room.getBotProfile().getTeamNumber() + " gets lucky and hits team 7558 for " + dmg + " damage!");
                            player.hitPlayer(dmg);
                        } else {
                            System.out.println("With evenly matched drivetrains, team 7558 gets lucky and hits team" + room.getBotProfile().getTeamNumber() + " for " + dmg + " damage!");
                            room.getBotProfile().damageBot(dmg);
                        }
                    }
                }

                if (playerAction == 2 && room.getBotProfile().getHealth() > 0) {
                    performPlayerAttack(room.getBotProfile(), player, playerDirection);
                }

                if (enemyAction == 2 && enemyDirection != 0 && room.getBotProfile().getHealth() > 0 && player.getHealth() > 0) {
                    performEnemyAttack(room.getBotProfile(), player, enemyDirection);
                }
            }

            if (room.getBotProfile().getHealth() <= 0) {
                System.out.println("Team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + " has been defeated!");
                room.setCleared(true);
            } else if (player.getHealth() <= 0) {
                System.out.println("ALT-F4 has been destroyed!");
            }
        } else {
            System.out.println("The remains of team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + " lies here...");
        }
    }

    public static void drawField(BotProfile bot, Player player) {

        String line = "";

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if (Arrays.equals(bot.getPos(), player.getPos()) && bot.getPos()[0] == j && bot.getPos()[1] == i) {
                    line += "CO===";
                } else if (bot.getPos()[0] == j && bot.getPos()[1] == i) {
                    line += "EN===";
                } else if (player.getPos()[0] == j && player.getPos()[1] == i) {
                    line += "PL===";
                } else {
                    line += "[]===";
                }

            }

            line = line.substring(0, 17);
            System.out.println(line);

            line = "";

            System.out.println("||   ||   ||   ||");
        }

    }

    private static int chooseEnemyAction(BotProfile bot, Player player, int skill) {
        List<Integer> attackDirections = getAttackDirections(bot, player);
        List<Integer> moveDirections = getMoveDirections(bot);
        boolean smartChoice = BATTLE_RANDOM.nextInt(100) < 5 + skill * 10;

        if (smartChoice && !attackDirections.isEmpty()) {
            return 2;
        }

        if (smartChoice && !moveDirections.isEmpty()) {
            return 1;
        }

        if (moveDirections.isEmpty()) {
            return 2;
        }

        if (attackDirections.isEmpty()) {
            return 1;
        }

        if (BATTLE_RANDOM.nextInt(100) < 65) {
            return 1;
        }

        return 2;
    }

    private static int chooseEnemyDirection(BotProfile bot, Player player, int action, int skill) {
        List<Integer> possibleDirections;

        if (action == 1) {
            possibleDirections = getMoveDirections(bot);
        } else {
            possibleDirections = getAttackDirections(bot, player);
        }

        if (possibleDirections.isEmpty()) {
            return 0;
        }

        if (BATTLE_RANDOM.nextInt(100) < Math.max(0, 28 - skill * 4)) {
            return 0;
        }

        boolean smartChoice = BATTLE_RANDOM.nextInt(100) < 10 + skill * 10;

        if (!smartChoice) {
            return possibleDirections.get(BATTLE_RANDOM.nextInt(possibleDirections.size()));
        }

        return getBestDirection(bot, player, possibleDirections, action);
    }

    private static int getBestDirection(BotProfile bot, Player player, List<Integer> possibleDirections, int action) {
        int bestDirection = possibleDirections.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (int direction : possibleDirections) {
            int score;

            if (action == 1) {
                score = scoreMoveDirection(bot, player, direction);
            } else {
                score = scoreAttackDirection(bot, player, direction);
            }

            if (score > bestScore) {
                bestScore = score;
                bestDirection = direction;
            }
        }

        return bestDirection;
    }

    private static int scoreMoveDirection(BotProfile bot, Player player, int direction) {
        int moveAmount = bot.getDrive().getMag();
        int nextX = bot.getPos()[0];
        int nextY = bot.getPos()[1];

        if (direction == 1) {
            nextX -= moveAmount;
        }

        if (direction == 2) {
            nextX += moveAmount;
        }

        if (direction == 3) {
            nextY -= moveAmount;
        }

        if (direction == 4) {
            nextY += moveAmount;
        }

        int distance = Math.abs(player.getPos()[0] - nextX) + Math.abs(player.getPos()[1] - nextY);
        return -distance;
    }

    private static int scoreAttackDirection(BotProfile bot, Player player, int direction) {
        if (direction == 1) {
            return bot.getPos()[0] - player.getPos()[0];
        }

        if (direction == 2) {
            return player.getPos()[0] - bot.getPos()[0];
        }

        if (direction == 3) {
            return bot.getPos()[1] - player.getPos()[1];
        }

        return player.getPos()[1] - bot.getPos()[1];
    }

    private static List<Integer> getMoveDirections(BotProfile bot) {
        List<Integer> directions = new ArrayList<>();
        int moveAmount = bot.getDrive().getMag();

        if (bot.getPos()[0] - moveAmount >= 0) {
            directions.add(1);
        }

        if (bot.getPos()[0] + moveAmount < FIELD_SIZE) {
            directions.add(2);
        }

        if (bot.getPos()[1] - moveAmount >= 0) {
            directions.add(3);
        }

        if (bot.getPos()[1] + moveAmount < FIELD_SIZE) {
            directions.add(4);
        }

        return directions;
    }

    private static List<Integer> getAttackDirections(BotProfile bot, Player player) {
        List<Integer> directions = new ArrayList<>();

        if (player.getPos()[1] == bot.getPos()[1] && player.getPos()[0] < bot.getPos()[0]) {
            directions.add(1);
        }

        if (player.getPos()[1] == bot.getPos()[1] && player.getPos()[0] > bot.getPos()[0]) {
            directions.add(2);
        }

        if (player.getPos()[0] == bot.getPos()[0] && player.getPos()[1] < bot.getPos()[1]) {
            directions.add(3);
        }

        if (player.getPos()[0] == bot.getPos()[0] && player.getPos()[1] > bot.getPos()[1]) {
            directions.add(4);
        }

        return directions;
    }

    private static void moveBot(BotProfile bot, int direction) {
        int moveAmount = bot.getDrive().getMag();

        if (direction == 1) {
            bot.changePos(-1 * moveAmount, 0);
        }

        if (direction == 2) {
            bot.changePos(moveAmount, 0);
        }

        if (direction == 3) {
            bot.changePos(0, -1 * moveAmount);
        }

        if (direction == 4) {
            bot.changePos(0, moveAmount);
        }
    }

    private static void performPlayerAttack(BotProfile bot, Player player, int direction) {
        System.out.println("ALT-F4 attacks " + getDirectionName(direction) + ".");

        if (!isTargetInDirection(player.getPos(), bot.getPos(), direction)) {
            System.out.println("The attack hits nothing.");
            return;
        }

        if (!player.getSuccessfulHit(BATTLE_RANDOM)) {
            System.out.println("The attack misses!");
            return;
        }

        int damage = calculateDamage(player.getPlayerDamage(), bot.getArmor().getMag());
        bot.damageBot(damage);
        System.out.println("Direct hit for " + damage + " damage!");
    }

    private static void performEnemyAttack(BotProfile bot, Player player, int direction) {
        System.out.println("Team " + bot.getTeamNumber() + " attacks " + getDirectionName(direction) + ".");

        if (!isTargetInDirection(bot.getPos(), player.getPos(), direction)) {
            System.out.println("The enemy attack hits nothing.");
            return;
        }

        if (!botSuccessfulHit(bot)) {
            System.out.println("The enemy attack misses!");
            return;
        }

        int damage = calculateDamage(bot.getWeapon().getMag(), player.getPlayerArmor());
        player.hitPlayer(damage);
        System.out.println("ALT-F4 takes " + damage + " damage!");
    }

    private static boolean isTargetInDirection(int[] attackerPos, int[] targetPos, int direction) {
        if (direction == 1) {
            return targetPos[1] == attackerPos[1] && targetPos[0] < attackerPos[0];
        }

        if (direction == 2) {
            return targetPos[1] == attackerPos[1] && targetPos[0] > attackerPos[0];
        }

        if (direction == 3) {
            return targetPos[0] == attackerPos[0] && targetPos[1] < attackerPos[1];
        }

        return targetPos[0] == attackerPos[0] && targetPos[1] > attackerPos[1];
    }

    private static boolean botSuccessfulHit(BotProfile bot) {
        return BATTLE_RANDOM.nextInt(100) < bot.getPower().getMag();
    }

    private static int calculateDamage(int attack, int defense) {
        return Math.max(5, attack - defense / 2);
    }

    private static int getBotSkill(BotProfile bot) {
        int score = 0;

        score += bot.getDrive().getMag();
        score += bot.getWeapon().getMag() / 10;
        score += bot.getArmor().getMag() / 10;
        score += bot.getPower().getMag() / 25;

        if (score >= 13) {
            return 5;
        }

        if (score >= 10) {
            return 4;
        }

        if (score >= 8) {
            return 3;
        }

        if (score >= 6) {
            return 2;
        }

        return 1;
    }

    private static int getBotSkill(BotProfile bot, RoomType roomType) {
        int skill = getBotSkill(bot);

        if (roomType == RoomType.BATTLE) {
            return Math.max(1, skill - 2);
        }

        if (roomType == RoomType.BOSS) {
            return Math.max(1, skill - 1);
        }

        return skill;
    }

    private static String getDirectionName(int direction) {
        if (direction == 1) {
            return "left";
        }

        if (direction == 2) {
            return "right";
        }

        if (direction == 3) {
            return "up";
        }

        return "down";
    }
}
