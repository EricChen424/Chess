package test;

import exceptions.*;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/28/2015.
 */
public class PieceTest {

    Square s;
    Piece p;
    Piece p1;
    Board b;

    @Before
    public void setUp(){
        Board.deleteBoard();
        b = Board.getInstance();
        s = new Square(true, 1, 2);
        p = new King(false, s);
        p1 = new Rook(false, null);
    }

    @Test
    public void testConstructor(){
        assertEquals(100, p.getHealth());
        assertEquals(UnitCost.KING, p.getUnitCost());
        assertFalse(p.getSide());
        assertEquals(s, p.getSquare());
        assertEquals(p, s.getPiece());
        assertFalse(p.isEliminated());
    }

    @Test
    public void testSetSquareNoInitialSquare(){
        Square dummySquare = new Square(true, 2, 3);

        assertEquals(null, p1.getSquare());
        assertEquals(null, dummySquare.getPiece());

        p1.setSquare(dummySquare);

        assertEquals(dummySquare, p1.getSquare());
        assertEquals(p1, dummySquare.getPiece());
    }

    @Test
    public void testSetSquare(){
        Square dummySquare = new Square(true, 2, 3);

        assertEquals(s, p.getSquare());
        assertEquals(null, dummySquare.getPiece());
        assertEquals(p, s.getPiece());

        p.setSquare(dummySquare);

        assertEquals(dummySquare, p.getSquare());
        assertEquals(p, dummySquare.getPiece());
        assertEquals(null, s.getPiece());
    }

    @Test
    public void testSetSquareNull() {
        assertFalse(p.isEliminated());
        assertEquals(s, p.getSquare());
        assertEquals(p, s.getPiece());

        p.setSquare(null);

        assertTrue(p.isEliminated());
        assertEquals(null, p.getSquare());
        assertEquals(null, s.getPiece());
    }

    @Test
    public void testEliminate() {
        assertFalse(p.isEliminated());
        assertEquals(s, p.getSquare());
        assertEquals(p, s.getPiece());

        p.eliminate();

        assertTrue(p.isEliminated());
        assertEquals(null, p.getSquare());
        assertEquals(null, s.getPiece());
    }

    @Test
    public void testMove() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException, StalemateException {
        assertTrue(b.getCurrentPlayer());

        Piece p1 = b.getSquare("e2").getPiece();
        assertTrue(p1.getSide());
        Set<Square> moves = p1.getAllMoves();
        assertTrue(moves.contains(b.getSquare("e4")));
        p1.move(b.getSquare("e4"));
        assertEquals(null, b.getSquare("e2").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("e4").getPiece().getUnitCost());
        assertTrue(b.getSquare("e4").getPiece().getSide());

        assertFalse(b.getCurrentPlayer());
        Piece p2 = b.getSquare("e7").getPiece();
        assertFalse(p2.getSide());
        assertTrue(p2.getAllMoves().contains(b.getSquare("e6")));
        p2.move(b.getSquare("e6"));
        assertEquals(null, b.getSquare("e7").getPiece());
        assertEquals(UnitCost.PAWN, b.getSquare("e6").getPiece().getUnitCost());
        assertFalse(b.getSquare("e6").getPiece().getSide());

        assertTrue(b.getCurrentPlayer());
    }

    @Test (expected = InvalidMoveException.class)
    public void testMoveInvalidSquare() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException, StalemateException {
        assertTrue(b.getCurrentPlayer());

        Piece p1 = b.getSquare("e2").getPiece();
        assertTrue(p1.getSide());
        assertFalse(p1.getAllMoves().contains(b.getSquare("c3")));
        p1.move(b.getSquare("c3"));
    }

    @Test (expected = InvalidTurnException.class)
    public void testMoveWrongTurn() throws InvalidCoordinateException, InvalidMoveException, InvalidTurnException, CheckmateException, StalemateException {
        assertTrue(b.getCurrentPlayer());

        Piece p1 = b.getSquare("e7").getPiece();
        assertFalse(p1.getSide());
        assertTrue(p1.getAllMoves().contains(b.getSquare("e6")));
        p1.move(b.getSquare("e6"));
    }
}