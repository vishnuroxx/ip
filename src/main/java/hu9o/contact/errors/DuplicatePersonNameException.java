package hu9o.contact.errors;

import hu9o.errors.Hu9oException;

/** Indicates that a person command's name matches someone already in the network. */
public class DuplicatePersonNameException extends Hu9oException {
    public DuplicatePersonNameException() {
        super("A person with that name already exists. Use a different name, or delete the existing one first.");
    }
}
