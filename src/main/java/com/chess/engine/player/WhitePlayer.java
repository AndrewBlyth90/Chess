package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player{

    /**
     * Constructor for White Player
     * @param board
     * @param whiteStandardLegalMoves
     * @param blackStandardLegalMoves
     */

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);

    }

    /**
     * Returns white pieces
     * @return
     */

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }


    /**
     * Returns Alliance
     * @return
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    /**
     * Returns opponent
     * @return
     */

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
