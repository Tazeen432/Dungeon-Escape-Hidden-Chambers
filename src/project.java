import java.util.*;

class Room {

    String name;
    boolean visited;

    HashMap<String, Room> connectedRooms;

    public Room(String name) {
        this.name = name;
        visited = false;
        connectedRooms = new HashMap<>();
    }

    public void connectRoom(String direction, Room room) {
        connectedRooms.put(direction, room);
    }

    public void showDirections() {

        System.out.println("Available Directions:");

        for (String dir : connectedRooms.keySet()) {
            System.out.println("- " + dir);
        }
    }
}

class Player {

    String name;
    Room currentRoom;

    ArrayList<String> inventory;

    Stack<Room> moveHistory;

    public Player(String name, Room startRoom) {

        this.name = name;
        currentRoom = startRoom;

        inventory = new ArrayList<>();
        moveHistory = new Stack<>();
    }

    public void move(String direction) {

        if (currentRoom.connectedRooms.containsKey(direction)) {

            moveHistory.push(currentRoom);

            currentRoom = currentRoom.connectedRooms.get(direction);

            currentRoom.visited = true;

            System.out.println(" Moved to: " + currentRoom.name);

        } else {
            System.out.println(" Invalid Direction!");
        }
    }

    public void undoMove() {

        if (!moveHistory.isEmpty()) {

            currentRoom = moveHistory.pop();

            System.out.println(" Returned to: " + currentRoom.name);

        } else {
            System.out.println(" No previous moves!");
        }
    }

    public void showInventory() {

        System.out.println(" Inventory:");

        if (inventory.isEmpty()) {
            System.out.println("Empty");
        } else {
            for (String item : inventory) {
                System.out.println("- " + item);
            }
        }
    }
}

class EnemyEvent {

    String eventName;

    public EnemyEvent(String eventName) {
        this.eventName = eventName;
    }

    public void showEvent() {
        System.out.println(" Enemy Event: " + eventName);
    }
}

class Game {

    Player player;

    Queue<EnemyEvent> enemyQueue;

    HashMap<String, String> items;

    public void startGame() {

        // Create Rooms
        Room entrance = new Room("Dungeon Entrance");
        Room hall = new Room("Dark Hall");
        Room treasure = new Room("Treasure Room");
        Room trap = new Room("Trap Room");

        // Graph Connections
        entrance.connectRoom("north", hall);

        hall.connectRoom("south", entrance);
        hall.connectRoom("east", treasure);
        hall.connectRoom("west", trap);

        treasure.connectRoom("west", hall);
        trap.connectRoom("east", hall);

        // Create Player
        player = new Player("Hero", entrance);

        // HashMap for Items
        items = new HashMap<>();

        items.put("key", "Silver Key");
        items.put("potion", "Health Potion");

        // Queue for Enemy Events
        enemyQueue = new LinkedList<>();

        enemyQueue.add(new EnemyEvent("Zombie Appeared"));
        enemyQueue.add(new EnemyEvent("Skeleton Warrior Appeared"));

        entrance.visited = true;

        gameLoop();
    }

    public void gameLoop() {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println(" ==============================");
            System.out.println("DUNGEON ESCAPE: HIDDEN CHAMBERS");
            System.out.println("==============================");

            System.out.println(" Current Room: " + player.currentRoom.name);

            player.currentRoom.showDirections();

            System.out.println(" 1. Move");
            System.out.println("2. Show Inventory");
            System.out.println("3. Pick Item");
            System.out.println("4. Undo Move");
            System.out.println("5. Enemy Event");
            System.out.println("6. Exit Game");

            System.out.print(" Enter Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Direction: ");
                    String dir = sc.nextLine().toLowerCase();

                    player.move(dir);
                    break;

                case 2:

                    player.showInventory();
                    break;

                case 3:

                    System.out.println(" Items Available:");

                    for (String key : items.keySet()) {
                        System.out.println("- " + key);
                    }

                    System.out.print("Pick Item: ");
                    String itemKey = sc.nextLine();

                    if (items.containsKey(itemKey)) {

                        String item = items.get(itemKey);

                        player.inventory.add(item);

                        System.out.println(item + " added to inventory!");

                    } else {
                        System.out.println("Item not found!");
                    }

                    break;

                case 4:

                    player.undoMove();
                    break;

                case 5:

                    if (!enemyQueue.isEmpty()) {

                        EnemyEvent e = enemyQueue.poll();
                        e.showEvent();

                    } else {
                        System.out.println(" No enemy events left!");
                    }

                    break;

                case 6:

                    System.out.println(" Game Exited!");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}

public class project {

    public static void main(String[] args) {

        Game game = new Game();
        game.startGame();
    }
}

