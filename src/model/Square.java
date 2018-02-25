package model;

import java.util.Arrays;

/**
 * Created by Eric on 12/22/2015.
 */
public class Square {
    private Piece myPiece;
    private boolean myColour;
    private int row;
    private int column;

    public Square(boolean colour, int row, int column){
        myColour = colour;
        this.row = row;
        this.column = column;
    }

    public boolean getColour(){
        return myColour;
    }

    public Piece getPiece(){
        return myPiece;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    /**
     * eliminates the current piece of this square if non-null and sets it to newPiece
     * does nothing if newPiece is the exact same object as myPiece
     * @param newPiece the new piece that belongs on this square
     */
    public void setPiece(Piece newPiece){
        if(newPiece == myPiece){
            return;
        }
        if(myPiece == null){
            myPiece = newPiece;
            if(newPiece != null) {
                newPiece.setSquare(this);
            }
        } else{
            Piece p = myPiece;
            myPiece = null;
            if(newPiece != null) {
                p.eliminate();
            }
            myPiece = newPiece;
            if((newPiece != null) && (!newPiece.getSquare().equals(this))) {
                newPiece.setSquare(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (myColour != square.myColour) return false;
        if (row != square.row) return false;
        return column == square.column;

    }

    @Override
    public int hashCode() {
        int result = (myColour ? 1 : 0);
        result = 31 * result + row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString(){
        String squareString = "";
        switch(column){
            case 0:
                squareString += "a";
                break;
            case 1:
                squareString += "b";
                break;
            case 2:
                squareString += "c";
                break;
            case 3:
                squareString += "d";
                break;
            case 4:
                squareString += "e";
                break;
            case 5:
                squareString += "f";
                break;
            case 6:
                squareString += "g";
                break;
            case 7:
                squareString += "h";
                break;
        }
        squareString += (8 - row);
        return squareString;
    }
}

