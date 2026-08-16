import java.util.Scanner;

/**
 * A simple command-line chatbot that echoes user commands.
 */
public class Hu9o {
    public static void main(String[] args) {
        String banner = " _   _           ___ \n"
                + "| | | | | | | | / _ \\   ___  \n"
                + "| |_| | | | | || (_) | / _ \\ \n"
                + "|  _  | | |_| | \\__,| | (_) |\n"
                + "|_| |_|  \\___/   /_/   \\___/ \n";
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
        System.out.println("_________________________________");
        System.out.println("\n" + command);
        System.out.println("_________________________________\n");
    }
}
