package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class Queen extends Piece {

    public Queen(boolean side, Square s){
        super(side, s, UnitCost.QUEEN);
    }

    /**
     * each turn, if chosen, can pick any straight line direction and move indefinitely until board boundaries or
     * another unit has been reached
     * cannot move past units, however can eliminate enemy units
     * if the King is in check, can only move to save the King from check
     * @return all possible moves for the Queen
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();

        allMoves.addAll(scanDiagonals());
        allMoves.addAll(scanCardinals());

        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        }

        this.allMoves = allMoves;
    }
}
