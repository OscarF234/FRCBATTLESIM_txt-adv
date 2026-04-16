package Rooms;

import Bot.BotProfile;
import Bot.BotProfiles;
import Bot.Item;
import Bot.Items;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// vibe coded because do not want to deal with graph theory
public class RoomGenerator {

    private static final int GRID_SIZE = 5;
    private static final int TOTAL_ROOMS = GRID_SIZE * GRID_SIZE;
    private static final int MIN_PATH_LENGTH = 10;
    private static final int MAX_PATH_LENGTH = 14;
    private static final int MIN_ITEM_ROOMS = 5;
    private static final int BOSS_ROOM_COUNT = 5;
    private static final double ITEM_ROOM_CHANCE = 0.15;
    private static final double SECOND_PARENT_CHANCE = 0.10;
    private static final int MIN_EXTRA_CONNECTIONS = 1;
    private static final int MAX_EXTRA_CONNECTIONS = 2;
    private static final Item[] ITEM_POOL = Items.ALL_ITEMS;

    public Room[] generateRoomsFromSeed(int seed) {
        return generateRooms(new Random(seed));
    }

    public Room[] generateRooms(Random random) {
        List<List<Integer>> connections = createEmptyGraph();
        List<Integer> mainPath = buildMainPath(random);

        connectMainPath(connections, mainPath);
        attachOptionalBranches(connections, mainPath, random);
        addExtraConnections(connections, random);

        RoomType[] roomTypes = assignRoomTypes(mainPath, random);
        BotProfile[] roomBots = assignBotProfiles(roomTypes, random);
        String[] roomIds = buildRoomIds(roomTypes);
        Room[] rooms = buildRooms(connections, roomTypes, roomBots, roomIds, random);

        rooms[0].setUnlocked(true);

        if (!hasPath(connections, 0, TOTAL_ROOMS - 1)) {
            throw new IllegalStateException("Generated map does not connect the first room to the last room.");
        }

        return rooms;
    }

    private List<List<Integer>> createEmptyGraph() {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < TOTAL_ROOMS; i++) {
            graph.add(new ArrayList<>());
        }

