package exceptions;

/**
 * Created by Eric on 12/30/2015.
 */
public class InvalidCoordinateException extends Exception {
    public InvalidCoordinateException(){
        super();
    }

    public InvalidCoordinateException(String msg){
        super(msg);
    }
}
