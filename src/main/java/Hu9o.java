import java.util.Scanner;
import java.util.ArrayList;

/**
 * A simple command-line chatbot that echoes user commands.
 */
public class Hu9o {

    // Task Manager
    public static ArrayList<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        String banner = " _   _           ___ \n"
                + "| | | | | | | | / _ \\   ___  \n"
                + "| |_| | | | | || (_) | / _ \\ \n"
                + "|  _  | | |_| | \\__,| | (_) |\n"
                + "|_| |_|  \\___/   /_/   \\___/ \n";

        // ChatBot Conversation
        System.out.println("_________________________________");
        System.out.print(banner);
        System.out.println("_________________________________\n");

        System.out.println("Woof! I'm Hu9o!");
        System.out.println("What can I do for you?\n");

        try (Scanner scanner = new Scanner(System.in)) {
            for (;;) {
                System.out.print("> ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("bye")) {
                    break;
                }

                answerHandler(command);
            }
        }
        System.out.println("_________________________________");
        System.out.println("\nBye. Hope to see you again soon! (wags tail)");
        System.out.println("_________________________________\n");
    }

    /**
     * Echoes a command with blank lines around the response.
     *
     * @param command the command entered by the user
     */
    private static void answerHandler(String command) {
        System.out.println("_________________________________\n");
        switch (command.toLowerCase()) {
            case "list":
                int index = 1;
                for (String task : tasks) {
                    System.out.println(index + "." + " " + task);
                    index++;
                }
                break;
            default:
                tasks.add(command);
                System.out.println("added: " + command);
                break;
        }
        System.out.println("_________________________________\n");

    }
}
