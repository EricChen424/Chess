package ui.terminalchess;

import model.Board;
import model.Piece;

import java.util.List;

/**
 * Created by Eric on 5/1/2016.
 */
public class Scoreboard {
    private static Scoreboard theScoreboard;
    private Board board;

    private Scoreboard(){
        board = Board.getInstance();
    }

    public static Scoreboard getInstance(){
        if(theScoreboard == null){
            theScoreboard = new Scoreboard();
            return theScoreboard;
        } else{
            return theScoreboard;
        }
    }

    @Override
    public String toString(){
        List<Piece> whiteGrave = board.getSideGraveyard(true);
        List<Piece> blackGrave = board.getSideGraveyard(false);
        int whiteScore = 0;
        int blackScore = 0;
        for(Piece p : whiteGrave){
            whiteScore += p.getCost();
        }
        for(Piece p : blackGrave){
            blackScore += p.getCost();
        }
        String s = "White Score: " + whiteScore + System.lineSeparator();
        s += "Black Score: " + blackScore + System.lineSeparator();

        return s;
    }
}
