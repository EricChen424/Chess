package exceptions;

/**
 * Created by Eric on 5/3/2016.
 */
public class CheckmateException extends Exception{
    public CheckmateException(){
        super();
    }

    public CheckmateException(String msg){
        super(msg);
    }
}
