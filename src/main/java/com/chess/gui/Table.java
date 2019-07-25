package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private static String defaultPieceImagesPath = "art/fancy/";
    private final Color lightTileColour = Color.decode("#FFFACD");
    private final Color darkTileColour = Color.decode("#593E1A");


    /**
     * Constructor for table
     */
    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    /**
     * Creates menu bar on GUI
     * @return
     */

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;

    }

    /**
     * Creates the File option on the GUI
     * @return
     */

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file!");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    /**
     * Creates the Preferences menu on the GUI
     * @return
     */

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();

            }
        });

        preferencesMenu.add(legalMoveHighlighterCheckbox);
        return preferencesMenu;
    }

    /**
     * Enum to deal with flipping the board.
     */

    public enum BoardDirection {

        NORMAL {
            @Override
            List<BoardPanel.TilePanel> traverse(final List<BoardPanel.TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }

        },
        FLIPPED {
            @Override
            List<BoardPanel.TilePanel> traverse(final List<BoardPanel.TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<BoardPanel.TilePanel> traverse(final List<BoardPanel.TilePanel> boardTiles);

        abstract BoardDirection opposite();


    }


    public static class MoveLog{

        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<Move>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }

    /**
     * Creates tiles on GUI
      */

    private class BoardPanel extends JPanel {

        final List<TilePanel> boardTiles;


        /**
         * Constructor for BoardPanel
         */
        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<TilePanel>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * Method that visualises the chess board. First removes old board and then creates new one.
         * @param board
         */

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }






        private class TilePanel extends JPanel {

            private final int tileId;

            /**
             * Constructor for TilePanel. Adds Mouse Listener Event to Tile
             * @param boardPanel
             * @param tileId
             */

            TilePanel(final BoardPanel boardPanel, final int tileId) {
                super(new GridBagLayout());
                this.tileId = tileId;
                setPreferredSize(TILE_PANEL_DIMENSION);
                assignTileColour();
                assignTilePieceIcon(chessBoard);

                addMouseListener(new MouseListener() {
                    public void mouseClicked(final MouseEvent e) {

                        if (isRightMouseButton(e)) { //Clears selection if right click
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        } else if (isLeftMouseButton(e)) {
                            if (sourceTile == null) {
                                sourceTile = chessBoard.getTile(tileId); //Assigns sourceTile
                                humanMovedPiece = sourceTile.getPiece(); //Assigns piece
                                if (humanMovedPiece == null) {
                                    sourceTile = null;
                                }
                            } else {
                                destinationTile = chessBoard.getTile(tileId); //Sets destinationTile
                                final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate()); //Creates move by passing the board and two coordinates
                                final MoveTransition transition = chessBoard.currentPlayer().makeMove(move); //Makes move and passes status to transition
                                if (transition.getMoveStatus().isDone()) {
                                    chessBoard = transition.getTransitionBoard(); //Creates new board
                                    //TODO Add the move that was made to the move log
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;
                            }
                            SwingUtilities.invokeLater(new Runnable() { //Changes visuals
                                public void run() {
                                    boardPanel.drawBoard(chessBoard);
                                }
                            });
                        }
                    }

                    public void mousePressed(final MouseEvent e) {

                    }

                    public void mouseReleased(final MouseEvent e) {

                    }

                    public void mouseEntered(final MouseEvent e) {

                    }

                    public void mouseExited(final MouseEvent e) {

                    }
                });
                validate();
            }


            /**
             * Method that creates tile
             * @param board
             */

            public void drawTile(final Board board) {
                assignTileColour();
                assignTilePieceIcon(board);
                highLightLegals(board);
                validate();
                repaint();
            }

            /**
             * Method that sets image of piece on tile
             * @param board
             */

            private void assignTilePieceIcon(final Board board) {
                this.removeAll();
                if (board.getTile(this.tileId).isTileOccupied()) {
                    try {
                        final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                                board.getTile(this.tileId).getPiece().toString() + ".gif"));
                        add(new JLabel(new ImageIcon(image)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            /**
             * Method that highlights possible legal moves with a green dot.
             * @param board
             */

            private void highLightLegals(final Board board){
                if(highlightLegalMoves){
                    for(final Move move : pieceLegalMoves(board)){
                        if(move.getDestinationCoordinate() == this.tileId){
                            try{
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            /**
             * Method that returns a collection on Moves that are legal moves for a selected piece.
             * @param board
             * @return
             */

            private Collection<Move> pieceLegalMoves(final Board board) {
                if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                    return humanMovedPiece.calculateLegalMoves(board);
                }
                return Collections.emptyList();
            }

            /**
             * Method that assigns the colour of the tile.
             */

            private void assignTileColour() {
                if (BoardUtils.EIGHTH_RANK[this.tileId] ||
                        BoardUtils.SIXTH_RANK[this.tileId] ||
                        BoardUtils.FOURTH_RANK[this.tileId] ||
                        BoardUtils.SECOND_RANK[this.tileId]) {
                    setBackground(this.tileId % 2 == 0 ? lightTileColour : darkTileColour);
                } else if (BoardUtils.SEVENTH_RANK[this.tileId] ||
                        BoardUtils.FIFTH_RANK[this.tileId] ||
                        BoardUtils.THIRD_RANK[this.tileId] ||
                        BoardUtils.FIRST_RANK[this.tileId]) {
                    setBackground(this.tileId % 2 != 0 ? lightTileColour : darkTileColour);
                }


            }

        }
    }
}
