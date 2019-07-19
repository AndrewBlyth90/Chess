package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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


    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<Move>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //Black king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {

                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        //TODO ADD A CASTLEMOVE!
                        kingCastles.add(null);
                    }

                }
            }

            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    //TODO ADD A CASTLEMOVE
                    kingCastles.add(null);
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }
}
