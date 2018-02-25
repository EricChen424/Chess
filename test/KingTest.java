package test;

import exceptions.*;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/22/2015.
 */
public class KingTest extends TestMethods{


    @Before
    public void setUp(){
        Board.deleteBoard();
        b = Board.getInstance();
    }

    /**
     * tests that the piece can move to the correct empty squares and squares with enemies on them
     * tests that the piece cannot move to squares with allies or squares past squares with any units on them
     * tests that the piece cannot move to squares that threaten check
     */
    @Test
    public void testAllMovesBlockedEmptySquareEnemySquareWhiteCheckThreat() throws InvalidCoordinateException {
        Piece p = b.getSquare("e1").getPiece();
        boardSetupWhiteKing(p);

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("b5"));
        testSet.add(b.getSquare("b4"));
        testSet.add(b.getSquare("a3"));

        assertEquals(testSet, p.getAllMoves());
    }

    /**
     * tests that the piece can only move to squares that prevent check if the King is in check
     */
    @Test
    public void testAllMovesInCheck() throws InvalidCoordinateException {
        Piece p = b.getSquare("e1").getPiece();

        b.movePiece(p, b.getSquare("c4"));
        b.movePiece(b.getSquare("d7").getPiece(), b.getSquare("h4"));
        b.movePiece(b.getSquare("a8").getPiece(), b.getSquare("c5"));
        assertEquals(p, b.getSquare("c4").getPiece());
        assertEquals(null, b.getSquare("d7").getPiece());
        assertEquals(UnitCost.ROOK, b.getSquare("c5").getPiece().getUnitCost());

        assertTrue(b.check());

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("c5"));
        testSet.add(b.getSquare("b4"));
        testSet.add(b.getSquare("b3"));

        assertEquals(testSet, p.getAllMoves());
    }

    /**
     * tests that the King can't castle in check
     */
    @Test
    public void testAllNoCastleInCheck() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException {
        boardSetupCastle();

        King k = testCastleSetup();

        b.movePiece(b.getSquare("e2").getPiece(), b.getSquare("a3"));
        b.movePiece(b.getSquare("d8").getPiece(), b.getSquare("e3"));

        assertTrue(b.check());
        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("f1"));

        assertEquals(testSet, k.getAllMoves());
    }

    /**
     * tests that the piece can castle if the squares between King and Rook are free and neither piece has moved yet
     */
    @Test
    public void testAllMovesThenLeftCastle() throws InvalidCoordinateException, InvalidMoveException,
            InvalidTurnException, CheckmateException, StalemateException {
        boardSetupCastle();

        King k = testCastleSetup();
        k.move(b.getSquare("c1"));

        assertEquals(b.getSquare("c1"), k.getSquare());
        assertEquals(UnitCost.ROOK, b.getSquare("d1").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("a1").getPiece());
    }

    @Test
    public void testAllMovesThenRightCastle() throws InvalidCoordinateException, InvalidMoveException,
            InvalidTurnException, CheckmateException, StalemateException {
        boardSetupCastle();

        King k = testCastleSetup();
        k.move(b.getSquare("g1"));

        assertEquals(b.getSquare("g1"), k.getSquare());
        assertEquals(UnitCost.ROOK, b.getSquare("f1").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("h1").getPiece());
    }

    @Test
    public void testAllMovesCannotCastleKingMoved() throws InvalidCoordinateException, InvalidMoveException,
            InvalidTurnException, CheckmateException, StalemateException {
        boardSetupCastle();

        King k = testCastleSetup();

        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        k.move(b.getSquare("d1"));
        assertTrue(k.getHasMoved());
        assertFalse(rook1.getHasMoved());
        assertFalse(rook2.getHasMoved());

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("e1"));
        testSet.add(b.getSquare("c1"));

        assertEquals(testSet, k.getAllMoves());
    }

    @Test
    public void testAllMovesCannotLeftCastleRookMoved() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        boardSetupCastle();

        King k = testCastleSetup();


        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        noneMoved(k, rook1, rook2);

        rook1.move(b.getSquare("b1"));

        assertTrue(rook1.getHasMoved());
        assertFalse(k.getHasMoved());
        assertFalse(rook2.getHasMoved());

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("f1"));
        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("g1"));

        assertEquals(testSet, k.getAllMoves());
    }

    @Test
    public void testAllMovesCannotLeftCastleNoSpace() throws InvalidCoordinateException {
        boardSetupCastle();

        King k = testCastleSetup();
        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        noneMoved(k, rook1, rook2);

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("f1"));
        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("g1"));

        Piece p = b.getSquare("b2").getPiece();

        // testing each square individually
        b.movePiece(p, b.getSquare("b1"));
        assertEquals(UnitCost.PAWN, b.getSquare("b1").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        assertEquals(testSet, k.getAllMoves());

        b.movePiece(p, b.getSquare("c1"));
        assertEquals(null, b.getSquare("b1").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("c1").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        assertEquals(testSet, k.getAllMoves());

        b.movePiece(p, b.getSquare("d1"));
        assertEquals(null, b.getSquare("c1").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("d1").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        testSet.remove(b.getSquare("d1"));
        k.allMoves();
        assertEquals(testSet, k.getAllMoves());

        // seeing if resetting the squares fixes it
        b.movePiece(p, b.getSquare("b2"));
        assertEquals(null, b.getSquare("d1").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("b2").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("c1"));
        k.allMoves();
        assertEquals(testSet, k.getAllMoves());
    }

    @Test
    public void testAllMovesCannotRightCastleRookMoved() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        boardSetupCastle();

        King k = testCastleSetup();

        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        noneMoved(k, rook1, rook2);

        rook2.move(b.getSquare("g1"));

        assertTrue(rook2.getHasMoved());
        assertFalse(k.getHasMoved());
        assertFalse(rook1.getHasMoved());

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("f1"));
        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("c1"));

        assertEquals(testSet, k.getAllMoves());
    }

    @Test
    public void testAllMovesCannotRightCastleNoSpace() throws InvalidCoordinateException {
        boardSetupCastle();

        King k = testCastleSetup();
        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        Piece p = b.getSquare("b2").getPiece();

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("c1"));
        testSet.add(b.getSquare("d1"));
        testSet.add(b.getSquare("f1"));

        // testing each square individually
        b.movePiece(p, b.getSquare("g1"));
        assertEquals(UnitCost.PAWN, b.getSquare("g1").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        assertEquals(testSet, k.getAllMoves());

        b.movePiece(p, b.getSquare("f1"));
        assertEquals(null, b.getSquare("g1").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("f1").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        testSet.remove(b.getSquare("f1"));
        k.allMoves();
        assertEquals(testSet, k.getAllMoves());

        // seeing if resetting the squares fixes it
        b.movePiece(p, b.getSquare("b2"));
        assertEquals(null, b.getSquare("f1").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("b2").getPiece().getUnitCost());

        noneMoved(k, rook1, rook2);
        testSet.add(b.getSquare("f1"));
        testSet.add(b.getSquare("g1"));
        k.allMoves();
        assertEquals(testSet, k.getAllMoves());
    }

    // checks that the King has its entire row free except the Rooks, can castle, and returns the King
    public King testCastleSetup() throws InvalidCoordinateException{
        Piece k = b.getSquare("e1").getPiece();
        assertEquals(UnitCost.KING, k.getUnitCost());
        King king = (King) k;

        Rook rook1 = getLeftRook();
        Rook rook2 = getRightRook();

        assertFalse(king.getHasMoved());
        assertFalse(rook1.getHasMoved());
        assertFalse(rook2.getHasMoved());

        return king;
    }

    // gets left rook
    public Rook getLeftRook() throws InvalidCoordinateException {
        Piece r1 = b.getSquare("a1").getPiece();
        assertEquals(UnitCost.ROOK, r1.getUnitCost());
        return (Rook) r1;
    }

    // gets right rook
    public Rook getRightRook() throws InvalidCoordinateException {
        Piece r1 = b.getSquare("h1").getPiece();
        assertEquals(UnitCost.ROOK, r1.getUnitCost());
        return (Rook) r1;
    }

    /**
     * ensures neither rooks nor king have moved
     */
    public void noneMoved(King k, Rook r1, Rook r2){
        assertFalse(r2.getHasMoved());
        assertFalse(r1.getHasMoved());
        assertFalse(k.getHasMoved());
    }
}