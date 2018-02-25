package test;

import exceptions.*;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/22/2015.
 */
public class BoardTest {

    private Board b;
    private Square[][] s;

    @Before
    public void setUp(){
        Board.deleteBoard();
        b = Board.getInstance();
        s = b.getSquares();
    }


    // ensures after setup every square has the right piece
    // also checks the getCurrentPiecesOfSide method
    @Test
    public void testProperPieces() throws Invalid2DArrayException {

        // black piece test
        for(int i = 0; i < 8; i++){
            assertEquals(UnitCost.PAWN, s[1][i].getPiece().getUnitCost());
        }
        assertEquals(UnitCost.ROOK, s[0][0].getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, s[0][1].getPiece().getUnitCost());
        assertEquals(UnitCost.BISHOP, s[0][2].getPiece().getUnitCost());
        assertEquals(UnitCost.QUEEN, s[0][3].getPiece().getUnitCost());
        assertEquals(UnitCost.KING, s[0][4].getPiece().getUnitCost());
        assertEquals(UnitCost.BISHOP, s[0][5].getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, s[0][6].getPiece().getUnitCost());
        assertEquals(UnitCost.ROOK, s[0][7].getPiece().getUnitCost());


        // white piece test
        for(int i = 0; i < 8; i++){
            assertEquals(UnitCost.PAWN, s[6][i].getPiece().getUnitCost());
        }
        assertEquals(UnitCost.ROOK, s[7][0].getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, s[7][1].getPiece().getUnitCost());
        assertEquals(UnitCost.BISHOP, s[7][2].getPiece().getUnitCost());
        assertEquals(UnitCost.QUEEN, s[7][3].getPiece().getUnitCost());
        assertEquals(UnitCost.KING, s[7][4].getPiece().getUnitCost());
        assertEquals(UnitCost.BISHOP, s[7][5].getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, s[7][6].getPiece().getUnitCost());
        assertEquals(UnitCost.ROOK, s[7][7].getPiece().getUnitCost());
    }

