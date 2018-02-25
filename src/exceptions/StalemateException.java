package exceptions;

/**
 * Created by Eric on 5/3/2016.
 */
public class StalemateException extends Exception{
    public StalemateException(){
        super();
    }

    public StalemateException(String msg){
        super(msg);
    }
}
