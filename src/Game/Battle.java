package Game;

import Bot.BotProfile;
import Rooms.Room;
import Rooms.RoomType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {

    private static final int FIELD_SIZE = 4;
    private static final Random BATTLE_RANDOM = new Random();

    public static void BattleScene(Scanner s, Room room, Player player, Random random) {

        if (!room.isClear()) {
            TextFormatter.printTitle("Battle Start");
            TextFormatter.printInfo("Team 7558 ALT-F4 vs Team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + "!");
            balanceEnemyForRoom(room);

            player.setPos(3, 3);
            room.getBotProfile().setPos(0, 0);

            while (true) { 
                
                if (room.getBotProfile().getHealth() <= 0) {
                    break;
                }

                if (player.getHealth() <= 0) {
                    break;
                }

                TextFormatter.printSection("Battlefield");
                TextFormatter.printBattleStatus("Team " + room.getBotProfile().getTeamNumber(), player.getHealth(), room.getBotProfile().getHealth());

                drawField(room.getBotProfile(), player);

                int enemyAction = 0;
                int enemyDirection = 0;
                int enemyMoveAmount = 0;

                if (room.getBotProfile().getHealth() > 0 && player.getHealth() > 0) {
                    int skill = getBotSkill(room.getBotProfile(), room.getType());
                    enemyAction = chooseEnemyAction(room.getBotProfile(), player, skill);

                    if (enemyAction == 1) {
                        int[] enemyMoveChoice = chooseEnemyMove(room.getBotProfile(), player, skill);
                        enemyDirection = enemyMoveChoice[0];
                        enemyMoveAmount = enemyMoveChoice[1];
                    } else if (enemyAction == 2) {
                        enemyDirection = chooseEnemyAttackDirection(room.getBotProfile(), player, skill);
                    }
                }

                TextFormatter.printMenu("Choose action:", "[M] Move", "[F] Attack");
                TextFormatter.printPrompt("Action >");
                    
                String input = s.next().toLowerCase();
                int playerAction = 0;
                int playerDirection = 0;
                 
                while (true) { 

                    if (isMoveInput(input)) {

                        while (true) {

                            TextFormatter.printMenu("Choose direction:", "[W] Up", "[A] Left", "[S] Down", "[D] Right");
                            TextFormatter.printPrompt("Direction >");

                            String dir = s.next().toLowerCase();
                            int direction = parseDirection(dir);
                            int maxMoveAmount = getMaxMoveAmount(player.getPos(), direction, player.getPlayerMovement());

                            if (maxMoveAmount > 0) {
                                while (true) {
                                    TextFormatter.printPrompt("Squares to move (1-" + maxMoveAmount + ") >");
                                    String moveAmountInput = s.next();

                                    if (isValidMoveAmount(moveAmountInput, maxMoveAmount)) {
                                        int moveAmount = Integer.parseInt(moveAmountInput);
                                        playerAction = 1;
                                        playerDirection = direction;
                                        movePlayer(player, direction, moveAmount);
                                        int heal = player.heal(random);
                                        TextFormatter.printInfo("You move " + getDirectionName(direction) + " " + moveAmount + " square" + getPlural(moveAmount) + ".");
                                        TextFormatter.printInfo("You heal for " + heal + " health.");
                                        break;
                                    }

                                    TextFormatter.printWarning("Invalid number of squares.");
                                }

                                if (playerAction == 1) {
                                    break;
                                }
                            }

                            TextFormatter.printWarning("Invalid direction.");
                        }

                        break;

                    } else if (isAttackInput(input)) {

                        while (true) {

                            TextFormatter.printMenu("Choose attack direction:", "[W] Up", "[A] Left", "[S] Down", "[D] Right");
                            TextFormatter.printPrompt("Direction >");

                            String dir = s.next().toLowerCase();
                            int direction = parseDirection(dir);

                            if (direction != 0) {
                                playerAction = 2;
                                playerDirection = direction;
                                break;
                            }

                            TextFormatter.printWarning("Invalid direction.");
                        }

                        break;
                        
                    } else {

                    TextFormatter.printWarning("Invalid action.");
                    TextFormatter.printMenu("Choose action:", "[M] Move", "[F] Attack");
                    TextFormatter.printPrompt("Action >");
                    input = s.next().toLowerCase();

                    }

                }

                if (room.getBotProfile().getHealth() > 0 && player.getHealth() > 0) {
                    if (enemyDirection == 0) {
                        TextFormatter.printInfo("Team " + room.getBotProfile().getTeamNumber() + " hesitates.");
                    } else if (enemyAction == 1) {
                        moveBot(room.getBotProfile(), enemyDirection, enemyMoveAmount);
                        TextFormatter.printInfo("Team " + room.getBotProfile().getTeamNumber() + " moves " + getDirectionName(enemyDirection) + " " + enemyMoveAmount + " square" + getPlural(enemyMoveAmount) + ".");
                    }
                }

                if (Arrays.equals(room.getBotProfile().getPos(),player.getPos())) {

                    TextFormatter.printWarning("Collision!");

                    int dmg = getCollisionDamage(random, room.getType());

                    if (room.getBotProfile().getDrive().getMag() > player.getPlayerMovement()) {
                        TextFormatter.printWarning("Team " + room.getBotProfile().getTeamNumber() + " has the superior drivetrain and rams 7558 for " + dmg + " damage!");
                        player.hitPlayer(Math.max(dmg - player.getPlayerArmor(), 0));
                    } else if (room.getBotProfile().getDrive().getMag() < player.getPlayerMovement()) {
                        TextFormatter.printSuccess("Team 7558 has the superior drivetrain and rams team " + room.getBotProfile().getTeamNumber() + " for " + dmg + " damage!");
                        room.getBotProfile().damageBot(Math.max(dmg - room.getBotProfile().getArmor().getMag(), 0));
                    } else {
                        if (random.nextInt(2) == 1) {
                            TextFormatter.printWarning("With evenly matched drivetrains, team " + room.getBotProfile().getTeamNumber() + " gets lucky and hits team 7558 for " + dmg + " damage!");
                            player.hitPlayer(Math.max(dmg - player.getPlayerArmor(), 0));
                        } else {
                            TextFormatter.printSuccess("With evenly matched drivetrains, team 7558 gets lucky and hits team " + room.getBotProfile().getTeamNumber() + " for " + dmg + " damage!");
                            room.getBotProfile().damageBot(Math.max(dmg - room.getBotProfile().getArmor().getMag(), 0));
                        }
                    }
                }

                if (playerAction == 2 && room.getBotProfile().getHealth() > 0) {
                    performPlayerAttack(room.getBotProfile(), player, playerDirection);
                }

                if (enemyAction == 2 && enemyDirection != 0 && room.getBotProfile().getHealth() > 0 && player.getHealth() > 0) {
                    performEnemyAttack(room.getBotProfile(), player, enemyDirection, room.getType());
                }
            }

            if (room.getBotProfile().getHealth() <= 0) {
                TextFormatter.printSuccess("Team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + " has been defeated!");
                room.setCleared(true);

                int gain = random.nextInt(50);
                System.out.println("You gain " + gain + " credits!");
                player.increaseCredits(gain);
            } else if (player.getHealth() <= 0) {
                TextFormatter.printWarning("ALT-F4 has been destroyed!");
            }
        } else {
            TextFormatter.printInfo("The remains of team " + room.getBotProfile().getTeamNumber() + " " + room.getBotProfile().getTeamName() + " lie here...");
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
        List<int[]> moveChoices = getMoveChoices(bot);
        boolean smartChoice = BATTLE_RANDOM.nextInt(100) < 5 + skill * 10;

        if (!attackDirections.isEmpty() && BATTLE_RANDOM.nextInt(100) < 35 + skill * 10) {
            return 2;
        }

        if (smartChoice && !attackDirections.isEmpty()) {
            return 2;
        }

        if (smartChoice && !moveChoices.isEmpty()) {
            return 1;
        }

        if (moveChoices.isEmpty()) {
            return 2;
        }

        if (attackDirections.isEmpty()) {
            return 1;
        }

        if (BATTLE_RANDOM.nextInt(100) < 55) {
            return 1;
        }

        return 2;
    }

    private static int[] chooseEnemyMove(BotProfile bot, Player player, int skill) {
        List<int[]> possibleMoves = getMoveChoices(bot);

        if (possibleMoves.isEmpty()) {
            return new int[] { 0, 0 };
        }

        if (BATTLE_RANDOM.nextInt(100) < Math.max(0, 16 - skill * 2)) {
            return new int[] { 0, 0 };
        }

        boolean smartChoice = BATTLE_RANDOM.nextInt(100) < 15 + skill * 10;

        if (!smartChoice) {
            return possibleMoves.get(BATTLE_RANDOM.nextInt(possibleMoves.size()));
        }

        return getBestMoveChoice(bot, player, possibleMoves);
    }

    private static int chooseEnemyAttackDirection(BotProfile bot, Player player, int skill) {
        List<Integer> possibleDirections = getAttackDirections(bot, player);

        if (possibleDirections.isEmpty()) {
            return 0;
        }

        boolean smartChoice = BATTLE_RANDOM.nextInt(100) < 15 + skill * 10;

        if (!smartChoice) {
            return possibleDirections.get(BATTLE_RANDOM.nextInt(possibleDirections.size()));
        }

        return getBestAttackDirection(bot, player, possibleDirections);
    }

    private static int[] getBestMoveChoice(BotProfile bot, Player player, List<int[]> possibleMoves) {
        int[] bestMove = possibleMoves.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (int[] moveChoice : possibleMoves) {
            int score = scoreMoveChoice(bot, player, moveChoice[0], moveChoice[1]);

            if (score > bestScore) {
                bestScore = score;
                bestMove = moveChoice;
            }
        }

        return bestMove;
    }

    private static int getBestAttackDirection(BotProfile bot, Player player, List<Integer> possibleDirections) {
        int bestDirection = possibleDirections.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (int direction : possibleDirections) {
            int score = scoreAttackDirection(bot, player, direction);

            if (score > bestScore) {
                bestScore = score;
                bestDirection = direction;
            }
        }

        return bestDirection;
    }

    private static int scoreMoveChoice(BotProfile bot, Player player, int direction, int moveAmount) {
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

    private static List<int[]> getMoveChoices(BotProfile bot) {
        List<int[]> moveChoices = new ArrayList<>();
        int maxMoveAmount = bot.getDrive().getMag();

        for (int direction = 1; direction <= 4; direction++) {
            int legalMoveAmount = getMaxMoveAmount(bot.getPos(), direction, maxMoveAmount);

            for (int moveAmount = 1; moveAmount <= legalMoveAmount; moveAmount++) {
                moveChoices.add(new int[] { direction, moveAmount });
            }
        }

        return moveChoices;
    }

    private static List<Integer> getAttackDirections(BotProfile bot, Player player) {
        List<Integer> directions = new ArrayList<>();
        int range = bot.getWeapon().getRange();

        if (isTargetInDirection(bot.getPos(), player.getPos(), 1, range)) {
            directions.add(1);
        }

        if (isTargetInDirection(bot.getPos(), player.getPos(), 2, range)) {
            directions.add(2);
        }

        if (isTargetInDirection(bot.getPos(), player.getPos(), 3, range)) {
            directions.add(3);
        }

        if (isTargetInDirection(bot.getPos(), player.getPos(), 4, range)) {
            directions.add(4);
        }

        return directions;
    }

    private static void moveBot(BotProfile bot, int direction, int moveAmount) {
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

    private static void movePlayer(Player player, int direction, int moveAmount) {
        if (direction == 1) {
            player.changePos(-1 * moveAmount, 0);
        }

        if (direction == 2) {
            player.changePos(moveAmount, 0);
        }

        if (direction == 3) {
            player.changePos(0, -1 * moveAmount);
        }

        if (direction == 4) {
            player.changePos(0, moveAmount);
        }
    }

    private static void performPlayerAttack(BotProfile bot, Player player, int direction) {
        TextFormatter.printInfo("ALT-F4 attacks " + getDirectionName(direction) + ".");

        if (!isTargetInDirection(player.getPos(), bot.getPos(), direction, player.getPlayerRange())) {
            TextFormatter.printWarning("The attack hits nothing.");
            return;
        }

        if (!player.getSuccessfulHit(BATTLE_RANDOM)) {
            TextFormatter.printWarning("The attack misses!");
            return;
        }

        int damage = calculateDamage(player.getPlayerDamage(), bot.getArmor().getMag());
        bot.damageBot(damage);
        TextFormatter.printSuccess("Direct hit for " + damage + " damage!");
    }

    private static void performEnemyAttack(BotProfile bot, Player player, int direction, RoomType roomType) {
        TextFormatter.printInfo("Team " + bot.getTeamNumber() + " attacks " + getDirectionName(direction) + ".");

        if (!isTargetInDirection(bot.getPos(), player.getPos(), direction, bot.getWeapon().getRange())) {
            TextFormatter.printWarning("The enemy attack hits nothing.");
            return;
        }

        if (!botSuccessfulHit(bot, roomType)) {
            TextFormatter.printWarning("The enemy attack misses!");
            return;
        }

        int damage = calculateEnemyDamage(bot.getWeapon().getMag(), player.getPlayerArmor(), roomType);
        player.hitPlayer(damage);
        TextFormatter.printWarning("ALT-F4 takes " + damage + " damage!");
    }

    private static boolean isTargetInDirection(int[] attackerPos, int[] targetPos, int direction, int range) {
        if (direction == 1) {
            return targetPos[1] == attackerPos[1]
                && targetPos[0] < attackerPos[0]
                && attackerPos[0] - targetPos[0] <= range;
        }

        if (direction == 2) {
            return targetPos[1] == attackerPos[1]
                && targetPos[0] > attackerPos[0]
                && targetPos[0] - attackerPos[0] <= range;
        }

        if (direction == 3) {
            return targetPos[0] == attackerPos[0]
                && targetPos[1] < attackerPos[1]
                && attackerPos[1] - targetPos[1] <= range;
        }

        return targetPos[0] == attackerPos[0]
            && targetPos[1] > attackerPos[1]
            && targetPos[1] - attackerPos[1] <= range;
    }

    private static boolean botSuccessfulHit(BotProfile bot, RoomType roomType) {
        int hitChance = bot.getPower().getMag();

        if (roomType == RoomType.BATTLE) {
            hitChance -= 24;
        }

        if (roomType == RoomType.BOSS) {
            hitChance -= 5;
        }

        hitChance = Math.max(30, Math.min(hitChance, 95));
        return BATTLE_RANDOM.nextInt(100) < hitChance;
    }

    private static int calculateDamage(int attack, int defense) {
        return Math.max(8, attack - defense / 2);
    }

    private static int calculateEnemyDamage(int attack, int defense, RoomType roomType) {
        int damage = calculateDamage(attack, defense);

        if (roomType == RoomType.BATTLE) {
            return Math.max(5, damage / 2 + 2);
        }

        if (roomType == RoomType.BOSS) {
            return Math.max(7, damage - 4);
        }

        return damage;
    }

    private static int getCollisionDamage(Random random, RoomType roomType) {
        if (roomType == RoomType.BATTLE) {
            return 6 + random.nextInt(10);
        }

        if (roomType == RoomType.BOSS) {
            return 10 + random.nextInt(14);
        }

        return 14 + random.nextInt(16);
    }

    private static void balanceEnemyForRoom(Room room) {
        if (room.getType() == RoomType.BATTLE && room.getBotProfile().getHealth() > 75) {
            room.getBotProfile().damageBot(room.getBotProfile().getHealth() - 75);
        }
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

    private static boolean isMoveInput(String input) {
        return input.equals("1") || input.equals("m") || input.equals("move");
    }

    private static boolean isAttackInput(String input) {
        return input.equals("2") || input.equals("f") || input.equals("attack");
    }

    private static int parseDirection(String input) {
        if (input.equals("a") || input.equals("left") || input.equals("1")) {
            return 1;
        }

        if (input.equals("d") || input.equals("right") || input.equals("2")) {
            return 2;
        }

        if (input.equals("w") || input.equals("up") || input.equals("3")) {
            return 3;
        }

        if (input.equals("s") || input.equals("down") || input.equals("4")) {
            return 4;
        }

        return 0;
    }

    private static int getMaxMoveAmount(int[] pos, int direction, int maxMoveAmount) {
        if (direction == 1) {
            return Math.min(maxMoveAmount, pos[0]);
        }

        if (direction == 2) {
            return Math.min(maxMoveAmount, FIELD_SIZE - 1 - pos[0]);
        }

        if (direction == 3) {
            return Math.min(maxMoveAmount, pos[1]);
        }

        if (direction == 4) {
            return Math.min(maxMoveAmount, FIELD_SIZE - 1 - pos[1]);
        }

        return 0;
    }

    private static boolean isValidMoveAmount(String input, int maxMoveAmount) {
        try {
            int moveAmount = Integer.parseInt(input);
            return moveAmount >= 1 && moveAmount <= maxMoveAmount;
        } catch (NumberFormatException e) {
            return false;
        }
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

    private static String getPlural(int amount) {
        if (amount == 1) {
            return "";
        }

        return "s";
    }
}
