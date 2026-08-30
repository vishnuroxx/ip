package errors;

public class InvalidDeleteIndexException extends Hu9oException {
    public InvalidDeleteIndexException() {
        super("Invalid index for deleting. Check the list of items using \"list\"");
    }

}
