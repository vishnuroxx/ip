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
     * Adds a task to the list and displays the updated task count.
     *
     * @param task the task created from the user's command
     */
    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:\n\t" + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");

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
            case "todo":
                // A todo command has the form: todo DESCRIPTION. Everything
                // after the first space is kept as the task description.
                addTask(new ToDoTask(str.substring(str.indexOf(" ") + 1)));
                break;

            case "deadline":
                try {
                    // A deadline command has the form: deadline DESCRIPTION
                    // /by DATE. The slash separates the description from the
                    // deadline rule.
                    int start_index = str.indexOf(" ") + 1;
                    int end_index = str.indexOf("/");
                    String description = str.substring(start_index, end_index);

                    // Remove the description so the remaining text starts at
                    // the rule keyword (which should be "by").
                    str = str.substring(end_index + 1);
                    String rule = str.substring(0, str.indexOf(" "));
                    if (!rule.equals("by")) {
                        System.out.println(rule);
                        throw new Exception("Invalid command: use /by");
                    }

                    // The text after "by" is the deadline specification.
                    String deadline = str.substring(str.indexOf(" ") + 1);
                    addTask(new DeadlineTask(description, deadline));

                } catch (Exception e) {
                    System.out.println("Invalid Format for Event task. Use TASK /from TIME /to TIME");
                }
                break;
            case "event":
                try {
                    // An event command has the form:
                    // event DESCRIPTION /from START /to END.
                    int start_index = str.indexOf(" ") + 1;
                    int end_index = str.indexOf("/");
                    String description = str.substring(start_index, end_index).trim();

                    // Parse the first rule and retain the time until the next
                    // slash, which separates the /from and /to sections.
                    str = str.substring(end_index + 1);
                    String rule_1 = str.substring(0, str.indexOf(" "));
                    str = str.substring(str.indexOf(" ") + 1);
                    String fromExpression = str.substring(0, str.indexOf("/")).trim();

                    // Parse the second rule and treat the remaining text as
                    // the event's ending time.
                    str = str.substring(str.indexOf("/") + 1);
                    String rule_2 = str.substring(0, str.indexOf(" "));
                    String toExpresion = str.substring(str.indexOf(" ") + 1);

                    // Both keywords must be present in the correct order.
                    if (!(rule_1.equals("from") && rule_2.equals("to"))) {
                        System.out.println(rule_1 + rule_2);
                        throw new Exception("Invalid command: use /by");
                    }
                    addTask(new EventTask(description, fromExpression, toExpresion));

                } catch (Exception e) {
                    System.out.println("Invalid Format for Deadline task. Use TASK /by DAY");
                }
                break;
            default:
                // Commands must begin with a supported keyword such as
                // todo, deadline, event, list, mark, or unmark.
                System.out.println("Unknown command");

        }
        System.out.println("_________________________________\n");

    }
}
