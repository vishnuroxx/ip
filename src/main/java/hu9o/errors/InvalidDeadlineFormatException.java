package hu9o.errors;
/** Indicates that a deadline command is missing its description or /by date. */
public class InvalidDeadlineFormatException extends Hu9oException {
    public InvalidDeadlineFormatException() { super("Invalid deadline format. Use: deadline DESCRIPTION /by DATE"); }
}