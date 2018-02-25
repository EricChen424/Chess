package model;

import exceptions.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 12/22/2015.
 */
public abstract class Piece {

    protected int health;
    protected UnitCost uCost;
    protected int cost;
    protected boolean side; // false if black, true if white
    protected Square mySquare;
    protected boolean isEliminated;
    protected Set<Square> allMoves;

    public Piece(boolean side, Square square, UnitCost cost){
        health = 100;
        this.side = side;
        mySquare = square;
        if(square != null) {
            square.setPiece(this);
        }
        this.uCost = cost;
        this.cost = uCost.getCost();
        isEliminated = false;
    }

    // getters
    public int getHealth(){
        return health;
    }

    public int getCost(){
        return cost;
    }

    public UnitCost getUnitCost(){ return uCost;}

    public boolean getSide(){
        return side;
    }

    public Square getSquare(){
        return mySquare;
    }

    void setEliminated(boolean eliminated){
        isEliminated = eliminated;
    }

    // creates allMoves if it doesn't exist yet, otherwise returns last updated version of allMoves
    public Set<Square> getAllMoves(){
        if(allMoves == null){
            allMoves = new HashSet<Square>();
            allMoves();
        }
        return allMoves;
    }

    /**
     * sets newSquare as the piece's square and this as newSquare's piece
     * sets mySquare's piece to null
     * if newSquare == null, calls eliminate() instead
     * does nothing if newSquare is the exact same object as mySquare
     * @param newSquare the square to set
     */
    public void setSquare(Square newSquare){
        if(newSquare == mySquare){
            return;
        }
        if(newSquare == null){
            eliminate();
            return;
        }
        if(mySquare == null){
            mySquare = newSquare;
            newSquare.setPiece(this);
        } else{
            Piece oldPiece = newSquare.getPiece();
            mySquare.setPiece(null);
            mySquare = newSquare;
            if((oldPiece == null) || (oldPiece != this)) {
                newSquare.setPiece(this);
            }
        }
    }

    public boolean isEliminated(){
        return isEliminated;
    }

    /**
     * sets isEliminated to true and mySquare to null
     * also ensures mySquare's piece is also null
     */
    public void eliminate(){
        Square s = mySquare;
        isEliminated = true;
        mySquare = null;
        if((s != null) && (s.getPiece() != null)){
            s.setPiece(null);
        }
    }

    /**
     * checks if the piece can move to that square and it's that piece's colour's turn
     * if both these conditions are met, moves the piece to that square otherwise throws InvalidMoveException
     * if square is not valid and InvalidTurnException if it's not that piece's colour's turn
     *
     * if a pawn moved, or a capture is made, reset the stalemate counter, otherwise increment it by 1
     *
     * tells the board it's the next turn
     * @param toSquare square to move to
     * @throws InvalidMoveException thrown if the piece cannot move to this Square
     * @throws InvalidMoveException thrown if it's not this piece's side's turn
     * @throws StalemateException thrown if board is in stalemate
     * @throws CheckmateException thrown if board is in checkmate
     */
    public void move(Square toSquare) throws InvalidMoveException, InvalidTurnException, InvalidCoordinateException, CheckmateException, StalemateException {
        Board board = Board.getInstance();
        if(!(board.getCurrentPlayer() == getSide())){
            if(board.getCurrentPlayer()) {
                throw new InvalidTurnException("Currently white's turn");
            } else{
                throw new InvalidTurnException("Currently black's turn");
            }
        }
        if(getAllMoves().contains(toSquare)){
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
            board.nextTurn();
        } else{
            throw new InvalidMoveException("Cannot move to this square");
        }
    }

    /**
     * updates allMoves to current set of all squares that can be moved to
     */
    public abstract void allMoves();

    /**
     * filters allMoves to only have Squares that can be moved to to prevent check
     */
    protected void checkFilter(Set<Square> allMoves){
        Board b = Board.getInstance();
        Set<Square> noPreventCheck = new HashSet<Square>();
        for(Square s : allMoves){
            if(!b.canMoveEscapeFromCheck(this, s)){
                noPreventCheck.add(s);
            }
        }
        allMoves.removeAll(noPreventCheck);
    }

    /**
     * returns a set of all diagonal squares extending from the piece until either:
     * 1) a board boundary is reached
     * 2) an ally piece is reached (does not include this square)
     * 3) an enemy piece is reached (includes this square)
     * @return a set of all diagonal squares satisfying the above conditions
     */
    protected Set<Square> scanDiagonals(){
        Set<Square> diagonals = new HashSet<Square>();
        int row = mySquare.getRow();
        int column = mySquare.getColumn();

        diagonals.addAll(getOneDiagonal(row, column, true, true));
        diagonals.addAll(getOneDiagonal(row, column, true, false));
        diagonals.addAll(getOneDiagonal(row, column, false, true));
        diagonals.addAll(getOneDiagonal(row, column, false, false));

        return diagonals;
    }

