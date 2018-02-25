package exceptions;

/**
 * Created by Eric on 5/1/2016.
 */
public class InvalidPromotionInputException extends Exception{
    public InvalidPromotionInputException(){
        super();
    }

    public InvalidPromotionInputException(String msg){
        super(msg);
    }
}
