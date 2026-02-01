package sri.karthikeya.caterers.exception.custom;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
