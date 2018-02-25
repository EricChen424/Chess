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
public class BishopTest extends TestMethods{


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
        Piece p = b.getSquare("f8").getPiece();
        boardSetup_B_Q_R(p);

        Set<Square> testSet = new HashSet<Square>();

        testSet.addAll(expectedDiagonals_B_Q_R());
        testSet.addAll(expectedDiagonals_B_Q_R_Black());
        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesBlockedEmptySquareEnemySquareWhite() throws InvalidCoordinateException {
        Piece p = b.getSquare("c1").getPiece();
        boardSetup_B_Q_R(p);

        Set<Square> testSet = new HashSet<Square>();

        testSet.addAll(expectedDiagonals_B_Q_R());
        testSet.addAll(expectedDiagonals_B_Q_R_White());
        assertEquals(testSet, p.getAllMoves());
    }

    /**
     * assume it's white's turn
     * tests that the piece can only move to squares that prevent check if the King is in check
     */
    @Test
    public void testAllMovesInCheckBlock() throws InvalidCoordinateException {
        Piece p = b.getSquare("c1").getPiece();
        assertTrue(b.getCurrentPlayer());
        boardSetupCheckDiagonalBlock(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("d6"));

        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckEliminate() throws InvalidCoordinateException{
        Piece p = b.getSquare("c1").getPiece();
        assertTrue(b.getCurrentPlayer());
        boardSetupCheckDiagonalEliminate(p);

        Set<Square> testSet = new HashSet<Square>();
        testSet.add(b.getSquare("d6"));

        assertEquals(testSet, p.getAllMoves());
    }

    @Test
    public void testAllMovesInCheckNoMoves() throws InvalidCoordinateException {
        Piece p = b.getSquare("c1").getPiece();
        boardSetupAndCheckNoAction(p);
    }
}