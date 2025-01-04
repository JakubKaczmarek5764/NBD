package exceptions;

public class LiteratureBorrowedException extends RuntimeException {
    public LiteratureBorrowedException() {
    }

    public LiteratureBorrowedException(String message) {
        super(message);
    }
}
