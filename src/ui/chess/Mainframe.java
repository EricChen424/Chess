package ui.chess;

import exceptions.*;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Eric on 5/3/2016.
 */
public class Mainframe extends JFrame {
    private JButton mainWindowExit;
    private JPanel boardPanel;
    private ChessMouseEvent mouseEvent;

    private Piece selected;
    private Board board;

    public static final int SQUARE_DIMENSION = 50;
    public static final Color DARK_BROWN = new Color(0xBF7935);
    public static final Color LIGHT_BROWN = new Color(0xFFCE9E);
    public static final Color GREEN = Color.GREEN;
    public static final Color YELLOW = new Color(0xEEAD0E);
    public static final Color RED = Color.RED;
    public static final Color BLACK = Color.BLACK;

    private class ChessMouseEvent implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Square selectedSquare = findSquare(x, y);
            if(selected == null){ // if no piece has been selected yet
                selected = selectedSquare.getPiece();
                if(selected != null){ // not an empty square
                    // repaint with allMoves displayed
                    boardPanel.repaint();
                }
            } else{ // otherwise we're making a move, make the move then deselect selected
                try {
                    selected.move(selectedSquare);
                } catch (InvalidMoveException e1) {
                    // cancel and update display to deselect the piece
                } catch (InvalidTurnException e1) {
                    // cancel and update display to deselect the piece
                } catch (InvalidCoordinateException e1) {
                    e1.printStackTrace(); // should never get here
                } catch (CheckmateException e1) {
                    e1.printStackTrace(); // end game // TODO: end game and print checkmate
                } catch (StalemateException e1) {
                    e1.printStackTrace(); // end game // TODO: end game and print stalemate
                }
                selected = null;
                boardPanel.repaint();
            }
        }

        // finds the square on the board that contains (x,y) and return it
        private Square findSquare(int x, int y) {
            int col = x/50;
            int row = y/50;
            return board.getSquares()[row][col];
        }

        @Override
        public void mousePressed(MouseEvent e) {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            return;
        }
    }

    private class BoardPanel extends JPanel{

        public BoardPanel(BorderLayout borderLayout){
            super();
            addMouseListener(mouseEvent);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            drawBoard(g);
        }

        // draws the board
        private void drawBoard(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Square[][] grid = board.getSquares();
            for(int i = 0; i < Board.BOARD_DIMENSION; i++){
                for(int j = 0; j < Board.BOARD_DIMENSION; j++){
                    drawSquare(grid[i][j], j, i, g2);
                }
            }
        }

        // draws square s and its piece
        // TODO: later on need to draw red squares for moves that will eliminate, and green squares for valid moves
        private void drawSquare(Square s, int i, int j, Graphics2D g2) {
            if(s.getColour()){
                g2.setPaint(LIGHT_BROWN);
            } else{
                g2.setPaint(DARK_BROWN);
            }
            if((selected != null) && (selected.getAllMoves().contains(s))){
                if(s.getPiece() != null){
                    g2.setPaint(RED);
                } else{
                    if(selected.getSide() == board.getCurrentPlayer()) {
                        g2.setPaint(GREEN);
                    } else{
                        g2.setPaint(YELLOW);
                    }
                }
            }
            g2.fillRect(i*50, j*50, SQUARE_DIMENSION, SQUARE_DIMENSION);
            g2.setPaint(BLACK);
            g2.drawRect(i*50, j*50, SQUARE_DIMENSION, SQUARE_DIMENSION);
            drawPiece(s.getPiece(), i, j, g2);
        }

        // draws Piece p
        private void drawPiece(Piece p, int i, int j, Graphics2D g2) {
            if(p != null) {
                BufferedImage img = null;
                String path = "src/ui/chess/img/";
                UnitCost cost = p.getUnitCost();
                path += restOfFile(p);
                try {
                    img = ImageIO.read(new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g2.drawImage(img, null, 50 * i+5, 50 * j+5);
            }
        }

        // adds "b" or "w" depending on if myPiece is black or white
        // adds the name of the unit afterwards (eg. queen, king)
        // finishes up with .png
        private String restOfFile(Piece myPiece) {
            UnitCost myCost = myPiece.getUnitCost();
            String restOfFile = "";
            if(myPiece.getSide()){
                restOfFile += "w";
            } else{
                restOfFile += "b";
            }
            if(myCost.equals(UnitCost.PAWN)){
                restOfFile += "pawn";
            } else if(myCost.equals(UnitCost.BISHOP)){
                restOfFile += "bishop";
            } else if(myCost.equals(UnitCost.KNIGHT)){
                restOfFile += "knight";
            } else if(myCost.equals(UnitCost.ROOK)){
                restOfFile += "rook";
            } else if(myCost.equals(UnitCost.QUEEN)){
                restOfFile += "queen";
            } else if(myCost.equals(UnitCost.KING)){
                restOfFile += "king";
            }
            restOfFile += ".png";
            return restOfFile;
        }
    }

    public Mainframe(){
        mouseEvent = new ChessMouseEvent();
        boardPanel = new BoardPanel(new BorderLayout());
        board = Board.getInstance();
        selected = null;
        initUI();
        setupButtons();
        positionComponents();
        setupBoard();
        add(boardPanel);
    }

    // sets up the buttons
    private void setupButtons() {
        mainWindowExit = new JButton("Exit");
        mainWindowExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(mainWindowExit);
    }

    // positions all the components
    private void positionComponents() {
        Insets insets = getInsets();
        positionBoard(insets);
        positionButtons(insets);
    }

    private void positionButtons(Insets insets) {
        Dimension exitDim = mainWindowExit.getPreferredSize();
        mainWindowExit.setBounds(300 + insets.left, insets.bottom - 50, exitDim.width, exitDim.height);
    }

    private void positionBoard(Insets insets) {
        Dimension size = boardPanel.getPreferredSize();
        boardPanel.setBounds(250+insets.left, 100 + insets.top, size.width, size.height);
    }

    private void setupBoard() {
        boardPanel.setSize(400, 400);
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void initUI() {
        createLayout();

        setTitle("Chess");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createLayout() {
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);
    }

}
