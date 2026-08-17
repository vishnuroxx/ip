package errors;
/** Indicates that a todo command has no description. */
public class InvalidTodoFormatException extends Hu9oException {
    public InvalidTodoFormatException() { super("Invalid todo format. Use: todo DESCRIPTION"); }
}