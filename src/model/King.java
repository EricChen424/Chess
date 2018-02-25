package model;

import exceptions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class King extends Piece {

    protected boolean hasMoved;

    public King(boolean side, Square s){
        super(side, s, UnitCost.KING);
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
     * @throws InvalidCoordinateException thrown if square coordinate is invalid (logic error with code)
     * @throws CheckmateException thrown if board is in checkmate
     * @throws StalemateException thrown if board is in stalemate
     */
    @Override
    public void move(Square toSquare) throws InvalidMoveException, InvalidTurnException,
            InvalidCoordinateException, CheckmateException, StalemateException {
        boolean isCastleSquare = castleSquares().contains(toSquare);
        Board board = Board.getInstance();
        if(!(board.getCurrentPlayer() == getSide())){
            if(board.getCurrentPlayer()) {
                throw new InvalidTurnException("Currently white's turn");
            } else{
                throw new InvalidTurnException("Currently black's turn");
            }
        }
        if(getAllMoves().contains(toSquare)){
            //TODO: find a way to refactor this without copypasting so much code
            if(getUnitCost().equals(UnitCost.PAWN)){
                board.resetStalemateCounter();
            } else if(toSquare.getPiece() != null){
                board.resetStalemateCounter();
            } else{
                try {
                    board.incStalemateCounter();
                } catch (StalemateException e) {
                    board.movePiece(this, toSquare);
                    throw new StalemateException("Stalemate!");
                }
            }
            board.movePiece(this, toSquare);
            if(!hasMoved){
                hasMoved = true;
            }

            if(isCastleSquare){
                int columnNum = toSquare.getColumn();
                String row;
                String column;
                Rook r = getRook(toSquare);

                if(getSide()){
                    row = "1";
                } else{
                    row = "8";
                }
                if(columnNum > 4){
                    column = "f";
                } else{
                    column = "d";
                }

                board.movePiece(r, board.getSquare(column + row));
            }
            // TODO: end
            board.nextTurn();
        } else{
            throw new InvalidMoveException("Cannot move to this square");
        }
    }

    // gets the correct Rook that will move to castle
    private Rook getRook(Square toSquare) throws InvalidCoordinateException {
        Rook r;
        boolean side = getSide();
        int columnNum = toSquare.getColumn();
        Board b = Board.getInstance();

        String row;
        String column;

        if(side) {
            row = "1";
        } else{
            row = "8";
        }

        if(columnNum > 4){
            column = "h";
        } else{
            column = "a";
        }

        r = (Rook) b.getSquare(column + row).getPiece();
        return r;
    }

    /**
     * @return a set of all adjacent squares to the King's square that are within board boundaries and don't have
     * an allied unit
     */
    private Set<Square> allAdjacentSquares(){
        Set<Square> adjacents = new HashSet<Square>();

        int row = mySquare.getRow();
        int column = mySquare.getColumn();
        Board theBoard = Board.getInstance();
        Square[][] board = theBoard.getSquares();

        try{
            addSquareToSet(adjacents, board[row + 1][column + 1]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row + 1][column - 1]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row - 1][column + 1]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row - 1][column - 1]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row + 1][column]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row - 1][column]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row][column + 1]);
        } catch (IndexOutOfBoundsException e){}
        try{
            addSquareToSet(adjacents, board[row][column - 1]);
        } catch (IndexOutOfBoundsException e){}

        return adjacents;
    }

    /**
     * @return a set containing up to the two squares (g1/g8, c1/c8) the King can jump to if it can castle
     */
    private Set<Square> castleSquares(){
        Board theBoard = Board.getInstance();
        Square[][] board = theBoard.getSquares();
        int row = mySquare.getRow();

        Set<Square> castleSquares = new HashSet<Square>();

        if(getHasMoved()){
            return castleSquares;
        }
        Set<Square> cardinals = scanCardinals();

        if(cardinals.contains(board[row][1])
                && cardinals.contains(board[row][2]) && cardinals.contains(board[row][3])){
            if(checkRook(board[row][0])){
                castleSquares.add(board[row][2]);
            }
        }

        if(cardinals.contains(board[row][5]) && cardinals.contains(board[row][6])){
            if(checkRook(board[row][7])){
                castleSquares.add(board[row][6]);
            }
        }


        return castleSquares;
    }

    /**
     * checks that square has a rook and the rook hasn't moved
     * @param square the square to check
     * @return true if square has a rook that hasn't moved; else false
     */
    private boolean checkRook(Square square) {
        Piece p = square.getPiece();
        if(p == null){
            return false;
        }
        if(p.getUnitCost().equals(UnitCost.ROOK)){
            Rook r = (Rook) p;
            if(!r.getHasMoved()){
                return true;
            }
        }
        return false;
    }

    /**
     * each turn, if chosen, this piece can only move one square in any direction, cannot move over allies, and cannot
     * put itself in check
     *
     * if the King has not moved nor is in check, a Rook has not moved, and the squares between the King and the Rook in question
     * are empty, the King can move two spaces towards the Rook and the Rook moves towards the King and jumps over it
     * to land adjacent to the King on the other side
     * TODO: remember the king cannot be in check
     * @return all possible moves for the King
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();
        allMoves.addAll(allAdjacentSquares());
        if(!b.getInCheck()){
            allMoves.addAll(castleSquares());
        }
        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        } else if (b.getCurrentPlayer() == getSide()){
            riskFilter(allMoves);
        }
        this.allMoves = allMoves;
    }

    /**
     * removes all squares that would put the King in check
     * @param allMoves the list of squares to filter
     */
    private void riskFilter(Set<Square> allMoves) {
        Board b = Board.getInstance();
        Set<Square> toRemove = new HashSet<Square>();
        for(Square s : allMoves){
            if(b.putInCheck(this, s)){
                toRemove.add(s);
            }
        }
        allMoves.removeAll(toRemove);
    }
}
