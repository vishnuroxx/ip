package errors;
/** Indicates that the command name is not supported. */
public class InvalidCommandException extends Hu9oException {
    public InvalidCommandException() { super("Unknown command. Use todo, list, deadline, event, mark, unmark or bye"); }
}