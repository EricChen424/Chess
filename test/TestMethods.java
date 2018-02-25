package test;

import exceptions.InvalidCoordinateException;
import model.*;
import org.junit.Test;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/30/2015.
 */
public class TestMethods {
    /**
     * useful helper methods
     */
    protected Board b;

    /**
     * intended actual type of piece is bishop, queen, or rook
     * moves piece to square c5
     * moves the pawn in a7 to h6
     * moves the pawn in f2 to h3
     * @param piece the piece to move to c5
     */

    protected void boardSetup_B_Q_R(Piece piece) throws InvalidCoordinateException {
        Square s = piece.getSquare();
        b.movePiece(piece, b.getSquare("c5"));
        b.movePiece(b.getSquare("a7").getPiece(), b.getSquare("h6"));
        b.movePiece(b.getSquare("f2").getPiece(), b.getSquare("h3"));
        assertEquals(null, s.getPiece());
        assertEquals(piece, b.getSquare("c5").getPiece());
        assertEquals(null, b.getSquare("a7").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("h6").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("f2").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("h3").getPiece().getUnitCost());
    }

    /**
     * returns some common squares for rooks and queens, or bishops and queens for B_Q_R test
     */
    protected Set<Square> expectedDiagonals_B_Q_R() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("b6"));
        s.add(b.getSquare("d6"));
        s.add(b.getSquare("b4"));
        s.add(b.getSquare("d4"));
        s.add(b.getSquare("a3"));
        s.add(b.getSquare("e3"));
        s.add(b.getSquare("a7"));
        s.add(b.getSquare("f2"));
        return s;
    }

    protected Set<Square> expectedLines_B_Q_R() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("c6"));
        s.add(b.getSquare("c4"));
        s.add(b.getSquare("c3"));
        s.add(b.getSquare("a5"));
        s.add(b.getSquare("b5"));
        s.add(b.getSquare("d5"));
        s.add(b.getSquare("e5"));
        s.add(b.getSquare("f5"));
        s.add(b.getSquare("g5"));
        s.add(b.getSquare("h5"));
        return s;
    }

    /**
     * squares exclusive to white (or black) bishops and queens for B_Q_R test
     * */
    protected Set<Square> expectedDiagonals_B_Q_R_Black() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("g1"));
        return s;
    }

    protected Set<Square> expectedDiagonals_B_Q_R_White() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("e7"));
        return s;
    }

    /**
     * squares exclusive to white (or black) rooks and queens for B_Q_R test
     */
    protected Set<Square> expectedLines_B_Q_R_White() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("c7"));
        return s;
    }

    protected Set<Square> expectedLines_B_Q_R_Black() throws InvalidCoordinateException {
        Set<Square> s = new HashSet<Square>();
        s.add(b.getSquare("c2"));
        return s;
    }

    /**
     * sets up the board for various check situations
     * diagonal/cardinal indicates the path the piece takes to save the King from check
     * block/eliminate indicates the method
     * @param p the piece that will be moved
     */

    protected void boardSetupCheckDiagonalBlock(Piece p) throws InvalidCoordinateException {
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("c5"));
        b.movePiece(b.getSquare("e7").getPiece(), b.getSquare("h4"));
        b.movePiece(p, b.getSquare("g3"));
        assertEquals(UnitCost.KING, b.getSquare("c5").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("g3").getPiece());
        assertEquals(null, b.getSquare("e7").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertTrue(b.check());
    }


    protected void boardSetupCheckDiagonalEliminate(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("c5"));
        b.movePiece(b.getSquare("e7").getPiece(), b.getSquare("h4"));
        b.movePiece(p, b.getSquare("g3"));
        assertEquals(UnitCost.KING, b.getSquare("c5").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("g3").getPiece());
        assertEquals(null, b.getSquare("e7").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        b.movePiece(b.getSquare("f8").getPiece(), b.getSquare("d6"));
        assertEquals(UnitCost.BISHOP, b.getSquare("d6").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("f8").getPiece());
        assertTrue(b.check());
    }


    protected void boardSetupCheckCardinalBlock(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("d7").getPiece(), b.getSquare("h6"));
        b.movePiece(p, b.getSquare("g5"));
        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("g5").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("d7").getPiece());
        assertTrue(b.check());
    }


    protected void boardSetupCheckCardinalEliminate(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("d7").getPiece(), b.getSquare("h6"));
        b.movePiece(p, b.getSquare("g5"));
        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("g5").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("d7").getPiece());
        b.movePiece(b.getSquare("d8").getPiece(), b.getSquare("d5"));
        assertEquals(UnitCost.QUEEN, b.getSquare("d5").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("d8").getPiece());
        assertTrue(b.check());
    }

    /**
     * sets up board for no action test and tests that p has 0 moves it can make to prevent check
     */

    protected void boardSetupAndCheckNoAction(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("b8").getPiece(), b.getSquare("c6"));
        b.movePiece(p, b.getSquare("b4"));

        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, b.getSquare("c6").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("b4").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("b8").getPiece());

        assertTrue(b.getCurrentPlayer());
        assertTrue(b.check());

        assertEquals(0, p.getAllMoves().size());
    }


    protected void boardSetupAndCheckNoActionKnight(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("b8").getPiece(), b.getSquare("e6"));
        b.movePiece(p, b.getSquare("b4"));

        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(UnitCost.KNIGHT, b.getSquare("e6").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("b4").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertEquals(null, b.getSquare("b8").getPiece());

        assertTrue(b.getCurrentPlayer());
        assertTrue(b.check());

        assertEquals(0, p.getAllMoves().size());
    }

    /**
     * sets up board for knight movement test
     * @param k a knight
     */

    protected void boardSetupKnight(Piece k) throws InvalidCoordinateException {
        b.movePiece(k, b.getSquare("b5"));
        b.movePiece(b.getSquare("c1").getPiece(), b.getSquare("a3"));
        b.movePiece(b.getSquare("c2").getPiece(), b.getSquare("c3"));

        assertEquals(k, b.getSquare("b5").getPiece());
        assertEquals(null, b.getSquare("c1").getPiece());
        assertEquals(null, b.getSquare("c2").getPiece());
        assertEquals(UnitCost.BISHOP, b.getSquare("a3").getPiece().getUnitCost());
        assertEquals(UnitCost.PAWN, b.getSquare("c3").getPiece().getUnitCost());
    }

    /**
     * sets up board for knight check testing
     */

    protected void boardSetupCheckKnightBlock(Piece p) throws InvalidCoordinateException {
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("c5"));
        b.movePiece(b.getSquare("e7").getPiece(), b.getSquare("h4"));
        b.movePiece(p, b.getSquare("e4"));
        assertEquals(UnitCost.KING, b.getSquare("c5").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("e4").getPiece());
        assertEquals(null, b.getSquare("e7").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        assertTrue(b.check());
    }


    protected void boardSetupCheckKnightEliminate(Piece p) throws InvalidCoordinateException{
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("c5"));
        b.movePiece(b.getSquare("e7").getPiece(), b.getSquare("h4"));
        b.movePiece(p, b.getSquare("e4"));
        assertEquals(UnitCost.KING, b.getSquare("c5").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("e4").getPiece());
        assertEquals(null, b.getSquare("e7").getPiece());
        assertEquals(null, b.getSquare("e1").getPiece());
        b.movePiece(b.getSquare("f8").getPiece(), b.getSquare("d6"));
        assertEquals(UnitCost.BISHOP, b.getSquare("d6").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("f8").getPiece());

    }


    protected void boardSetupWhiteKing(Piece p) throws InvalidCoordinateException {
        b.movePiece(p, b.getSquare("a4"));
        b.movePiece(b.getSquare("c7").getPiece(), b.getSquare("h5")); // move pawn to free queen
        b.movePiece(b.getSquare("b2").getPiece(), b.getSquare("b3")); // move friendly pawn to block king
        b.movePiece(b.getSquare("b8").getPiece(), b.getSquare("b4")); // move horse
        assertEquals(p, b.getSquare("a4").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("b3").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("c7").getPiece());
        assertEquals(UnitCost.KNIGHT, b.getSquare("b4").getPiece().getUnitCost());
    }


    protected void boardSetupBlackKing(Piece p) throws InvalidCoordinateException {
        b.movePiece(p, b.getSquare("a5"));
        b.movePiece(b.getSquare("c2").getPiece(), b.getSquare("h5"));
        b.movePiece(b.getSquare("b7").getPiece(), b.getSquare("b6"));
        b.movePiece(b.getSquare("b1").getPiece(), b.getSquare("b5"));
        assertEquals(p, b.getSquare("a5").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("b6").getPiece().getUnitCost());
        assertEquals(null, b.getSquare("c2").getPiece());
        assertEquals(UnitCost.KNIGHT, b.getSquare("b5").getPiece().getUnitCost());
    }

    /**
     * clears all units between white rooks and white king
     */

    protected void boardSetupCastle() throws InvalidCoordinateException {
        b.movePiece(b.getSquare("b1").getPiece(), b.getSquare("b3"));
        b.movePiece(b.getSquare("c1").getPiece(), b.getSquare("c3"));
        b.movePiece(b.getSquare("d1").getPiece(), b.getSquare("d3"));
        b.movePiece(b.getSquare("f1").getPiece(), b.getSquare("f3"));
        b.movePiece(b.getSquare("g1").getPiece(), b.getSquare("g3"));
        //b.movePiece(b.getSquare("e2").getPiece(), b.getSquare("a4"));
        assertEquals(null, b.getSquare("b1").getPiece());
        assertEquals(null, b.getSquare("c1").getPiece());
        assertEquals(null, b.getSquare("d1").getPiece());
        assertEquals(null, b.getSquare("f1").getPiece());
        assertEquals(null, b.getSquare("g1").getPiece());
        //assertEquals(null, b.getSquare("e2").getPiece());

        Rook rook1 = (Rook) b.getSquare("a1").getPiece();
        Rook rook2 = (Rook) b.getSquare("h1").getPiece();
        King king = (King) b.getSquare("e1").getPiece();

        assertEquals(UnitCost.ROOK, rook1.getUnitCost());
        assertEquals(UnitCost.ROOK, rook2.getUnitCost());
        assertEquals(UnitCost.KING, king.getUnitCost());
        assertFalse(king.getHasMoved());
        assertFalse(rook1.getHasMoved());
        assertFalse(rook1.getHasMoved());
    }

    protected void setupBoardPawnCheckBlock(Piece p) throws InvalidCoordinateException {
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("a8").getPiece(), b.getSquare("a4"));
        b.movePiece(p, b.getSquare("c3"));
        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("c3").getPiece());
        assertEquals(UnitCost.ROOK, b.getSquare("a4").getPiece().getUnitCost());
        assertTrue(b.check());
    }

    protected void setupBoardPawnCheckEliminate(Piece p) throws InvalidCoordinateException {
        b.movePiece(b.getSquare("e1").getPiece(), b.getSquare("d4"));
        b.movePiece(b.getSquare("a8").getPiece(), b.getSquare("b4"));
        b.movePiece(p, b.getSquare("c3"));
        assertEquals(UnitCost.KING, b.getSquare("d4").getPiece().getUnitCost());
        assertEquals(p, b.getSquare("c3").getPiece());
        assertEquals(UnitCost.ROOK, b.getSquare("b4").getPiece().getUnitCost());
        assertTrue(b.check());
    }
}
