package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {


    /**
     * Creates an array of legal move coordinates for knight class
     */
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};


    /**
     * Constructor for Knight that takes two arguments of a position on the board and an alliance.
     *
     * @param piecePosition
     * @param pieceAlliance
     */
    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }


    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Calculates the legal moves available to the knight piece and returns an immutable list of
     * possible legal moves.
     *
     * @param board
     * @return
     */

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<Move>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {  //Cycles each item in an array of possible moves for Knight
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; //Adds possible move to current position
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //Checks if target position is <64

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||  //checks if current position is on the edge of board
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); // If target destination is valid assigns it to variable
                if (!candidateDestinationTile.isTileOccupied()) { //checks if tile is empty
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //Checks target piece alliance
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Method that creates a new Knight object when movePiece is called.
     * @param move
     * @return
     */

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }


    /**
     * toString method that returns the Knight Piece Type
     * @return
     */

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }


    /**
     * Calculates if Piece is on first column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    /**
     * Calculates if Piece is on Second column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    /**
     * Calculates if Piece is on Seventh Column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);

    }

    /**
     * Calculates if Piece is on eighth column
     * @param currentPosition
     * @param candidateOffset
     * @return
     */

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }

}

