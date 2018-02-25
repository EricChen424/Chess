package model;

import exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 12/22/2015.
 */
public class Board {
    // represents a square 8x8 chess board
    // TODO: stalemate and UNDO

    public static final int BOARD_DIMENSION = 8;

    private Square[][] board;
    private static Board theBoard;
    private boolean currentTurn; // true if white, false if black
    private boolean inCheck;
    private int turnCount;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private Pawn promotable;
    private int stalemateCounter;

    /**
     * constructs the board
     */
    private Board(){
        board = new Square[8][8];
        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();
        currentTurn = true;
        turnCount = 1;
        inCheck = false;
        promotable = null;
        stalemateCounter = 0;
        initializeBoard();
    }

    /**
     * if there is no instance of theBoard, create one
     * else return the one and only instance of Board
     * @return the one and only instance of Board
     */
    public static Board getInstance(){
        if(theBoard == null){
            theBoard = new Board();
        }
        return theBoard;
    }

    // deletes the board
    public static void deleteBoard(){
        theBoard = null;
    }

    // getters
    public Square[][] getSquares(){
        return board;
    }

    public boolean getCurrentPlayer(){
        return currentTurn;
    }

    public int getTurnNumber(){
        return turnCount;
    }

    public boolean getInCheck(){ return inCheck;}

    public Pawn getPromotable(){ return promotable;}

    public List<Piece> getWhitePieces(){
        return whitePieces;
    }

    public List<Piece> getBlackPieces(){
        return blackPieces;
    }

    // increments stalemate counter by 1
    void incStalemateCounter() throws StalemateException {
        stalemateCounter++;
        if(stalemateCounter == 50){
            throw new StalemateException("Stalemate!");
        }
    }

    // sets stalemate counter to 0
    void resetStalemateCounter(){
        stalemateCounter = 0;
    }


    /**
     * returns a list of all eliminated pieces for side side
     * @param side the side whose eliminated pieces we are interested in
     * @return a list of all eliminated pieces for side
     */
    public List<Piece> getSideGraveyard(boolean side){
        List<Piece> sidePieces;
        List<Piece> graveyard = new ArrayList<Piece>();
        if(side){
            sidePieces = whitePieces;
        } else{
            sidePieces = blackPieces;
        }
        for(Piece p : sidePieces){
            if(p.isEliminated()){
                graveyard.add(p);
            }
        }
        return graveyard;
    }

    /**
     * @return the player before the current player; if it's turn 1, return true (white)
     */
    public boolean getPreviousPlayer(){
        if(turnCount == 1){
            return true;
        } else {
            return !currentTurn;
        }
    }

    /**
     * if currentTurn is true, set it to false; otherwise set it to true
     * updates all moves for the previous player (as the piece moved can block allies)
     * check for if next player is in check
     * updates all moves for the next player to take into account the check
     *
     * @throws CheckmateException if board is in checkmate, throw CheckmateException
     */
    void nextTurn() throws CheckmateException {
        // TODO maybe make the promote method also update opposing team, check(), and updatecurrent team to allow for
        // promotion on the next player's turn but make it feel like it happened before that
        promotable = updatePromotable();
        currentTurn = !currentTurn;
        turnCount++;
        updateOpposingTeam();
        check();
        updateCurrentTeam();
        if(checkmate()){
            throw new CheckmateException("Checkmate!");
        }
    }

    /**
     * @return returns the one promotable pawn if it exists, otherwise returns null
     */
    private Pawn updatePromotable() {
        List<Pawn> pawnsOfCurrentTurn;
        if(currentTurn) {
            pawnsOfCurrentTurn = filterPawns(whitePieces);
        } else{
            pawnsOfCurrentTurn = filterPawns(blackPieces);
        }
        filterPromotable(pawnsOfCurrentTurn);
        assert(pawnsOfCurrentTurn.size() <= 1);
        if(pawnsOfCurrentTurn.size() == 1){
            return pawnsOfCurrentTurn.get(0);
        } else {
            return null;
        }
    }

    /**
     * removes all Pawns in pawns that are not promotable
     * @param pawns
     */
    private void filterPromotable(List<Pawn> pawns) {
        List<Pawn> toRemove = new ArrayList<Pawn>();
        for(Pawn p : pawns){
            if(!p.canPromote()){
                toRemove.add(p);
            }
        }
        pawns.removeAll(toRemove);
    }

