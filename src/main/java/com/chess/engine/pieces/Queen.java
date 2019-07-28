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

public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 8, 7, 9}; //Creates array of possible movements


    /**
     * Constructor for QUeen
     *
     * @param pieceAlliance
     * @param piecePosition
     */
    public Queen(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
    }

    public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }


    /**
     * Calculates legal moves for Queen
     *
     * @param board
     * @return
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<Move>(); //Creates list for legal moves

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) { //Cycles though possible moves
            int candidateDestinationCoordinate = this.piecePosition; /** POSSIBLE ERROR */
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //Checks if target destination is legal

                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || //checks if piece is at edge of board
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate((candidateDestinationCoordinate))) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); // If tile is valid destination assigns it to variable
                    if (!candidateDestinationTile.isTileOccupied()) { //checks if tile is empty
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //Checks target piece alliance
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Method that creates a new Queen object when movePiece is called.
     *
     * @param move
     * @return
     */

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }


    /**
     * toString method that returns the Piece Type
     *
     * @return
     */

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    /**
     * Calculates if Queen object is on first column
     *
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }


    /**
     * Calculates if Queen Object is on Eighth column
     *
     * @param currentPosition
     * @param candidateOffset
     * @return
     */
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

}
