package model;

import exceptions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class Rook extends Piece {

    boolean hasMoved;

    public Rook(boolean side, Square s){
        super(side, s, UnitCost.ROOK);
        hasMoved = false;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    /**
     * checks if the piece can move to that square and it's that piece's colour's turn
     * if both these conditions are met, moves the piece to that square otherwise throws InvalidMoveException
     * if square is not valid and InvalidTurnException if it's not that piece's colour's turn
     *
     * if hasMoved is false and the piece successfully moves, sets hasMoved to true
     * @param toSquare square to move to
     * @throws InvalidMoveException thrown if the piece cannot move to this Square
     * @throws InvalidMoveException thrown if it's not this piece's side's turn
     * @throws StalemateException thrown if board in stalemate
     * @throws CheckmateException thrown if board in checkmate
     */
    @Override
    public void move(Square toSquare) throws InvalidMoveException, InvalidTurnException, InvalidCoordinateException, CheckmateException, StalemateException {
        super.move(toSquare);
        if(!hasMoved){
            hasMoved = true;
        }
    }

    /**
     * each turn, if chosen, can move indefinitely in one of the cardinal directions until it reaches board boundaries
     * or another unit
     * cannot move past units, however can eliminate enemy units
     *
     * if the King is in check, can only move to save the King from check
     * @return all possible moves for the Rook
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();

        allMoves.addAll(scanCardinals());
        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        }

        this.allMoves = allMoves;
    }
}