    /**
     * returns a new list containing only non-eliminated pawns in pieces
     * @param pieces the list of pieces to filter
     * @return a new list of pawns with only non-eliminated pawns
     */
    private List<Pawn> filterPawns(List<Piece> pieces) {
        List<Pawn> pawns = new ArrayList<Pawn>();
        for(Piece p : pieces){
            if(p.getUnitCost().equals(UnitCost.PAWN) && !p.isEliminated()){
                pawns.add((Pawn) p);
            }
        }
        return pawns;
    }

    /**
     * makes the 64 squares , creates pieces for both players, and sets them in the correct spaces
     */
    private void initializeBoard() {
        generateSquares();
        setBlackPieces();
        setWhitePieces();
        currentTurn = true;
    }

    /**
     * creates and sets the white pieces
     */
    private void setWhitePieces() {
        for(int i = 0; i < 8; i++){
            whitePieces.add(new Pawn(true, board[6][i]));
        }
        whitePieces.add(new Rook(true, board[7][0]));
        whitePieces.add(new Knight(true, board[7][1]));
        whitePieces.add(new Bishop(true, board[7][2]));
        whitePieces.add(new Queen(true, board[7][3]));
        whitePieces.add(new King(true, board[7][4]));
        whitePieces.add(new Bishop(true, board[7][5]));
        whitePieces.add(new Knight(true, board[7][6]));
        whitePieces.add(new Rook(true, board[7][7]));
    }

    /**
     * creates and sets the black pieces
     */
    private void setBlackPieces() {
        for(int i = 0; i < 8; i++){
            blackPieces.add(new Pawn(false, board[1][i]));
        }

        blackPieces.add(new Rook(false, board[0][0]));
        blackPieces.add(new Knight(false, board[0][1]));
        blackPieces.add(new Bishop(false, board[0][2]));
        blackPieces.add(new Queen(false, board[0][3]));
        blackPieces.add(new King(false, board[0][4]));
        blackPieces.add(new Bishop(false, board[0][5]));
        blackPieces.add(new Knight(false, board[0][6]));
        blackPieces.add(new Rook(false, board[0][7]));
    }

