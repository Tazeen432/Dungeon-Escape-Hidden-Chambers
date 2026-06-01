import java.util.*;

class Room {
    String name;
    String type;
    boolean visited = false;
    HashMap<String, Room> neighbors = new HashMap<>();

    Room(String name, String type) {
        this.name = name;
        this.type = type;
    }

    void connect(String dir, Room room) {
        neighbors.put(dir, room);
    }
}

class Enemy {
    String name;
    int health;
    int damage;

    Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }
}
class SkillNode {
    String skill;
    SkillNode left, right;

    SkillNode(String skill) {
        this.skill = skill;
    }
}
class BSTNode {
    int score;
    BSTNode left, right;

    BSTNode(int score) {
        this.score = score;
    }
}
class bst {
    BSTNode root;

    void insert(int score) {
        root = insertRec(root, score);
    }

    BSTNode insertRec(BSTNode node, int score) {
        if (node == null) return new BSTNode(score);

        if (score < node.score)
            node.left = insertRec(node.left, score);
        else
            node.right = insertRec(node.right, score);

        return node;
    }
        void display() {
            System.out.println("\nLeaderboard Scores:");
            inorder(root);
        }

        void inorder(BSTNode node) {
            if (node == null) return;
            inorder(node.left);
            System.out.println(node.score);
            inorder(node.right);
        }


}
class Player {
    Room currentRoom;
    int health = 100;
    int score = 0;

    ArrayList<String> inventory = new ArrayList<>();
    LinkedList<String> movementHistory = new LinkedList<>();
    Stack<Room> undoStack = new Stack<>();
    Deque<String> recentRooms = new ArrayDeque<>();

    Player(Room start) {
        currentRoom = start;
    }
}


public class project {

    static Scanner sc = new Scanner(System.in);
    static bst leaderboard = new bst();
    public static void main(String[] args) {
        // Skill Tree
        SkillNode rootSkill = new SkillNode("Attack");
        rootSkill.left = new SkillNode("Sword Mastery");
        rootSkill.right = new SkillNode("Critical Strike");

        // Rooms (Graph)
        Room entrance = new Room("Entrance", "NORMAL");
        Room hall = new Room("Dark Hall", "NORMAL");
        Room trap = new Room("Trap Room", "TRAP");
        Room enemy = new Room("Monster Lair", "ENEMY");
        Room treasure = new Room("Treasure Chamber", "TREASURE");
        Room library = new Room("Ancient Library", "NORMAL");
        Room exit = new Room("Hidden Exit", "EXIT");

        entrance.connect("north", hall);

        hall.connect("south", entrance);
        hall.connect("east", trap);
        hall.connect("west", enemy);
        hall.connect("north", library);

        trap.connect("west", hall);

        enemy.connect("east", hall);
        enemy.connect("north", treasure);

        treasure.connect("south", enemy);

        library.connect("south", hall);
        library.connect("north", exit);

        Player player = new Player(entrance);
        entrance.visited = true;

        HashMap<String, String> itemMap = new HashMap<>();
        itemMap.put("key", "Silver Key");
        itemMap.put("potion", "Health Potion");

        Queue<Enemy> enemyQueue = new LinkedList<>();
        enemyQueue.add(new Enemy("Zombie", 30, 10));
        enemyQueue.add(new Enemy("Skeleton", 40, 12));

        System.out.println("=== DUNGEON ESCAPE: HIDDEN CHAMBERS ===");

        while (true) {

            if (player.health <= 0) {
                System.out.println("\nYou died!");
                leaderboard.insert(player.score);
                leaderboard.display();
                break;
            }

            System.out.println("\n--------------------------------");
            System.out.println("Current Room: " + player.currentRoom.name);
            System.out.println("Health: " + player.health);
            System.out.println("Score : " + player.score);

            // Memory Fog System
            System.out.println("\nVisible Directions:");
            for (String dir : player.currentRoom.neighbors.keySet()) {
                System.out.println("- " + dir);
            }

            System.out.println("""

1. Move
2. Inventory
3. Pick Item
4. Undo Move
5. Fight Enemy
6. Recall Recent Rooms
7. Show Skills
8. Show Leaderboard
9. Exit Game
""");

            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1 -> {
                    System.out.print("Direction: ");
                    String dir = sc.nextLine().toLowerCase();

                    if (player.currentRoom.neighbors.containsKey(dir)) {

                        player.undoStack.push(player.currentRoom);

                        player.movementHistory.add(player.currentRoom.name);

                        player.recentRooms.addLast(player.currentRoom.name);
                        if (player.recentRooms.size() > 5)
                            player.recentRooms.removeFirst();

                        player.currentRoom = player.currentRoom.neighbors.get(dir);
                        player.currentRoom.visited = true;

                        player.score += 10;

                        roomEvent(player);
                    } else {
                        System.out.println("Invalid direction!");
                    }
                }

                case 2 -> {
                    System.out.println("\nInventory:");
                    if (player.inventory.isEmpty())
                        System.out.println("Empty");
                    else
                        for (String item : player.inventory)
                            System.out.println("- " + item);
                }

                case 3 -> {
                    System.out.println("Available: key, potion");
                    String item = sc.nextLine();

                    if (itemMap.containsKey(item)) {
                        player.inventory.add(itemMap.get(item));
                        System.out.println("Added: " + itemMap.get(item));
                    }
                }

                case 4 -> {
                    if (!player.undoStack.isEmpty()) {
                        player.currentRoom = player.undoStack.pop();
                        System.out.println("Returned to " + player.currentRoom.name);
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                }

                case 5 -> {
                    if (!enemyQueue.isEmpty()) {
                        Enemy e = enemyQueue.peek();

                        while (e.health > 0 && player.health > 0) {
                            e.health -= 15;
                            System.out.println("You hit " + e.name);

                            if (e.health > 0) {
                                player.health -= e.damage;
                                System.out.println(e.name + " hit you.");
                            }
                        }

                        if (e.health <= 0) {
                            System.out.println(e.name + " defeated!");
                            player.score += 50;
                            enemyQueue.poll();
                        }
                    } else {
                        System.out.println("No enemies left.");
                    }
                }

                case 6 -> {
                    System.out.println("\nRecent Rooms (Deque):");
                    for (String r : player.recentRooms)
                        System.out.println(r);
                }

                case 7 -> {
                    System.out.println("\nSkill Tree:");
                    System.out.println("Attack");
                    System.out.println("|-- Sword Mastery");
                    System.out.println("|-- Critical Strike");
                }

                case 8 -> leaderboard.display();

                case 9 -> {
                    leaderboard.insert(player.score);
                    System.out.println("Game Saved to Leaderboard.");
                    leaderboard.display();
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void roomEvent(Player player) {

        switch (player.currentRoom.type) {

            case "TRAP" -> {
                System.out.println("Trap activated! -15 Health");
                player.health -= 15;
            }

            case "TREASURE" -> {
                System.out.println("Treasure found! +40 Score");
                player.score += 40;
            }

            case "ENEMY" -> {
                System.out.println("Enemy room discovered!");
            }

            case "EXIT" -> {
                System.out.println("\nYOU ESCAPED THE DUNGEON!");
                player.score += 100;
                leaderboard.insert(player.score);
                leaderboard.display();
                System.exit(0);
            }
        }
    }
}





