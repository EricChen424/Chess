package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class Bishop extends Piece {

    public Bishop(boolean side, Square s){
        super(side, s, UnitCost.BISHOP);
    }

    /**
     * each turn, if chosen can move in a diagonal direction indefinitely until it reaches the board boundaries or
     * another unit
     * cannot move past units, but can eliminate enemy units
     * if the King is in check, can only move to save the King from check
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();

        allMoves.addAll(scanDiagonals());
        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        }
        this.allMoves = allMoves;
    }
}
