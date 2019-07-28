package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;


    /**
     * Constructor for Board class.
     * @param builder
     */

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }


    /**
     * Creates a text representation of the chessboard.
     * @return String
     */

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(); //creates new StringBuilder
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString(); //Runs toString in relevant class
            builder.append(String.format("%3s", tileText)); //Sets spacing
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) { //Sets row length
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Returns white Player object
     * @return
     */

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    /**
     * returns Black player object
     * @return
     */

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    /**
     * returns the current player
     * @return
     */

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }



    /**
     * Returns Black pieces
     * @return
     */

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    /**
     * Returns White Pieces
     * @return
     */

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }


    /**
     * Calculates Legal moves for collection of pieces.
     * @param pieces
     * @return
     */
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<Move>();
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Returns a collection of active pieces
     * @param gameBoard
     * @param alliance
     * @return
     */
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
                                                           final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<Piece>();
        for (final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }


    /**
     * getter method for tile
     * @param tileCoordinate
     * @return
     */
    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }


    /**
     * Creates gameboard using Builder argument
     * @param builder
     * @return
     */

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES]; //Creates array large enough to have each tile
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) { //Loops for each tile
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i)); //Creates tile using tile number and piece using i as key
        }
        return ImmutableList.copyOf(tiles); //Passes copy as a list.
    }


    /**
     * Sets pieces on correct tile.
     * @return
     */

    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        //Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        //White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //Set white to move first
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build(); //returns built board

    }

    /**
     * Method that returns an Iterable of all legal moves.
     * @return
     */

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }


    /**
     * Builder class for building game board.
     */

    public static class Builder {


        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        /**
         * Constructor for Builder - Creates Map called boardConfig using Int as key and Piece as value.
         */

        public Builder() {
        this.boardConfig = new HashMap<Integer, Piece>();
        }

        /**
         * Adds argument piece position and piece into boardConfig
         * @param piece
         * @return
         */

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        /**
         * Sets first player
         * @param alliance
         * @return
         */

        public Builder setMoveMaker(final Alliance alliance) {
            this.nextMoveMaker = alliance;
            return this;
        }


        /**
         * Passes 'this' object to Board to be created
         * @return
         */

        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }

}
