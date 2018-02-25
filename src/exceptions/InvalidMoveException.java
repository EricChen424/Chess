package exceptions;

/**
 * Created by Eric on 12/22/2015.
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(){
        super();
    }

    public InvalidMoveException(String msg){
        super(msg);
    }
}
