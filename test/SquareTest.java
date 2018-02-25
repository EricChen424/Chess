package test;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/22/2015.
 */
public class SquareTest {
    private Square s;

    @Before
    public void setUp(){
        s = new Square(false, 1, 2);
    }

    @Test
    public void testSetPieceEmptySquare(){
        Square dummySquare = new Square(false, 2, 3);
        Pawn p = new Pawn(true, dummySquare);

        assertEquals(dummySquare, p.getSquare());
        assertEquals(p, dummySquare.getPiece());
        assertEquals(null, s.getPiece());

        s.setPiece(p);

        assertEquals(null, dummySquare.getPiece());
        assertEquals(s, p.getSquare());
        assertEquals(p, s.getPiece());
    }

    @Test
    public void testSetPieceOccupiedSquare(){
        Square dummySquare = new Square(false, 2, 3);
        Pawn p = new Pawn(true, dummySquare);
        King dummyPiece = new King(false, s);

        assertEquals(dummySquare, p.getSquare());
        assertEquals(p, dummySquare.getPiece());
        assertEquals(dummyPiece, s.getPiece());
        assertEquals(s, dummyPiece.getSquare());

        s.setPiece(p);

        assertEquals(s, p.getSquare());
        assertEquals(null, dummySquare.getPiece());
        assertEquals(p, s.getPiece());
        assertEquals(null, dummyPiece.getSquare());
    }
}