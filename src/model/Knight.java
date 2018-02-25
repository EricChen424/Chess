package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class Knight extends Piece {

    public Knight(boolean side, Square s){
        super(side, s, UnitCost.KNIGHT);
    }

    /**
     * each turn, if chosen, this piece can move 1 unit in one cardinal direction (N, E, S, W), and 2 units in another
     * perpendicular cardinal direction or vice versa
     * able to jump over allies to reach the final destination
     * if King is in check, can only move to spaces that will save the King from check
     * @return all possible moves for the Knight
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();

        allMoves.addAll(allLJumps());
        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        }

        this.allMoves = allMoves;
    }

    /**
     * returns a set of the up to 8 squares the knight can jump to based on the movement pattern of one unit in a cardinal
     * direction and 2 units in another perpendicular direction (or vice versa)
     *
     * does not include any squares that would be out of board bounds or squares with an ally unit
     * @return a set of up to 8 squares with the above conditions
     */
    private Set<Square> allLJumps(){
        Set<Square> lJumps = new HashSet<Square>();
        Board theBoard = Board.getInstance();

        int row = mySquare.getRow();
        int column = mySquare.getColumn();
        Square[][] board = theBoard.getSquares();

        try {
            addSquareToSet(lJumps, board[row + 1][column + 2]);
        } catch(IndexOutOfBoundsException e) {}
        try {
            addSquareToSet(lJumps, board[row + 2][column + 1]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row + 1][column - 2]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row + 2][column - 1]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row - 1][column + 2]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row - 2][column + 1]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row - 1][column - 2]);
        } catch (IndexOutOfBoundsException e){}
        try {
            addSquareToSet(lJumps, board[row - 2][column - 1]);
        } catch (IndexOutOfBoundsException e){}

        return lJumps;
    }
}
