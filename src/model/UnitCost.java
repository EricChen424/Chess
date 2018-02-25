package model;

/**
 * Created by Eric on 12/30/2015.
 */
public enum UnitCost {

    KING(0), QUEEN(9), ROOK(5), BISHOP(3), KNIGHT(3), PAWN(1);

    private final int cost;

    UnitCost(int cost){
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }
}
