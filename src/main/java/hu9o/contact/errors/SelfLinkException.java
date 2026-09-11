package hu9o.contact.errors;

import hu9o.errors.Hu9oException;

/** Indicates that a link command named the same person twice. */
public class SelfLinkException extends Hu9oException {
    public SelfLinkException() {
        super("Cannot link a person to themselves.");
    }
}
