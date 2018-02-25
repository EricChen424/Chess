package exceptions;

/**
 * Created by Eric on 5/1/2016.
 */
public class QuitException extends Exception {
    public QuitException(){
        super();
    }

    public QuitException(String msg){
        super(msg);
    }
}