    /**
     * creates an 8x8 block of squares alternating between black and white
     */
    private void generateSquares() {
        boolean white = true;
        boolean black = false;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(white){
                    board[i][j] = new Square(true, i, j);
                    if(j == 7){
                        break;
                    }
                    white = false;
                    black = true;
                } else if (black){
                    board[i][j] = new Square(false, i, j);
                    if(j == 7){
                        break;
                    }
                    white = true;
                    black = false;
                } else{
                    throw new ProgrammerErrorException();
                }

            }
        }
    }

    /**
     * moves piece p from its original square to square s, and eliminates any piece originally on s
     * silently returns if p is null (as no piece is being moved)
     * @param p the piece to move
     * @param s the square to move to
     */
    public void movePiece(Piece p, Square s){
        if(p == null){
            return;
        }
        s.setPiece(p);
    }

    /**
     * if coord is a 2 character String where the first character is a letter a-h and the second character is a number
     * 1-8, returns the Square corresponding to this coord
     * although not recommended, there can be an arbitrary number of spaces at the borders of the String
     * otherwise, throws InvalidCoordinateException
     * @param coord the coordinate of a square on the chess board
     * @return the square that the coordinate specifies
     * @throws InvalidCoordinateException thrown when inputted String is not a valid chess board coordinate
     */
    public Square getSquare(String coord) throws InvalidCoordinateException {
        String squareCoord = coord.trim();
        if(squareCoord.length() != 2){
            throw new InvalidCoordinateException("Did not enter a chess board coordinate");
        }
        String column = squareCoord.substring(0, 1).toLowerCase();
        String row = squareCoord.substring(1, 2);
        int rowNum = Integer.parseInt(row);

        if(!(column.equals("a") || column.equals("b") || column.equals("c") || column.equals("d") ||
            column.equals("e") || column.equals("f") || column.equals("g") || column.equals("h"))){
            throw new InvalidCoordinateException("Column value must be a letter from a-h");
        }

        if((rowNum < 1) || (rowNum > 8)){
            throw new InvalidCoordinateException("Row value must be a value from 1-8");
        }

        int rowIndex = 8 - rowNum;

        int columnIndex;

        if(column.equals("a")){
            columnIndex = 0;
        } else if(column.equals("b")){
            columnIndex = 1;
        } else if(column.equals("c")){
            columnIndex = 2;
        } else if(column.equals("d")){
            columnIndex = 3;
        } else if(column.equals("e")){
            columnIndex = 4;
        } else if(column.equals("f")){
            columnIndex = 5;
        } else if(column.equals("g")){
            columnIndex = 6;
        } else {
            columnIndex = 7;
        }

        return board[rowIndex][columnIndex];
    }

    /**
     *  returns board[row][column]
     * @param row the 0-indiced row of the square
     * @param column the 0-indiced column of the square
     * @return board[row][column]
     * @throws InvalidCoordinateException thrown when row and column are not in the interval [0,7]
     */
    public Square getSquare(int row, int column) throws InvalidCoordinateException{
        if((row < 0) || (row > 7) || (column < 0) || (column > 7)){
            throw new InvalidCoordinateException("Row and column must be from 0 to 7");
        }
        return board[row][column];
    }

    /**
     * returns true if the King of the current turn is in a position where an enemy piece can eliminate it
     * the next turn, else false, and sets inCheck to the result
     * @return true if King is in check, else false
     */
    public boolean check(){
        boolean offence = !getCurrentPlayer();

        List<Square> allPossibleMoves;

        if(offence){
            allPossibleMoves = getAllPossibleMoves(whitePieces);
        } else{
            allPossibleMoves = getAllPossibleMoves(blackPieces);
        }

        for(Square s : allPossibleMoves){
            Piece p = s.getPiece();
            if((p != null) && (p.getUnitCost().equals(UnitCost.KING)) &&
                    (p.getSide() == getCurrentPlayer())){
                inCheck = true;
                return true;
            }
        }
        inCheck = false;
        return false;
    }

    /**
     * gets all possible moves for all pieces in pieces that are still on the board (ie. their square isn't null)
     * @param pieces the pieces to find all possible moves for
     * @return a list of all possible moves in pieces
     */
    private List<Square> getAllPossibleMoves(List<Piece> pieces) {
        List<Square> allSquares = new ArrayList<Square>();

        for(Piece p : pieces){
            if(!p.isEliminated()){
                allSquares.addAll(p.getAllMoves());
            }
        }
        return allSquares;
    }

    /**
     * returns true if the current turn's player has no possible move to remove the King from check
     * @return true if King is in check, else false
     */
    private boolean checkmate(){
        if(!getInCheck()){
            return false;
        } else{
            boolean currentPlayer = getCurrentPlayer();
            //boolean stillCheck = false;
            List<Piece> allPieces;

            if(currentPlayer){
                allPieces = whitePieces;
            } else{
                allPieces = blackPieces;
            }

            if(canEscapeFromCheck(allPieces)){
                return false;
            } else{
                return true;
            }
        }
    }

    /**
     * returns true if there exists a move to free the current King from check else false
     * @param allPieces all pieces for the current player
     * @return true if the King can be freed from check; else false
     */
    private boolean canEscapeFromCheck(List<Piece> allPieces) {

        // if there exists a piece with moves available, it's not a checkmate
        for(Piece p : allPieces){
            if(!p.isEliminated()){
                if(!p.getAllMoves().isEmpty()){
                    return true;
                }
            }
        }
        return false;


        // for each move in allPossibleMoves make the move
        // check if the board is in check still
        // reset the board to its original setting (returning units to their correct place and from the graveyard

        /**
        boolean canEscape = false;
        for(Piece p : allPieces){
            if(!p.isEliminated()) {
                for(Square s : p.allMoves()){ // TODO: this implementation will not work for en passant as movePiece doesn't affect the pieces correctly
                    Square originalSquare = p.getSquare();
                    Piece maybeEliminated = s.getPiece();
                    movePiece(p, s);
                    if(!check()){
                        canEscape = true;
                    }
                    movePiece(p, originalSquare);
                    if(maybeEliminated != null){
                        maybeEliminated.setEliminated(false);
                        movePiece(maybeEliminated, s); // TODO may pose problems if we use observer pattern on "eliminate()" to change score
                    }
                    if(canEscape){
                        return true;
                    }
                }
            }
        }
        return false;
         **/
    }

    /**
     * returns true if moving the King to s will put it in check else false
     * @param king the king to move
     * @param s the square to move to
     * @return true if king gets in check by moving to s, else false
     */
    public boolean putInCheck(King king, Square s) {
        assert(!getInCheck());
        boolean riskyMove = false;
        Square originalSquare = king.getSquare();
        Piece maybeEliminated = s.getPiece();
        movePiece(king, s);
        updateOpposingTeam();
        if(check()){
            riskyMove = true;
        }
        movePiece(king, originalSquare);
        if(maybeEliminated != null){
            maybeEliminated.setEliminated(false);
            movePiece(maybeEliminated, s); // TODO may pose problems if we use observer pattern on "eliminate()" to change score
        }
        updateOpposingTeam();
        if(check()){
            assert(false);
        }
        return riskyMove;
    }

    /**
     * If moving p to s removes check, return true, else false
     * @param p the piece to move
     * @param s the square to move to
     * @return true if moving p to s removes check, else false
     */
    public boolean canMoveEscapeFromCheck(Piece p, Square s){
        //make the move
        // check if the board is in check still
        // reset the board to its original setting (returning units to their correct place and from the graveyard)
        // TODO: this implementation will not work for en passant as movePiece doesn't affect the pieces correctly
        assert(getInCheck());
        boolean canEscape = false;
        Square originalSquare = p.getSquare();
        Piece maybeEliminated = s.getPiece();
        movePiece(p, s);
        updateOpposingTeam();
        if(!check()){
            canEscape = true;
        }
        movePiece(p, originalSquare);
        if(maybeEliminated != null){
            maybeEliminated.setEliminated(false);
            movePiece(maybeEliminated, s); // TODO may pose problems if we use observer pattern on "eliminate()" to change score
        }
        updateOpposingTeam();
        if(!check()){
            assert(false);
        }
        return canEscape;
    }

    /**
     * Updates all pieces that are on the current player's side
     */
    private void updateCurrentTeam() {
        if (getCurrentPlayer()) {
            updateTeam(whitePieces);
        } else {
            updateTeam(blackPieces);
        }
    }

    /**
     * Updates all pieces that are not on the current player's side
     */
    private void updateOpposingTeam() {
        if (getCurrentPlayer()) {
            updateTeam(blackPieces);
        } else {
            updateTeam(whitePieces);
        }
    }

    /**
     * updates all pieces in pieces that aren't eliminated
     * @param pieces the pieces to update
     */
    private void updateTeam(List<Piece> pieces){
        for (Piece p : pieces) {
            if(!p.isEliminated()) {
                p.allMoves();
            }
        }
    }

    @Override
    public String toString(){
        String s = new String();

        for(int i = 0; i < 8; i++){
            s += 8-i + " ";
            for(int j = 0; j < 8; j++){
                Square tempSquare = board[i][j];
                s += "[";
                Piece p = tempSquare.getPiece();

                if(p == null){
                    s += "  ";
                } else{
                    if(p.getSide()){
                        s += "W";
                    } else{
                        s += "B";
                    }
                    UnitCost u = p.getUnitCost();
                    if(u.equals(UnitCost.PAWN)){
                        s += "P";
                    } else if(u.equals(UnitCost.BISHOP)){
                        s += "B";
                    } else if(u.equals(UnitCost.KNIGHT)){
                        s += "H";
                    } else if(u.equals(UnitCost.ROOK)){
                        s += "R";
                    } else if(u.equals(UnitCost.QUEEN)){
                        s += "Q";
                    } else{
                        s += "K";
                    }
                }
                s += "]";
            }
            s += System.lineSeparator();
        }
        s += "    A  " + " B  " + " C  " + " D  " + " E  " + " F  " + " G  " + " H  ";
        return s;
    }

    /**
     * promotes the unit promotable to s, where s is one of k = "Knight",  b = "Bishop", r = "Rook", q = "Queen"
     * @param s the unit to promote to
     */
    public void promote(String s) throws InvalidPromotionException, InvalidPromotionInputException {
        if(promotable == null){
            throw new InvalidPromotionException("There is no unit eligible for promotion.");
        }
        String tempString = s.toLowerCase();
        if(!((tempString.equals("k")) || (tempString.equals("b")) || (tempString.equals("r")) || (tempString.equals("q")))){
            throw new InvalidPromotionInputException("Invalid input. Must input one of 'k', 'b', 'r', or 'q'.");
        }

        boolean side = promotable.getSide();
        Square mySquare = promotable.getSquare();

        Piece promoted;

        if(tempString.equals("k")){
            promoted = new Knight(side, mySquare);
        } else if(s.equals("b")){
            promoted = new Bishop(side, mySquare);
        } else if(s.equals("r")){
            promoted = new Rook(side, mySquare);
        } else{
            promoted = new Queen(side, mySquare);
        }

        if(side){
            whitePieces.add(promoted);
            whitePieces.remove(promotable);
        } else{
            blackPieces.add(promoted);
            blackPieces.remove(promotable);
        }
        promotable = null;
        //TODO: scoreboard might get wonky after this as a pawn technically got eliminated
        // TODO: ensure that this occurs BEFORE board calls nextTurn() so everything updates
    }
}
