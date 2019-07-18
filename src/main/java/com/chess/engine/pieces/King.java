package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    //Constant for possible move locations for King object

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};


    /**
     * Constructor for King object.
     * @param pieceAlliance
     * @param piecePosition
     */

    public King(final Alliance pieceAlliance, final int piecePosition) {
        super(pieceType.KING, piecePosition, pieceAlliance);
    }

    /**
     * Calculates legal Moves for King object.
     * @param board
     * @return Collection of Moves
     */

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<Move>(); // Creates empty Array list of Moves

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) { //Cycles through possible move locations

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; //Creates possible move

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); // If target destination is valid assigns it to variable
                if (!candidateDestinationTile.isTileOccupied()) { //checks if tile is empty
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //Checks target piece alliance
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }

            }


        }

        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * toString method that returns King Piece Type
     * @return
     */

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }


    /**
     * Calculates if King Piece  is on first column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }


    /**
     * Calculates if King is on Eighth column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }

}