    // ensures after setup each square in the board alternates colour
    @Test
    public void testSquaresAlternatingColour(){
        boolean expectWhite = true;
        boolean expectBlack = false;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                assertEquals(i, s[i][j].getRow());
                assertEquals(j, s[i][j].getColumn());
                if(expectWhite){
                    assertTrue(s[i][j].getColour());
                    if(j == 7){
                        break;
                    }
                    expectWhite = false;
                    expectBlack = true;
                } else if(expectBlack){
                    assertFalse(s[i][j].getColour());
                    if(j == 7){
                        break;
                    }
                    expectWhite = true;
                    expectBlack = false;
                } else{
                    fail("???");
                }
            }
        }
    }

    // tests moving a piece from one square to an empty square
    // also tests moving a piece from one square to a square with another piece
    @Test
    public void testMovePiece(){
        Rook br0 = new Rook(false, s[0][0]);
        Knight wk0 = new Knight(true, s[7][1]);

        assertEquals(br0, s[0][0].getPiece());
        assertEquals(wk0, s[7][1].getPiece());
        assertEquals(s[0][0], br0.getSquare());
        assertEquals(s[7][1], wk0.getSquare());

        b.movePiece(br0, s[7][1]);
        assertEquals(br0, s[7][1].getPiece());
        assertEquals(s[7][1], br0.getSquare());
        assertEquals(null, wk0.getSquare());
    }

    @Test
    public void testNextTurn() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        assertTrue(b.getCurrentPlayer());
        assertTrue(b.getPreviousPlayer());
        assertEquals(1, b.getTurnNumber());
        b.getSquare("b2").getPiece().move(b.getSquare("b4"));
        assertFalse(b.getCurrentPlayer());
        assertEquals(2, b.getTurnNumber());
        assertTrue(b.getPreviousPlayer());
        b.getSquare("b7").getPiece().move(b.getSquare("b5"));
        assertTrue(b.getCurrentPlayer());
        assertEquals(3, b.getTurnNumber());
        assertFalse(b.getPreviousPlayer());
    }

    @Test
    public void testGetSquare() throws Exception{
        assertEquals("a8", b.getSquare("a8").toString());
        assertEquals("g5", b.getSquare("    g5     ").toString());
    }

    @Test (expected = InvalidCoordinateException.class)
    public void testGetSquareInvalidString() throws Exception{
        b.getSquare("hello");
    }

    @Test (expected = InvalidCoordinateException.class)
    public void testGetSquareInvalidString2() throws Exception{
        b.getSquare("h");
    }

    @Test (expected = InvalidCoordinateException.class)
    public void testGetSquareLetterPastH() throws Exception{
        b.getSquare(" i4 ");
    }

    @Test (expected = InvalidCoordinateException.class)
    public void testGetSquareNumberPast8() throws Exception{
        b.getSquare("b9");
    }

    @Test
    public void testCheckAndCheckmate() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException {
        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        try {
            b.getSquare("e2").getPiece().move(b.getSquare("e4"));
        } catch (CheckmateException e) {
            fail();
        }

        assertFalse(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        try {
            b.getSquare("f7").getPiece().move(b.getSquare("f5"));
        } catch (CheckmateException e) {
            fail();
        }

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        try {
            b.getSquare("e4").getPiece().move(b.getSquare("f5"));
        } catch (CheckmateException e) {
            fail();
        }

        assertFalse(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        try {
            b.getSquare("g7").getPiece().move(b.getSquare("g5"));
        } catch (CheckmateException e) {
            fail();
        }

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        try {
            b.getSquare("d1").getPiece().move(b.getSquare("h5"));
        } catch (CheckmateException e) {
            assertFalse(b.getCurrentPlayer());
            assertTrue(b.getInCheck());
        }
    }


    @Test
    public void testCheck() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("e2").getPiece().move(b.getSquare("e3"));

        assertFalse(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("d7").getPiece().move(b.getSquare("d5"));

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("f1").getPiece().move(b.getSquare("b5"));

        assertFalse(b.getCurrentPlayer());
        assertTrue(b.getInCheck());

        b.getSquare("d8").getPiece().move(b.getSquare("d7"));

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("a2").getPiece().move(b.getSquare("a3"));

        assertFalse(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("f7").getPiece().move(b.getSquare("f6"));

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("b5").getPiece().move(b.getSquare("d7"));

        assertFalse(b.getCurrentPlayer());
        assertTrue(b.getInCheck());

        b.getSquare("e8").getPiece().move(b.getSquare("f7"));

        assertTrue(b.getCurrentPlayer());
        assertFalse(b.getInCheck());

        b.getSquare("d1").getPiece().move(b.getSquare("h5"));

        assertFalse(b.getCurrentPlayer());
        assertTrue(b.getInCheck());
    }

    @Test
    public void testGetSideGraveyard() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece testP1 = b.getSquare("c7").getPiece();
        Piece testP2 = b.getSquare("b8").getPiece();
        b.getSquare("d2").getPiece().move(b.getSquare("d4"));
        testP1.move(b.getSquare("c5"));
        b.getSquare("d4").getPiece().move(b.getSquare("c5"));
        b.getSquare("b7").getPiece().move(b.getSquare("b6"));
        b.getSquare("c5").getPiece().move(b.getSquare("b6"));
        b.getSquare("c8").getPiece().move(b.getSquare("b7"));
        b.getSquare("b6").getPiece().move(b.getSquare("a7"));
        b.getSquare("b7").getPiece().move(b.getSquare("c6"));
        b.getSquare("a7").getPiece().move(b.getSquare("b8"));
        b.getSquare("a8").getPiece().move(b.getSquare("b8"));

        assertEquals(1, b.getSideGraveyard(true).size());
        assertEquals(4, b.getSideGraveyard(false).size());
        assertEquals(b.getSideGraveyard(true).get(0).getUnitCost(), UnitCost.PAWN);
        List<Piece> testList = new ArrayList<Piece>();
        testList.add(testP1);
        testList.add(testP1);
        testList.add(testP1);
        testList.add(testP2);
        assertEquals(testList, b.getSideGraveyard(false));
    }

    @Test
    public void testPromote() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException,
            InvalidPromotionInputException, InvalidPromotionException, StalemateException, CheckmateException {
        Piece testP1 = b.getSquare("d2").getPiece();
        setUpPromotion();

        assertTrue(b.getPromotable().equals(testP1));

        b.promote("q");

        Piece newQueen = b.getSquare("b8").getPiece();

        assertEquals(newQueen.getUnitCost(), UnitCost.QUEEN);
        assertEquals(b.getPromotable(), null);

        Set<Square> moves = new HashSet<Square>();
        moves.add(b.getSquare("a8"));
        moves.add(b.getSquare("c8"));
        moves.add(b.getSquare("d8"));
        moves.add(b.getSquare("b7"));
        moves.add(b.getSquare("b6"));
        moves.add(b.getSquare("b5"));
        moves.add(b.getSquare("b4"));
        moves.add(b.getSquare("b3"));
        moves.add(b.getSquare("a7"));
        moves.add(b.getSquare("c7"));
        moves.add(b.getSquare("d6"));
        moves.add(b.getSquare("e5"));
        moves.add(b.getSquare("f4"));
        moves.add(b.getSquare("g3"));

        assertEquals(moves, newQueen.getAllMoves()); // test that all moves updated correctly
        assertFalse(b.getSideGraveyard(true).contains(testP1));
        assertFalse(b.getWhitePieces().contains(testP1));
        assertTrue(b.getWhitePieces().contains(newQueen));
    }

    @Test (expected = InvalidPromotionInputException.class)
    public void testPromoteICIException() throws InvalidCoordinateException, InvalidPromotionInputException,
            InvalidPromotionException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece testP1 = b.getSquare("d2").getPiece();
        setUpPromotion();

        assertTrue(b.getPromotable().equals(testP1));

        b.promote("j");
    }

    @Test (expected = InvalidPromotionException.class)
    public void testPromoteICException() throws InvalidCoordinateException, InvalidPromotionInputException,
            InvalidPromotionException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        b.getSquare("d2").getPiece().move(b.getSquare("d4"));

        assertEquals(b.getPromotable(), null);

        b.promote("q");
    }

    // tests that the stalemate counter gets reset if a pawn moves or a capture is made but also that the board can tell
    // a stalemate has been made if 50 moves not following the above conditions are made
    @Test (expected = StalemateException.class)
    public void testStalemate() throws InvalidMoveException, CheckmateException, StalemateException, InvalidCoordinateException, InvalidTurnException {
        make4nUselessMoves(12);
        Piece wknight = b.getSquare("b1").getPiece();
        Piece bknight = b.getSquare("b8").getPiece();

        // 2 more useless moves to get to 50
        wknight.move(b.getSquare("c3"));
        bknight.move(b.getSquare("c6"));
    }

    @Test
    public void testStalematePawn() throws InvalidMoveException, CheckmateException, StalemateException, InvalidCoordinateException, InvalidTurnException {
        make4nUselessMoves(12);
        Piece wknight = b.getSquare("b1").getPiece();
        Piece bpawn = b.getSquare("b7").getPiece();

        // pawn moves on move 50
        wknight.move(b.getSquare("c3"));
        bpawn.move(b.getSquare("b5"));
    }

    @Test
    public void testStalemateCapture() throws InvalidMoveException, CheckmateException, StalemateException, InvalidCoordinateException, InvalidTurnException {
        make4nUselessMoves(11);
        Piece wknight = b.getSquare("b1").getPiece();
        Piece wknight2 = b.getSquare("g1").getPiece();
        Piece bknight = b.getSquare("g8").getPiece();
        Piece bknight2 = b.getSquare("b8").getPiece();

        // make 5 more useless moves after 44
        wknight.move(b.getSquare("c3"));
        bknight.move(b.getSquare("f6"));
        wknight2.move(b.getSquare("f3"));
        bknight2.move(b.getSquare("c6"));
        wknight.move(b.getSquare("e4"));

        // capture on move 50
        bknight.move(b.getSquare("e4"));
    }

    // makes 4 * n useless moves
    private void make4nUselessMoves(int n) throws InvalidCoordinateException, InvalidMoveException, CheckmateException, StalemateException, InvalidTurnException {
        Piece wknight = b.getSquare("b1").getPiece();
        Piece bknight = b.getSquare("b8").getPiece();
        for(int i = 0; i < n; i++){
            wknight.move(b.getSquare("c3"));
            bknight.move(b.getSquare("c6"));
            wknight.move(b.getSquare("b1"));
            bknight.move(b.getSquare("b8"));
        }
    }

    private void setUpPromotion() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        b.getSquare("d2").getPiece().move(b.getSquare("d4"));
        b.getSquare("c7").getPiece().move(b.getSquare("c5"));
        b.getSquare("d4").getPiece().move(b.getSquare("c5"));
        b.getSquare("b7").getPiece().move(b.getSquare("b6"));
        b.getSquare("c5").getPiece().move(b.getSquare("b6"));
        b.getSquare("c8").getPiece().move(b.getSquare("b7"));
        b.getSquare("b6").getPiece().move(b.getSquare("a7"));
        b.getSquare("b7").getPiece().move(b.getSquare("c6"));
        b.getSquare("a7").getPiece().move(b.getSquare("b8"));
    }


}