package exceptions;

/**
 * Created by Eric on 5/1/2016.
 */
public class InvalidPromotionException extends Exception {
    public InvalidPromotionException(){
        super();
    }

    public InvalidPromotionException(String msg){
        super(msg);
    }
}
