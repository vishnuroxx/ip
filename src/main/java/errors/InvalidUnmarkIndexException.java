package errors;
/** Indicates that unmark was given an invalid task number. */
public class InvalidUnmarkIndexException extends Hu9oException {
    public InvalidUnmarkIndexException() { super("Invalid index. Check the list of items using \"list\""); }
}