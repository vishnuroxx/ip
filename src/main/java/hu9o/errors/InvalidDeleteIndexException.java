package hu9o.errors;

/** Indicates that delete was given an invalid task number. */
public class InvalidDeleteIndexException extends Hu9oException {
    public InvalidDeleteIndexException() {
        super("Invalid index for deleting. Check the list of items using \"list\"");
    }
}
