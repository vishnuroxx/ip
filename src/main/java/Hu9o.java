import java.util.Scanner;
import java.util.ArrayList;

/**
 * A simple command-line chatbot that manages a list of tasks.
 */
public class Hu9o {

    /** Stores all tasks entered during the current program run. */
    public static ArrayList<Task> tasks = new ArrayList<>();

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
                // Wait for one complete command from the user.
                System.out.print("> ");
                String command = scanner.nextLine();

                // "bye" is handled by the input loop so the chatbot can exit
                // without treating it as a task.
                if (command.equalsIgnoreCase("bye")) {
                    break;
                }

                // Pass every other command to the query dispatcher below.
                answerHandler(command);
            }
        }
        System.out.println("_________________________________");
        System.out.println("\nBye. Hope to see you again soon! (wags tail)");
        System.out.println("_________________________________\n");
    }

    /**
     * Identifies the requested operation and performs it on the task list.
     * The first word is treated as the operation (for example, "list" or
     * "mark"), while later words provide arguments such as a task index.
     *
     * @param str the complete command entered by the user
     */
    private static void answerHandler(String str) {
        // Split the input so that commands such as "mark 2" can be handled
        // separately from their arguments.
        String[] parts = str.trim().split("\\s+");
        String command = parts[0];
        System.out.println("_________________________________\n");

        // Dispatch the query based on its first word.
        switch (command.toLowerCase()) {
            case "list":
                // Display every task using its one-based position for users.
                int index = 1;
                for (Task task : tasks) {
                    System.out.println(index + "." + " " + task);
                    index++;
                }
                break;
            case "mark":
                // Convert the user-facing task number to an ArrayList index
                // and mark that task as completed.
                try {
                    Task task = tasks.get(Integer.parseInt(parts[1]) - 1);
                    task.mark();
                    System.out.println("Nice! I've marked this task as done:\n\t" + task);

                } catch (Exception e) {
                    System.out.println("Invalid Index");
                }
                break;
            case "unmark":
                // Convert the task number and mark that task as incomplete.
                try {
                    Task task = tasks.get(Integer.parseInt(parts[1]) - 1);
                    task.unmark();
                    System.out.println("Ok, I've marked this task as not done yet:\n\t" + task);

                } catch (Exception e) {
                    System.out.println("Invalid index");
                }
                break;
            default:
                // Any command that is not a supported keyword becomes a new
                // unfinished task, preserving the complete original input.
                tasks.add(new Task(str));
                System.out.println("added: " + str);
                break;
        }
        System.out.println("_________________________________\n");

    }
}
