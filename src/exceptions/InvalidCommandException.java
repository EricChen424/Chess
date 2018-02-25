package exceptions;

/**
 * Created by Eric on 5/1/2016.
 */
public class InvalidCommandException extends Exception {
    public InvalidCommandException(){
        super();
    }

    public InvalidCommandException(String msg){
        super(msg);
    }
}
