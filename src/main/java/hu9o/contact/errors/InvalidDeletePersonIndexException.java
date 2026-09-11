package hu9o.contact.errors;

import hu9o.errors.Hu9oException;

/** Indicates that a deleteperson command's index does not name an existing person. */
public class InvalidDeletePersonIndexException extends Hu9oException {
    public InvalidDeletePersonIndexException() {
        super("Invalid index for deleting. Check the list of people using \"people\"");
    }
}
