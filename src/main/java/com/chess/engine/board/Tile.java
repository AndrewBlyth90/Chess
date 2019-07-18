package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {



    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    /**
     * Method that creates 64 tiles and returns an immutable copy as a map.
     */

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap();

        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    /**
     *
     * @param tileCoordinate
     * @param piece
     * @return Tile
     *
     * If piece returns null will create a new occupied tile based on given coord and piece. Otherwise returns
     * empty tile coord.
     *
     */

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }


    /**
     * Tile constructor.
     * @param tileCoordinate
     */


    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    /**
     * Absract class methods
     */

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();


    /**
     * Empty tile Class
     */

    public static final class EmptyTile extends Tile{

       private EmptyTile(final int coordinate){
            super(coordinate);
        }


        @Override
        public String toString() {
           return "-";
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    /**
     * Occupied Tile Class
     */

    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile;

        /**
         * Occupied Tile constructor
         * @param tileCoordinate
         * @param pieceOnTile
         */

        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        /**
         * toString method that returns lowercase if Alliance is black
         * @return
         */

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                   getPiece().toString();
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    }

}
