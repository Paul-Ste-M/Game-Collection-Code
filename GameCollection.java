/*
 * Paul Stevens
 */

import java.io.*;
import java.util.Scanner;

class Game {
    String title;
    String console;

    Game(String title, String console) {
        this.title = title;
        this.console = console;
    }
// Format for video game search
    public String toString() {
        return title + "\t" + console;
    }
}

class Node<T> {
    T data;
    Node<T> next;

    Node(T data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList<T> {
    Node<T> head;

    public LinkedList() {
        head = null;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public void clear() {
        head = null;
    }

    public LinkedList<T> search(String titleKeyword, String consoleKeyword) {
        LinkedList<T> results = new LinkedList<>();
        Node<T> temp = head;
        while (temp != null) {
            Game game = (Game) temp.data;
            if ((titleKeyword.equals("*") || game.title.toLowerCase().contains(titleKeyword.toLowerCase())) &&
                (consoleKeyword.equals("*") || game.console.toLowerCase().contains(consoleKeyword.toLowerCase()))) {
                results.add(temp.data);
            }
            temp = temp.next;
        }
        return results;
    }

    public void printToConsole() {
        Node<T> temp = head;
        if (temp == null) {
            System.out.println("No games found.");
        } else {
            while (temp != null) {
                System.out.println(temp.data);
                temp = temp.next;
            }
        }
    }
// Prints The Searched Result To A Flle
    public void printToFile(String filename, boolean append) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, append))) {
            Node<T> temp = head;
            while (temp != null) {
                pw.println(temp.data);
                temp = temp.next;
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
// Prompts the User For Different Options Based On Keyboard Input
public class GameCollection {
    static LinkedList<Game> gameList = new LinkedList<>();
    static Scanner scanner = new Scanner(System.in);
   
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n-- Video Game Database --");
            System.out.println("1. Load a game collection file");
            System.out.println("2. Search games");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    loadFile();
                    break;
                case 2:
                    searchGames();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Load the video game collection file and populate the list
    public static void loadFile() {
        System.out.print("Enter the game collection file name: ");
        String filename = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            gameList.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] gameData = line.split("\t");
                if (gameData.length == 2) {
                    gameList.add(new Game(gameData[0], gameData[1]));
                }
            }
            System.out.println("Game collection loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // Search games by title or console with wildcard support
    public static void searchGames() {
        System.out.print("Enter game title (or * for all): ");
        String titleKeyword = scanner.nextLine();
        System.out.print("Enter game console (or * for all): ");
        String consoleKeyword = scanner.nextLine();

        LinkedList<Game> searchResults = gameList.search(titleKeyword, consoleKeyword);

        System.out.println("Search results:");
        searchResults.printToConsole();

        System.out.print("Do you want to save the results to a file? (y/n): ");
        String saveToFile = scanner.nextLine();
        if (saveToFile.equalsIgnoreCase("y")) {
            System.out.print("Enter file name: ");
            String filename = scanner.nextLine();
            System.out.print("Append to file? (y/n): ");
            boolean append = scanner.nextLine().equalsIgnoreCase("y");
            searchResults.printToFile(filename, append);
            System.out.println("Results saved to " + filename);
            
       
        }
    }
}
