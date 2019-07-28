package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9}; //Creates array of possible movements

    /**
     * Constructor for Bishop class
     *
     * @param piecePosition
     * @param pieceAlliance
     */

    public Bishop(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance, true);
    }

    public Bishop(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * calculates legal moves for bishop
     *
     * @param board
     * @return
     */

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<Move>(); //Creates list for legal moves

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) { //Cycles though possible moves
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //Checks if target destination is legal

                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || //checks if piece is at edge of board
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate((candidateDestinationCoordinate))) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); // If tile is valid destination assigns it to variable
                    if (!candidateDestinationTile.isTileOccupied()) { //checks if tile is empty
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //Checks target piece alliance
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Method that creates a new Bishop object when movePiece is called.
     * @param move
     * @return
     */

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    /**
     * toString method that returns bishop piece type
     * @return
     */

    @Override
    public String toString(){
         return PieceType.BISHOP.toString();
    }


    /**
     * Checks if Bishop object is on first column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }

    /**
     * Checks if Bishop obeject is on the eighth column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }
}
