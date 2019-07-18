package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;


public class BlackPlayer extends Player{

    /**
     * Black player constructor method
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
     * @return
     */

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    /**
     * Getter method for Alliance
     * @return
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    /**
     * Getter method for opponent
     * @return
     */
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
