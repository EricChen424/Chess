package ui.terminalchess;


import model.*;
import exceptions.*;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Eric on 5/1/2016.
 */
public class TerminalChess {
    private static Scanner sc = new Scanner(System.in);
    private static Board b = Board.getInstance();
    private static Scoreboard sb = Scoreboard.getInstance();

    // we should never get InvalidCoordinateException or InvalidPromotionException (logic error) so game will crash if they occur
    public static void main(String[] args) throws InvalidCoordinateException, InvalidPromotionException {
        while(true){
            System.out.println(b);
            if(b.getPromotable() != null){
                handlePromotion();
                System.out.println(b);
            }
            printTurn();
            String command = sc.next();
            try {
                commandParser(command);
            } catch (QuitException e) {
                System.out.println("Terminating...");
                break;
            } catch (StalemateException e) {
                System.out.println(b);
                System.out.println(e.getMessage());
                System.out.println("Both players draw!");
                break;
            } catch (CheckmateException e) {
                System.out.println(b);
                System.out.println(e.getMessage());
                if(b.getCurrentPlayer()){
                    System.out.println("Black wins!");
                } else{
                    System.out.println("White wins!");
                }
                break;
            }
        }
    }

    private static void handlePromotion() throws InvalidPromotionException {
        Pawn promotable = b.getPromotable();
        System.out.println("Pawn at " + promotable.getSquare() + " can be promoted.");
        System.out.println("Enter 'q' [Queen], 'r' [Rook], 'b' [Bishop], or 'k' [Knight] to promote to respective unit: ");

        String toPromote = sc.next();
        try {
            b.promote(toPromote);
        } catch (InvalidPromotionInputException e) {
            System.out.println(e.getMessage());
            handlePromotion();
        }
    }

    private static void printTurn(){
        if (b.getInCheck()) {
            System.out.println("Check!");
        }
        if(b.getCurrentPlayer()){
            System.out.println("White's turn.");
        } else{
            System.out.println("Black's turn.");
        }
        System.out.println("Enter command (enter 'help' for list of commands): ");
    }

    /**
     * tells the board to do different commands depending on the input s
     * @param s the input
     */
    private static void commandParser(String s) throws QuitException, InvalidCoordinateException, StalemateException, CheckmateException {
        String temp = s.toLowerCase();
        while(!isMoveCommand(temp)){
            if(temp.equals("help")){
                printHelpList();
            } else if((temp.length() == 4) && temp.substring(0,2).equals("am") &&
                    checkLetter(temp.substring(2,3)) && checkNumber(temp.substring(3,4))){
                Square allMovesSquare = null;
                allMovesSquare = b.getSquare((temp.substring(2,4)));
                printAllMoves(allMovesSquare);
            } else if(temp.equals("score")){
                System.out.println(sb);
            } else if(temp.equals("board")){
                System.out.println(b);
            } else if(temp.equals("quit")){
                throw new QuitException();
            } else{
                System.out.println("Invalid command. Please re-enter command or enter 'help' for list of commands.");
            }
            printTurn();
            temp = sc.next().toLowerCase();
        }
        String mover = temp.substring(0,2);
        String movee = temp.substring(2,4);
        try {
            b.getSquare(mover).getPiece().move(b.getSquare(movee));
        } catch (InvalidMoveException e) {
            System.out.println("Invalid move. Please enter a valid command.");
            String nextAttempt = sc.next();
            commandParser(nextAttempt);
        } catch (InvalidTurnException e) {
            System.out.println("It's not this unit's turn. Please enter a valid command.");
            String nextAttempt = sc.next();
            commandParser(nextAttempt);
        }
    }

    private static void printAllMoves(Square allMovesSquare) {
        Piece p = allMovesSquare.getPiece();
        if(p == null){
            System.out.println("There is no unit at this square");
        } else{
            Set<Square> allMoves = p.getAllMoves();
            if(allMoves.size() == 0){
                System.out.println("No moves available for the unit at " + allMovesSquare);
            } else{
                System.out.println("The following moves are valid:");
                for(Square s : allMoves){
                    System.out.println(s);
                }
            }
        }
    }

    private static void printHelpList() {
        System.out.println("List of commands:");
        System.out.println("'xoxo' (eg. a7b3) - 'x' must be a letter from a-h, 'o' must be a number from 1-7; moves the unit" +
                "at from square at the first 'xo' to the square at the second 'xo'");
        System.out.println("'amxo' (eg. amb3) - prints all moves that the unit at b3 can perform");
        System.out.println("'help' - print list of commands");
        System.out.println("'score' - print score for both players");
        System.out.println("'board' - reprint the board");
        System.out.println("'quit' - end the game");

    }

    /**
     * @param s the string to check
     * @return true if string s is of the form 'anan' where a is a letter 'a-h' and
     * n is a number '1-8'
     */
    private static boolean isMoveCommand(String s){
        String temp = s.toLowerCase();
        if(temp.length() != 4){
            return false;
        }
        String firstChar = temp.substring(0,1);
        String secondChar = temp.substring(1,2);
        String thirdChar = temp.substring(2,3);
        String fourthChar = temp.substring(3,4);
        if(checkLetter(firstChar) && checkLetter(thirdChar) && checkNumber(secondChar)
                && checkNumber(fourthChar)){
            return true;
        }
        return false;
    }

    /**
     * @param s a String to check
     * @return true if s is a character from 1-8
     */
    private static boolean checkNumber(String s) {
        if(s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") ||
                s.equals("6") || s.equals("7") || s.equals("8")){
            return true;
        }
        return false;
    }

    /**
     * @param s a String to check
     * @return true if s is a character from a-h
     */
    private static boolean checkLetter(String s) {
        if(s.equals("a") || s.equals("b") || s.equals("c") || s.equals("d") || s.equals("e") ||
                s.equals("f") || s.equals("g") || s.equals("h")){
            return true;
        }
        return false;
    }
}
