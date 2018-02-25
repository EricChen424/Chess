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
public class PawnTest extends TestMethods{


    @Before
    public void setUp(){
        Board.deleteBoard();
        b = Board.getInstance();
    }


    /**
     * for both black and white
     * first move the pawn makes
     * tests that the piece can move to the correct empty squares and squares with enemies on them
     * tests that the piece cannot move to squares with allies or squares past squares with any units on them
     */
    @Test
    public void testAllMovesEmptySquaresEnemySquare_firstMoveBlack() throws InvalidCoordinateException {
        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("a6"));
        assertEquals(UnitCost.PAWN, b.getSquare("a6").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("a6"));
        testSet.add(b.getSquare("b6"));
        testSet.add(b.getSquare("b5"));

        assertEquals(testSet, pawn.getAllMoves());
    }

    @Test
    public void testAllMovesEmptySquaresEnemySquare_firstMoveWhite() throws InvalidCoordinateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("a3"));
        assertEquals(UnitCost.PAWN, b.getSquare("a3").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("a3"));
        testSet.add(b.getSquare("b3"));
        testSet.add(b.getSquare("b4"));

        assertEquals(testSet, pawn.getAllMoves());
    }

    @Test
    public void testAllMovesSecondSquareBlocked_firstMoveBlack() throws InvalidCoordinateException {
        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("f7").getPiece(), b.getSquare("b5"));
        assertEquals(UnitCost.PAWN, b.getSquare("b5").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b6"));

        assertEquals(testSet, pawn.getAllMoves());
    }

    @Test
    public void testAllMovesSecondSquareBlocked_firstMoveWhite() throws InvalidCoordinateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("g2").getPiece(), b.getSquare("b4"));
        assertEquals(UnitCost.PAWN, b.getSquare("b4").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b3"));

        assertEquals(testSet, pawn.getAllMoves());
    }

    // uses an enemy unit instead to ensure blocking is independent of which side the blocker is on
    @Test
    public void testAllMovesFirstSquareBlocked_firstMoveBlack() throws InvalidCoordinateException {
        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("g2").getPiece(), b.getSquare("b6"));
        assertEquals(UnitCost.PAWN, b.getSquare("b6").getPiece().getUnitCost());

        Set<Square> getAllMoves = pawn.getAllMoves();

        assertEquals(0, getAllMoves.size());

    }

    @Test
    public void testAllMovesFirstSquareBlocked_firstMoveWhite() throws InvalidCoordinateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;

        assertFalse(pawn.getHasMoved());

        b.movePiece(b.getSquare("g7").getPiece(), b.getSquare("b3"));
        assertEquals(UnitCost.PAWN, b.getSquare("b3").getPiece().getUnitCost());

        assertEquals(0, pawn.getAllMoves().size());
    }

    // after double jump
    @Test
    public void testAllMovesEmptySquareEnemySquare_secondMoveBlackTwoJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece k = b.getSquare("b1").getPiece();
        assertEquals(UnitCost.KNIGHT, k.getUnitCost());

        k.move(b.getSquare("c3"));

        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());

        pawn.move(b.getSquare("b5"));

        assertEquals(pawn, b.getSquare("b5").getPiece());
        assertEquals(2, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("a4"));
        assertEquals(UnitCost.PAWN, b.getSquare("a4").getPiece().getUnitCost());

        b.movePiece(b.getSquare("g2").getPiece(), b.getSquare("c4"));
        assertEquals(UnitCost.PAWN, b.getSquare("c4").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("c4"));
        testSet.add(b.getSquare("b4"));
        testSet.add(b.getSquare("a4"));

        pawn.allMoves();

        assertEquals(testSet, pawn.getAllMoves());
    }

    @Test
    public void testAllMovesEmptySquareEnemySquare_secondMoveWhiteTwoJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertEquals(1, b.getTurnNumber());

        pawn.move(b.getSquare("b4"));

        assertEquals(pawn, b.getSquare("b4").getPiece());
        assertEquals(1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("a5"));
        assertEquals(UnitCost.PAWN, b.getSquare("a5").getPiece().getUnitCost());

        b.movePiece(b.getSquare("g7").getPiece(), b.getSquare("c5"));
        assertEquals(UnitCost.PAWN, b.getSquare("c5").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("a5"));
        testSet.add(b.getSquare("b5"));
        testSet.add(b.getSquare("c5"));

        pawn.allMoves();

        assertEquals(testSet, pawn.getAllMoves());
    }

    // blocking after a double jump

    @Test
    public void testAllMovesBlock_secondMoveBlackTwoJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece k = b.getSquare("b1").getPiece();
        assertEquals(UnitCost.KNIGHT, k.getUnitCost());

        k.move(b.getSquare("c3"));

        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());

        pawn.move(b.getSquare("b5"));

        assertEquals(pawn, b.getSquare("b5").getPiece());
        assertEquals(2, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("b4"));
        assertEquals(UnitCost.PAWN, b.getSquare("b4").getPiece().getUnitCost());

        pawn.allMoves();

        assertEquals(0, pawn.getAllMoves().size());
    }

    @Test
    public void testAllMovesBlock_secondMoveWhiteTwoJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertEquals(1, b.getTurnNumber());

        pawn.move(b.getSquare("b4"));

        assertEquals(pawn, b.getSquare("b4").getPiece());
        assertEquals(1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("b5"));
        assertEquals(UnitCost.PAWN, b.getSquare("b5").getPiece().getUnitCost());

        pawn.allMoves();

        assertEquals(0, pawn.getAllMoves().size());
    }

    // after single jump

    @Test
    public void testAllMovesEmptySquareEnemySquare_secondMoveBlackOneJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece k = b.getSquare("b1").getPiece();
        assertEquals(UnitCost.KNIGHT, k.getUnitCost());

        k.move(b.getSquare("c3"));

        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());

        pawn.move(b.getSquare("b6"));

        assertEquals(pawn, b.getSquare("b6").getPiece());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("a5"));
        assertEquals(UnitCost.PAWN, b.getSquare("a5").getPiece().getUnitCost());

        b.movePiece(b.getSquare("g2").getPiece(), b.getSquare("c5"));
        assertEquals(UnitCost.PAWN, b.getSquare("c5").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b5"));
        testSet.add(b.getSquare("a5"));
        testSet.add(b.getSquare("c5"));

        pawn.allMoves();

        assertEquals(testSet, pawn.getAllMoves());
    }

    @Test
    public void testAllMovesEmptySquareEnemySquare_secondMoveWhiteOneJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertEquals(1, b.getTurnNumber());

        pawn.move(b.getSquare("b3"));

        assertEquals(pawn, b.getSquare("b3").getPiece());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("a4"));
        assertEquals(UnitCost.PAWN, b.getSquare("a4").getPiece().getUnitCost());

        b.movePiece(b.getSquare("g7").getPiece(), b.getSquare("c4"));
        assertEquals(UnitCost.PAWN, b.getSquare("c4").getPiece().getUnitCost());

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("a4"));
        testSet.add(b.getSquare("b4"));
        testSet.add(b.getSquare("c4"));

        pawn.allMoves();

        assertEquals(testSet, pawn.getAllMoves());
    }

    // blocking after single jump
    @Test
    public void testAllMovesBlock_secondMoveBlackOneJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        Piece k = b.getSquare("b1").getPiece();
        assertEquals(UnitCost.KNIGHT, k.getUnitCost());

        k.move(b.getSquare("c3"));

        Piece p = b.getSquare("b7").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertEquals(2, b.getTurnNumber());

        pawn.move(b.getSquare("b6"));

        assertEquals(pawn, b.getSquare("b6").getPiece());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("b5"));
        assertEquals(UnitCost.PAWN, b.getSquare("b5").getPiece().getUnitCost());

        pawn.allMoves();

        assertEquals(0, pawn.getAllMoves().size());
    }

    @Test
    public void testAllMovesBlock_secondMoveWhiteOneJump() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException {
        Piece p = b.getSquare("b2").getPiece();
        assertEquals(UnitCost.PAWN, p.getUnitCost());

        Pawn pawn = (Pawn) p;
        assertFalse(pawn.getHasMoved());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertEquals(1, b.getTurnNumber());

        pawn.move(b.getSquare("b3"));

        assertEquals(pawn, b.getSquare("b3").getPiece());
        assertEquals(-1, pawn.getTurnDoubleJumped());
        assertTrue(pawn.getHasMoved());

        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("b4"));
        assertEquals(UnitCost.PAWN, b.getSquare("b4").getPiece().getUnitCost());

        pawn.allMoves();

        assertEquals(0, pawn.getAllMoves().size());
    }

    /**
     * tests en passant
     */
    @Test
    public void testAllMovesEnPassant() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException {
        assertTrue(b.getCurrentPlayer());
        Piece p1 = b.getSquare("b7").getPiece();
        Piece p2 = b.getSquare("c2").getPiece();

        assertEquals(UnitCost.PAWN, p1.getUnitCost());
        assertEquals(UnitCost.PAWN, p2.getUnitCost());

        Pawn bpawn = (Pawn) p1;
        Pawn wpawn = (Pawn) p2;

        assertEquals(1, b.getTurnNumber());

        assertFalse(wpawn.getHasMoved());
        assertEquals(-1, wpawn.getTurnDoubleJumped());

        b.movePiece(bpawn, b.getSquare("b4"));

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b3"));

        assertEquals(testSet, bpawn.getAllMoves());

        assertEquals(bpawn, b.getSquare("b4").getPiece());

        wpawn.move(b.getSquare("c4"));
        assertFalse(b.getCurrentPlayer());
        assertEquals(wpawn, b.getSquare("c4").getPiece());
        assertEquals(2, b.getTurnNumber());
        assertTrue(wpawn.getHasMoved());
        assertEquals(1, wpawn.getTurnDoubleJumped());

        testSet.add(b.getSquare("c3"));
        System.out.print(b);

        assertEquals(testSet, bpawn.getAllMoves());
    }

    /**
     * tests en passant if the player plays another move when he could have done an en passant
     */
    @Test
    public void testAllMovesEnPassantMissedOpportunity() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, StalemateException, CheckmateException {
        assertTrue(b.getCurrentPlayer());
        Piece p1 = b.getSquare("b7").getPiece();
        Piece p2 = b.getSquare("c2").getPiece();

        assertEquals(UnitCost.PAWN, p1.getUnitCost());
        assertEquals(UnitCost.PAWN, p2.getUnitCost());

        Pawn bpawn = (Pawn) p1;
        Pawn wpawn = (Pawn) p2;

        assertEquals(1, b.getTurnNumber());

        assertFalse(wpawn.getHasMoved());
        assertEquals(-1, wpawn.getTurnDoubleJumped());

        b.movePiece(bpawn, b.getSquare("b4"));

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b3"));

        assertEquals(testSet, bpawn.getAllMoves());

        assertEquals(bpawn, b.getSquare("b4").getPiece());

        wpawn.move(b.getSquare("c4"));
        assertFalse(b.getCurrentPlayer());
        assertEquals(2, b.getTurnNumber());
        assertTrue(wpawn.getHasMoved());
        assertEquals(1, wpawn.getTurnDoubleJumped());

        b.getSquare("e7").getPiece().move(b.getSquare("e5"));
        assertTrue(b.getCurrentPlayer());
        assertEquals(3, b.getTurnNumber());
        assertTrue(wpawn.getHasMoved());
        assertEquals(1, wpawn.getTurnDoubleJumped());
        System.out.print(b);

        assertEquals(testSet, bpawn.getAllMoves());
    }

    /**
     * assume it's white's turn
     * tests that the piece can only move to squares that prevent check if the King is in check
     */
    @Test
    public void testAllMovesInCheckBlock() throws InvalidCoordinateException {
        assertTrue(b.getCurrentPlayer());
        Piece p = b.getSquare("h2").getPiece();
        setupBoardPawnCheckBlock(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("c4"));



        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckEliminate() throws InvalidCoordinateException {
        assertTrue(b.getCurrentPlayer());
        Piece p = b.getSquare("h2").getPiece();
        setupBoardPawnCheckEliminate(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("b4"));
        testSet.add(b.getSquare("c4"));

        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckNoMoves() throws InvalidCoordinateException {
        Piece p = b.getSquare("h2").getPiece();
        boardSetupAndCheckNoAction(p);
    }

    @Test
    public void testCanPromote() throws InvalidCoordinateException {
        b.movePiece(b.getSquare("a1").getPiece(), b.getSquare("a3"));
        b.movePiece(b.getSquare("a8").getPiece(), b.getSquare("a6"));
        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("a1"));
        b.movePiece(b.getSquare("a2").getPiece(), b.getSquare("a8"));

        Piece p1 = b.getSquare("a1").getPiece();
        Piece p2 = b.getSquare("a8").getPiece();
        assertTrue(p1.getUnitCost().equals(UnitCost.PAWN));
        assertTrue(p2.getUnitCost().equals(UnitCost.PAWN));

        Pawn pawn1 = (Pawn) p1;
        Pawn pawn2 = (Pawn) p2;
        assertTrue(pawn1.canPromote());
        assertTrue(pawn2.canPromote());
    }
}