    /**
     * starting at board[row][column], get all diagonals from this square to either an enemy piece, the square before
     * the boundary, or the square before an ally unit in the direction:
     * north if northSouth is true
     * south if northSouth is false
     * east if eastWest is true
     * west if eastWest is false
     * @param row the row of the square the method starts on
     * @param column the column of the square the method starts
     * @param northSouth if true, go north; if false, go south
     * @param eastWest if true, go east; if false, go west
     * @return all diagonals in the specified direction up to the specified square condition
     */
    private Set<Square> getOneDiagonal(int row, int column, boolean northSouth, boolean eastWest) {
        Set<Square> diagonals = new HashSet<Square>();
        Board theBoard = Board.getInstance();
        int rowDirection;
        int columnDirection;

        if(northSouth){
            rowDirection = -1;
        } else{
            rowDirection = 1;
        }

        if(eastWest){
            columnDirection = 1;
        } else{
            columnDirection = -1;
        }

        int currRow = row + rowDirection;
        int currColumn = column + columnDirection;
        Square[][] board = theBoard.getSquares();

        while((currRow >= 0) && (currColumn >= 0) && (currRow < Board.BOARD_DIMENSION)
                && (currColumn < Board.BOARD_DIMENSION)){
            if(addSquareToSet(diagonals, board[currRow][currColumn])){
                break;
            }
            currRow += rowDirection;
            currColumn += columnDirection;
        }

        return diagonals;
    }

    /**
     * adds square to squareSet if square is empty or has an enemy unit, otherwise does nothing with square
     * if square contains any unit, returns true; else return false
     * @param squareSet a set of squares to add squares to
     * @param square the square to assess
     * @return true if square contains a unit; else false
     */
    protected boolean addSquareToSet(Set<Square> squareSet, Square square) {
        Piece thePiece = square.getPiece();
        if(thePiece != null){
            if(thePiece.getSide() != getSide()){
                squareSet.add(square);
            }
            return true;
        } else {
            squareSet.add(square);
            return false;
        }
    }

    /**
     * returns a set of all cardinal squares (N, E, S, W) extending from the piece until either:
     * 1) a board boundary is reached
     * 2) an ally piece is reached (does not include this square)
     * 3) an enemy piece is reached (includes this square)
     * @return a set of all cardinal squares satisfying the above conditions
     */
    protected Set<Square> scanCardinals(){
        Set<Square> cardinals = new HashSet<Square>();
        int row = mySquare.getRow();
        int column = mySquare.getColumn();

        cardinals.addAll(getOneVertical(row, column, true));
        cardinals.addAll(getOneVertical(row, column, false));
        cardinals.addAll(getOneHorizontal(row, column, true));
        cardinals.addAll(getOneHorizontal(row, column, false));

        return cardinals;
    }

    /**
     * returns a set of all squares in a horizontal direction (right if direction is true; else left) from the base
     * square at coordinates row and column of the board until a unit or board boundary is reached
     * a square with an enemy unit is included but not friendly unit
     * @param row the row of the base square
     * @param column the column of the base square
     * @param direction true if right; false if left
     * @return all squares in the specified horizontal direction from the base square satisfying the condition
     */
    private Set<Square> getOneHorizontal(int row, int column, boolean direction) {
        Set<Square> horizontals = new HashSet<Square>();
        Board theBoard = Board.getInstance();

        Square[][] board = theBoard.getSquares();

        int firstRight = column + 1;
        int firstLeft = column - 1;

        if(direction){
            for(int i = firstRight; i < Board.BOARD_DIMENSION; i++){
                if(addSquareToSet(horizontals, board[row][i])){
                    break;
                }
            }
        } else{
            for(int i = firstLeft; i >= 0; i--){
                if(addSquareToSet(horizontals, board[row][i])){
                    break;
                }
            }
        }
        return horizontals;
    }

    /**
     * returns a set of all squares in a horizontal direction (up if direction is true; else down) from the base
     * square at coordinates row and column of the board until a unit or board boundary is reached
     * a square with an enemy unit is included but not friendly unit
     * @param row the row of the base square
     * @param column the column of the base square
     * @param direction true if up; false if down
     * @return all squares in the specified horizontal direction from the base square satisfying the condition
     */
    private Set<Square> getOneVertical(int row, int column, boolean direction) {
        Set<Square> verticals = new HashSet<Square>();
        Board theBoard = Board.getInstance();

        Square[][] board = theBoard.getSquares();

        int firstUp = row - 1;
        int firstDown = row + 1;

        if(direction){
            for(int i = firstDown; i < Board.BOARD_DIMENSION; i++){
                if(addSquareToSet(verticals, board[i][column])){
                    break;
                }
            }
        } else{
            for(int i = firstUp; i >= 0; i--){
                if(addSquareToSet(verticals, board[i][column])){
                    break;
                }
            }
        }
        return verticals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (health != piece.health) return false;
        if (side != piece.side) return false;
        if (isEliminated != piece.isEliminated) return false;
        if (uCost != piece.uCost) return false;
        return mySquare != null ? mySquare.equals(piece.mySquare) : piece.mySquare == null;

    }

    @Override
    public int hashCode() {
        int result = health;
        result = 31 * result + (uCost != null ? uCost.hashCode() : 0);
        result = 31 * result + (side ? 1 : 0);
        result = 31 * result + (mySquare != null ? mySquare.hashCode() : 0);
        result = 31 * result + (isEliminated ? 1 : 0);
        return result;
    }
}
