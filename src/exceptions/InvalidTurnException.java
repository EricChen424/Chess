package exceptions;

/**
 * Created by Eric on 12/31/2015.
 */
public class InvalidTurnException extends Throwable {
    public InvalidTurnException(){
        super();
    }

    public InvalidTurnException(String s) {
        super(s);
    }
}
