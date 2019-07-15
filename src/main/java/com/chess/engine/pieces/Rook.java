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

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8}; //Creates array of possible movements



    public Rook(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition);
    }

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
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}
