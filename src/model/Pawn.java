package model;

import exceptions.CheckmateException;
import exceptions.InvalidCoordinateException;
import exceptions.InvalidMoveException;
import exceptions.InvalidTurnException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public class Pawn extends Piece {

    private boolean hasMoved;
    private int turnDoubleJumped;
    private Piece unitBehindMe;

    // turnDoubleJumped is set to -1 to signify the variable is meaningless until it is set positive
    public Pawn(boolean side, Square s){
        super(side, s, UnitCost.PAWN);
        hasMoved = false;
        turnDoubleJumped = -1;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    public int getTurnDoubleJumped(){
        return turnDoubleJumped;
    }

    /**
     * returns true if the pawn is white and on row 8 (Square index 0) or the pawn is black and on row 1 (Square index 7)
     * else false
     * @return true if the pawn can be promoted, else false
     */
    public boolean canPromote(){
        if(getSide()){
            return mySquare.getRow() == 0;
        } else{
            return mySquare.getRow() == 7;
        }
    }

    /**
     * checks if the piece can move to that square and it's that piece's colour's turn
     * if both these conditions are met, moves the piece to that square otherwise throws InvalidMoveException
     * if square is not valid and InvalidTurnException if it's not that piece's colour's turn
     * <p/>
     * tells the board it's the next turn
     * <p/>
     * if a double jump is chosen, set turnDoubleJumped to the turn number before the board incremented the turn count
     * <p/>
     * if hasMoved is false, set it to true
     *
     * @param toSquare square to move to
     * @throws InvalidMoveException thrown if the piece cannot move to this Square
     * @throws InvalidMoveException thrown if it's not this piece's side's turn
     * @throws InvalidCoordinateException thrown if coordinate entered is invalid (logic error with code)
     * @throws CheckmateException thrown if board is in checkmate
     */
    @Override
    public void move(Square toSquare) throws InvalidMoveException, InvalidTurnException, InvalidCoordinateException, CheckmateException {
        Square origSquare = mySquare;
        Board board = Board.getInstance();
        if(!(board.getCurrentPlayer() == getSide())){
            if(board.getCurrentPlayer()) {
                throw new InvalidTurnException("Currently white's turn");
            } else{
                throw new InvalidTurnException("Currently black's turn");
            }
        }
        // TODO: same as with king
        if(getAllMoves().contains(toSquare)){
            board.movePiece(this, toSquare);
            board.resetStalemateCounter();
            if (!hasMoved) {
                hasMoved = true;
            }
            if(Math.abs(mySquare.getRow() - origSquare.getRow()) == 2){
                turnDoubleJumped = board.getTurnNumber();
            }
            Piece p = getUnitBehindMe();
            if((p != null) && (p.getUnitCost().equals(UnitCost.PAWN))){
                // if p satisfies en passant, eliminate it
                Pawn pawnToEliminate = (Pawn) p;
                int enemyDoubleJumpTurn = pawnToEliminate.getTurnDoubleJumped();
                if(enemyDoubleJumpTurn + 1 == board.getTurnNumber()){
                    pawnToEliminate.eliminate();
                }
            }
            // TODO: same as with king
            board.nextTurn();
        } else{
            throw new InvalidMoveException("Cannot move to this square");
        }
    }

    /**
     * @return a set of up to 2 squares - the square immediately in front of the pawn in the direction of the enemy
     * side, and the square one square past that square if the pawn can double jump if the squares in question are empty
     */
    private Set<Square> forwardJumpSquares(){
        Set<Square> forwardJumps = new HashSet<Square>();
        int direction;
        int row = mySquare.getRow();
        int column = mySquare.getColumn();
        Board theBoard = Board.getInstance();
        Square[][] board = theBoard.getSquares();

        // in normal chess the only time a pawn is at the endpoint of the board is if it's on the opposite side
        // and thus can't move further
        if((row == 0) || (row == Board.BOARD_DIMENSION)){
            return forwardJumps;
        }

        if(getSide()){
            direction = -1;
        } else{
            direction = 1;
        }

        row += direction;
        Square firstSquare = board[row][column];

        // if the firstSquare is empty, then add it, otherwise there's something there and can't move any further
        if(firstSquare.getPiece() == null){
            forwardJumps.add(firstSquare);
        } else{
            return forwardJumps;
        }

        // if the pawn hasn't moved, its double jump square must be within board bounds
        if(!getHasMoved()){
            row += direction;
            Square secondSquare = board[row][column];
            if(secondSquare.getPiece() == null){
                forwardJumps.add(secondSquare);
            }
        }

        return forwardJumps;
    }

    /**
     * @return a set containing potentially the 2 squares diagonal from the pawn in the direction of the enemy side
     * if the square contains an enemy unit or if an en passant is possible
     */
    private Set<Square> possibleEliminations(){
        Set<Square> eliminations = new HashSet<Square>();
        int direction;
        int row = mySquare.getRow();
        int column = mySquare.getColumn();
        Board theBoard = Board.getInstance();
        Square[][] board = theBoard.getSquares();

        // in normal chess the only time a pawn is at the endpoint of the board is if it's on the opposite side
        // and thus can't move further
        if((row == 0) || (row == Board.BOARD_DIMENSION)){
            return eliminations;
        }

        if(getSide()){
            direction = -1;
        } else{
            direction = 1;
        }

        try {
            Square leftDiagonal = board[row + direction][column - 1];

            if(checkEnemy(leftDiagonal)) {
                addSquareToSet(eliminations, leftDiagonal);
            } else{
                Square leftEnPSquare = board[row][column - 1];
                if(possibleEnPassant(leftEnPSquare)){
                    addSquareToSet(eliminations, leftDiagonal);
                }
            }
        } catch (IndexOutOfBoundsException e){}

        try {
            Square rightDiagonal = board[row + direction][column + 1];

            if(checkEnemy(rightDiagonal)) {
                addSquareToSet(eliminations, rightDiagonal);
            } else{
                Square rightEnPSquare = board[row][column + 1];
                if(possibleEnPassant(rightEnPSquare)){
                    addSquareToSet(eliminations, rightDiagonal);
                }
            }
        } catch (IndexOutOfBoundsException e){}

        return eliminations;
    }

    /**
     * checks that square has an enemy pawn satisfying en passant conditions:
     * 1) the pawn just did a double jump so it's on the one of the two middle rows depending on its side
     * 2) the current turn is the turn right after the double jump
     * @param square the square to check
     * @return true if can do en passant; else false
     */
    private boolean possibleEnPassant(Square square) {
        Board theBoard = Board.getInstance();
        boolean enemySide = !getSide();

        if(enemySide){
            if(square.getRow() != 4){
                return false;
            }
        } else{
            if(square.getRow() != 3){
                return false;
            }
        }

        Piece p = square.getPiece();
        if((p != null) && (p.getUnitCost().equals(UnitCost.PAWN)) && (p.getSide() == enemySide)){
            Pawn pawn = (Pawn) p;
            int turnDoubleJumped = pawn.getTurnDoubleJumped();
            if(turnDoubleJumped == -1){
                return false;
            }
            if(theBoard.getTurnNumber() == (turnDoubleJumped + 1)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if s has a piece and the piece is on the opposite team of this piece
     * @param s the square to check
     * @return true if s has an enemy piece, else false
     */
    private boolean checkEnemy(Square s){
        Piece p = s.getPiece();

        if((p != null) && (p.getSide() != getSide())){
            return true;
        }
        return false;
    }

    /**
     * if it's the first move, able to move forwards 2 spaces
     * otherwise can only move forwards 1 space each turn, if chosen
     * cannot move past allies or eliminate enemies using forwards move
     * can move diagonally forwards to eliminate an enemy
     * if King is in check, can only move to save to King from check
     *
     * if an enemy pawn moves 2 spaces forwards and is now adjacent to this pawn, this pawn is able to
     * move diagonally forwards towards the enemy pawn to eliminate it however this pawn only has the turn
     * after the enemy pawn moved to do so
     * @return all possible moves for the Pawn
     */
    @Override
    public void allMoves() {
        Board b = Board.getInstance();
        Set<Square> allMoves = new HashSet<Square>();

        allMoves.addAll(forwardJumpSquares());
        allMoves.addAll(possibleEliminations());

        if(b.getInCheck() && (b.getCurrentPlayer() == getSide())){
            checkFilter(allMoves);
        }

        this.allMoves = allMoves;
    }

    public Piece getUnitBehindMe() throws InvalidCoordinateException {
        boolean side = getSide();
        int row = mySquare.getRow();
        int column = mySquare.getColumn();
        Board b = Board.getInstance();

        if(side){
            return b.getSquare(row+1,column).getPiece();
        } else{
            return b.getSquare(row-1,column).getPiece();
        }
    }
}
