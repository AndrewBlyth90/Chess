package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;


public class BlackPlayer extends Player {

    /**
     * Black player constructor method
     *
     * @param board
     * @param whiteStandardLegalMoves
     * @param blackStandardLegalMoves
     */

    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);

    }


    /**
     * Method that returns Active Black pieces
     *
     * @return
     */

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    /**
     * Getter method for Alliance
     *
     * @return
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    /**
     * Getter method for opponent
     *
     * @return
     */
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }


    /**
     * Calculates and returns a Collection of Moves that are classified as King Castles. A king castle is a special move in chess where both the
     * king and the castle can move on the same turn.
     * @param playerLegals
     * @param opponentLegals
     * @return
     */

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                 final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<Move>(); //Creates new array list

        if (this.playerKing.isFirstMove() && !this.isInCheck()) { //Checks if first move AND not in check
            //Black king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) { //Checks tiles are empty
                final Tile rookTile = this.board.getTile(7); //Assigns rook tile
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) { //checks rook tile is occupied AND first move
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() && //Checks no attacks can be placed on tiles
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5)); //Switches piecess
                    }
                }
            }
            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }


        return ImmutableList.copyOf(kingCastles); //Returns list
    }
}
