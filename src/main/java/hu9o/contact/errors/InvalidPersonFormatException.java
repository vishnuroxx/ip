package hu9o.contact.errors;

import hu9o.errors.Hu9oException;

/** Indicates that a person command has no name, phone, or email. */
public class InvalidPersonFormatException extends Hu9oException {
    public InvalidPersonFormatException() {
        super("Invalid person format. Use: person NAME /phone PHONE /email EMAIL [/dob DOB] [/notes NOTES]");
    }
}
