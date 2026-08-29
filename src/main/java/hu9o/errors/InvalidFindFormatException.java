package hu9o.errors;

/** Indicates that a find command is missing its keyword. */
public class InvalidFindFormatException extends Hu9oException {
    public InvalidFindFormatException() {
        super("Invalid find format. Use: find KEYWORD");
    }
}
