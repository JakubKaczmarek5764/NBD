package exceptions;

public class WeightExceededException extends RuntimeException {
    public WeightExceededException() {
    }

    public WeightExceededException(String message) {
        super(message);
    }
}
