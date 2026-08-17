package errors;
/** Indicates that mark was given an invalid task number. */
public class InvalidMarkIndexException extends Hu9oException {
    public InvalidMarkIndexException() { super("Invalid index. Check the list of items using \"list\""); }
}