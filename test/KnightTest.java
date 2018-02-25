package test;

import exceptions.InvalidCoordinateException;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Eric on 12/22/2015.
 */
public class KnightTest extends TestMethods{


    @Before
    public void setUp(){
        Board.deleteBoard();
        b = Board.getInstance();
    }


    /**
     * for both black and white
     * tests that the piece can move to the correct empty squares and squares with enemies on them
     * tests that the piece cannot move to squares with allies or squares past squares with any units on them
     */

    @Test
    public void testAllMovesBlockedEmptySquareEnemySquareBlack() throws InvalidCoordinateException {
        Piece bknight = b.getSquare("b8").getPiece();
        boardSetupKnight(bknight);

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("a3"));
        testSet.add(b.getSquare("c3"));
        testSet.add(b.getSquare("d6"));
        testSet.add(b.getSquare("d4"));

        assertEquals(testSet, bknight.getAllMoves());
    }

    @Test
    public void testAllMovesBlockedEmptySquareEnemySquareWhite() throws InvalidCoordinateException {
        Piece wknight = b.getSquare("b1").getPiece();
        boardSetupKnight(wknight);

        Set<Square> testSet = new HashSet<Square>();

        testSet.add(b.getSquare("a7"));
        testSet.add(b.getSquare("c7"));
        testSet.add(b.getSquare("d6"));
        testSet.add(b.getSquare("d4"));

        assertEquals(testSet, wknight.getAllMoves());
    }

    /**
     * assume it's white's turn
     * tests that the piece can only move to squares that prevent check if the King is in check
     */
    @Test
    public void testAllMovesInCheckBlock() throws InvalidCoordinateException {
        Piece p = b.getSquare("b1").getPiece();
        assertTrue(b.getCurrentPlayer());
        boardSetupCheckKnightBlock(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("d6"));
        System.out.print(b.toString());

        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckEliminate() throws InvalidCoordinateException {
        Piece p = b.getSquare("b1").getPiece();
        assertTrue(b.getCurrentPlayer());
        boardSetupCheckKnightEliminate(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("d6"));
        assertTrue(b.check());

        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckNoMoves() throws InvalidCoordinateException {
        Piece p = b.getSquare("b1").getPiece();
        boardSetupAndCheckNoActionKnight(p);
    }
}