        return graph;
    }

    private List<Integer> buildMainPath(Random random) {
        List<Integer> allowedPathLengths = getAllowedPathLengths();

        for (int attempt = 0; attempt < 200; attempt++) {
            int targetPathLength = allowedPathLengths.get(random.nextInt(allowedPathLengths.size()));
            List<Integer> mainPath = new ArrayList<>();
            boolean[] visited = new boolean[TOTAL_ROOMS];

            mainPath.add(0);
            visited[0] = true;

            if (buildPathDepthFirst(0, targetPathLength, visited, mainPath, random)) {
                return mainPath;
            }
        }

        return buildFallbackMainPath();
    }

    private List<Integer> getAllowedPathLengths() {
        List<Integer> allowedPathLengths = new ArrayList<>();
        int minimumPathLength = manhattanDistance(0, TOTAL_ROOMS - 1) + 1;

        for (int pathLength = MIN_PATH_LENGTH; pathLength <= MAX_PATH_LENGTH; pathLength++) {
            if (pathLength < minimumPathLength) {
                continue;
            }

            if ((pathLength - minimumPathLength) % 2 == 0) {
                allowedPathLengths.add(pathLength);
            }
        }

        if (allowedPathLengths.isEmpty()) {
            throw new IllegalStateException("No valid main-path lengths fit the current grid settings.");
        }

        return allowedPathLengths;
    }

    private boolean buildPathDepthFirst(int currentRoom, int targetPathLength, boolean[] visited, List<Integer> currentPath, Random random) {
        if (currentRoom == TOTAL_ROOMS - 1) {
            return currentPath.size() == targetPathLength;
        }

        if (currentPath.size() >= targetPathLength) {
            return false;
        }

        List<Integer> neighbors = getGridNeighbors(currentRoom);
        Collections.shuffle(neighbors, random);

        for (int nextRoom : neighbors) {
            if (visited[nextRoom]) {
                continue;
            }

            int nextPathLength = currentPath.size() + 1;

            if (!canStillReachEnd(nextRoom, nextPathLength, targetPathLength)) {
                continue;
            }

            visited[nextRoom] = true;
            currentPath.add(nextRoom);

            if (buildPathDepthFirst(nextRoom, targetPathLength, visited, currentPath, random)) {
                return true;
            }

            currentPath.remove(currentPath.size() - 1);
            visited[nextRoom] = false;
        }

        return false;
    }

    private boolean canStillReachEnd(int roomIndex, int currentPathLength, int targetPathLength) {
        int distanceToEnd = manhattanDistance(roomIndex, TOTAL_ROOMS - 1);

        if (currentPathLength + distanceToEnd > targetPathLength) {
            return false;
        }

        return (targetPathLength - currentPathLength - distanceToEnd) % 2 == 0;
    }

    private List<Integer> buildFallbackMainPath() {
        List<Integer> fallbackPath = new ArrayList<>();

        fallbackPath.add(0);
        fallbackPath.add(1);
        fallbackPath.add(2);
        fallbackPath.add(3);
        fallbackPath.add(8);
        fallbackPath.add(7);
        fallbackPath.add(6);
        fallbackPath.add(11);
        fallbackPath.add(12);
        fallbackPath.add(13);
        fallbackPath.add(18);
        fallbackPath.add(19);
        fallbackPath.add(24);

        return fallbackPath;
    }

    private void connectMainPath(List<List<Integer>> connections, List<Integer> mainPath) {
        for (int i = 0; i < mainPath.size() - 1; i++) {
            connectRooms(connections, mainPath.get(i), mainPath.get(i + 1));
        }
    }

    private void attachOptionalBranches(List<List<Integer>> connections, List<Integer> mainPath, Random random) {
        Set<Integer> connectedRooms = new HashSet<>(mainPath);
        List<Integer> remainingRooms = new ArrayList<>();

        for (int roomIndex = 0; roomIndex < TOTAL_ROOMS; roomIndex++) {
            if (!connectedRooms.contains(roomIndex)) {
                remainingRooms.add(roomIndex);
            }
        }

        while (!remainingRooms.isEmpty()) {
            Collections.shuffle(remainingRooms, random);
            boolean attachedRoom = false;

            for (int i = 0; i < remainingRooms.size(); i++) {
                int roomIndex = remainingRooms.get(i);
                List<Integer> parentCandidates = getConnectedNeighbors(roomIndex, connectedRooms);

                if (parentCandidates.isEmpty()) {
                    continue;
                }

                int parentRoom = parentCandidates.get(random.nextInt(parentCandidates.size()));
                connectRooms(connections, parentRoom, roomIndex);

                if (random.nextDouble() < SECOND_PARENT_CHANCE) {
                    List<Integer> secondParentCandidates = new ArrayList<>(parentCandidates);
                    secondParentCandidates.remove(Integer.valueOf(parentRoom));

                    if (!secondParentCandidates.isEmpty()) {
                        int secondParent = secondParentCandidates.get(random.nextInt(secondParentCandidates.size()));
                        connectRooms(connections, secondParent, roomIndex);
                    }
                }

                connectedRooms.add(roomIndex);
                remainingRooms.remove(i);
                attachedRoom = true;
                break;
            }

            if (!attachedRoom) {
                throw new IllegalStateException("Could not attach every room to the 5x5 grid.");
            }
        }
    }

    private List<Integer> getConnectedNeighbors(int roomIndex, Set<Integer> connectedRooms) {
        List<Integer> connectedNeighbors = new ArrayList<>();

        for (int neighbor : getGridNeighbors(roomIndex)) {
            if (connectedRooms.contains(neighbor)) {
                connectedNeighbors.add(neighbor);
            }
        }

        return connectedNeighbors;
    }

    private void addExtraConnections(List<List<Integer>> connections, Random random) {
        int extraConnections = MIN_EXTRA_CONNECTIONS + random.nextInt(MAX_EXTRA_CONNECTIONS - MIN_EXTRA_CONNECTIONS + 1);
        List<int[]> availableEdges = new ArrayList<>();

        for (int roomIndex = 0; roomIndex < TOTAL_ROOMS; roomIndex++) {
            for (int neighbor : getGridNeighbors(roomIndex)) {
                if (roomIndex < neighbor && !connections.get(roomIndex).contains(neighbor)) {
                    availableEdges.add(new int[] { roomIndex, neighbor });
                }
            }
        }

        Collections.shuffle(availableEdges, random);

        for (int i = 0; i < extraConnections && i < availableEdges.size(); i++) {
            int[] edge = availableEdges.get(i);
            connectRooms(connections, edge[0], edge[1]);
        }
    }

    private void connectRooms(List<List<Integer>> connections, int firstRoom, int secondRoom) {
        if (!areGridNeighbors(firstRoom, secondRoom)) {
            throw new IllegalArgumentException("Rooms can only connect to neighboring cells in the 5x5 grid.");
        }

        if (!connections.get(firstRoom).contains(secondRoom)) {
            connections.get(firstRoom).add(secondRoom);
        }

        if (!connections.get(secondRoom).contains(firstRoom)) {
            connections.get(secondRoom).add(firstRoom);
        }
    }

    private RoomType[] assignRoomTypes(List<Integer> mainPath, Random random) {
        RoomType[] roomTypes = new RoomType[TOTAL_ROOMS];
        List<Integer> bossRooms = getBossRooms(mainPath);

        for (int i = 0; i < TOTAL_ROOMS; i++) {
            roomTypes[i] = RoomType.BATTLE;
        }

        roomTypes[mainPath.get(mainPath.size() - 1)] = RoomType.FINAL_BOSS;

        for (int bossRoom : bossRooms) {
            roomTypes[bossRoom] = RoomType.BOSS;
        }

        setItemRoom(roomTypes, mainPath.get(mainPath.size() / 3));
        setItemRoom(roomTypes, mainPath.get((mainPath.size() * 2) / 3));
        setItemRoom(roomTypes, findNearestBattleRoom(mainPath, roomTypes, mainPath.size() - 3));

        for (int i = 1; i < TOTAL_ROOMS - 1; i++) {
            if (roomTypes[i] == RoomType.BATTLE && random.nextDouble() < ITEM_ROOM_CHANCE) {
                roomTypes[i] = RoomType.ITEM;
            }
        }

        ensureMinimumItemRooms(roomTypes, random);
        roomTypes[0] = RoomType.START;
        return roomTypes;
    }

    private BotProfile[] assignBotProfiles(RoomType[] roomTypes, Random random) {
        BotProfile[] roomBots = new BotProfile[TOTAL_ROOMS];
        List<BotProfile> regularProfiles = new ArrayList<>(Arrays.asList(BotProfiles.REGULAR_PROFILES));
        List<BotProfile> bossProfiles = new ArrayList<>(Arrays.asList(BotProfiles.BOSS_PROFILES));
        List<BotProfile> finalBossProfiles = new ArrayList<>(Arrays.asList(BotProfiles.FINAL_BOSS_PROFILES));
        int regularIndex = 0;
        int bossIndex = 0;

        Collections.shuffle(regularProfiles, random);
        Collections.shuffle(bossProfiles, random);
        Collections.shuffle(finalBossProfiles, random);

        for (int roomIndex = 0; roomIndex < TOTAL_ROOMS; roomIndex++) {
            if (roomTypes[roomIndex] == RoomType.START) {
                continue;
            }

            if (roomTypes[roomIndex] == RoomType.FINAL_BOSS) {
                roomBots[roomIndex] = finalBossProfiles.get(0);
                continue;
            }

            if (roomTypes[roomIndex] == RoomType.BOSS) {
                roomBots[roomIndex] = bossProfiles.get(bossIndex);
                bossIndex++;
                continue;
            }

            roomBots[roomIndex] = regularProfiles.get(regularIndex);
            regularIndex++;
        }

        return roomBots;
    }

    private List<Integer> getBossRooms(List<Integer> mainPath) {
        LinkedHashSet<Integer> bossRooms = new LinkedHashSet<>();

        for (int i = 1; i <= BOSS_ROOM_COUNT; i++) {
            int bossIndex = (i * mainPath.size()) / (BOSS_ROOM_COUNT + 1);

            if (bossIndex >= mainPath.size() - 1) {
                bossIndex = mainPath.size() - 2;
            }

            if (bossIndex < 1) {
                bossIndex = 1;
            }

            bossRooms.add(mainPath.get(bossIndex));
        }

        if (bossRooms.size() < BOSS_ROOM_COUNT) {
            for (int i = 1; i < mainPath.size() - 1 && bossRooms.size() < BOSS_ROOM_COUNT; i++) {
                bossRooms.add(mainPath.get(i));
            }
        }

        return new ArrayList<>(bossRooms);
    }

    private void setItemRoom(RoomType[] roomTypes, int roomIndex) {
        if (roomTypes[roomIndex] == RoomType.BATTLE) {
            roomTypes[roomIndex] = RoomType.ITEM;
        }
    }

    private int findNearestBattleRoom(List<Integer> mainPath, RoomType[] roomTypes, int preferredIndex) {
        for (int offset = 0; offset < mainPath.size(); offset++) {
            int leftIndex = preferredIndex - offset;

            if (leftIndex >= 1 && leftIndex < mainPath.size() - 1 && roomTypes[mainPath.get(leftIndex)] == RoomType.BATTLE) {
                return mainPath.get(leftIndex);
            }

            int rightIndex = preferredIndex + offset;

            if (rightIndex >= 1 && rightIndex < mainPath.size() - 1 && roomTypes[mainPath.get(rightIndex)] == RoomType.BATTLE) {
                return mainPath.get(rightIndex);
            }
        }

        return mainPath.get(1);
    }

    private void ensureMinimumItemRooms(RoomType[] roomTypes, Random random) {
        while (countRoomsOfType(roomTypes, RoomType.ITEM) < MIN_ITEM_ROOMS) {
            int roomIndex = 1 + random.nextInt(TOTAL_ROOMS - 2);

            if (roomTypes[roomIndex] == RoomType.BATTLE) {
                roomTypes[roomIndex] = RoomType.ITEM;
            }
        }
    }

    private int countRoomsOfType(RoomType[] roomTypes, RoomType wantedType) {
        int count = 0;

        for (RoomType roomType : roomTypes) {
            if (roomType == wantedType) {
                count++;
            }
        }

        return count;
    }

    private String[] buildRoomIds(RoomType[] roomTypes) {
        String[] roomIds = new String[TOTAL_ROOMS];

        for (int i = 0; i < TOTAL_ROOMS; i++) {
            roomIds[i] = "room_" + i;
        }

        return roomIds;
    }

    private Room[] buildRooms(List<List<Integer>> connections, RoomType[] roomTypes, BotProfile[] roomBots, String[] roomIds, Random random) {
        Room[] rooms = new Room[TOTAL_ROOMS];

        for (int roomIndex = 0; roomIndex < TOTAL_ROOMS; roomIndex++) {
            rooms[roomIndex] = new Room(
                roomIds[roomIndex],
                roomTypes[roomIndex],
                buildConnectedRoomIds(connections.get(roomIndex), roomIds),
                buildItems(roomTypes[roomIndex], roomIndex, random),
                roomBots[roomIndex]
            );
        }

        return rooms;
    }

    private String[] buildConnectedRoomIds(List<Integer> roomConnections, String[] roomIds) {
        String[] connectedRoomIds = new String[roomConnections.size()];

        for (int i = 0; i < roomConnections.size(); i++) {
            connectedRoomIds[i] = roomIds[roomConnections.get(i)];
        }

        return connectedRoomIds;
    }

    private Item[] buildItems(RoomType roomType, int roomIndex, Random random) {
        if (roomType != RoomType.ITEM) {
            return new Item[0];
        }

        int itemCount = 3;
        Item[] items = new Item[itemCount];

        for (int i = 0; i < itemCount; i++) {
            Item template = ITEM_POOL[random.nextInt(ITEM_POOL.length)];
            items[i] = new Item(template.getItemId(), template.getSubsystem(), template.getMag(), template.getRange(), template.getPrice());
        }

        return items;
    }

    private boolean hasPath(List<List<Integer>> connections, int startRoom, int endRoom) {
        boolean[] visited = new boolean[TOTAL_ROOMS];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        visited[startRoom] = true;
        queue.add(startRoom);

        while (!queue.isEmpty()) {
            int currentRoom = queue.remove();

            if (currentRoom == endRoom) {
                return true;
            }

            for (int nextRoom : connections.get(currentRoom)) {
                if (!visited[nextRoom]) {
                    visited[nextRoom] = true;
                    queue.add(nextRoom);
                }
            }
        }

        return false;
    }

    private List<Integer> getGridNeighbors(int roomIndex) {
        List<Integer> neighbors = new ArrayList<>();
        int row = roomIndex / GRID_SIZE;
        int column = roomIndex % GRID_SIZE;

        if (row > 0) {
            neighbors.add(roomIndex - GRID_SIZE);
        }

        if (row < GRID_SIZE - 1) {
            neighbors.add(roomIndex + GRID_SIZE);
        }

        if (column > 0) {
            neighbors.add(roomIndex - 1);
        }

        if (column < GRID_SIZE - 1) {
            neighbors.add(roomIndex + 1);
        }

        return neighbors;
    }

    private boolean areGridNeighbors(int firstRoom, int secondRoom) {
        return getGridNeighbors(firstRoom).contains(secondRoom);
    }

    private int manhattanDistance(int firstRoom, int secondRoom) {
        int firstRow = firstRoom / GRID_SIZE;
        int firstColumn = firstRoom % GRID_SIZE;
        int secondRow = secondRoom / GRID_SIZE;
        int secondColumn = secondRoom % GRID_SIZE;

        return Math.abs(firstRow - secondRow) + Math.abs(firstColumn - secondColumn);
    }
}
