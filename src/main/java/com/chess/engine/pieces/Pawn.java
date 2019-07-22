package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    /**
     * Creates array of possible moves for the pawn. 8 and 16 for regular movement (Depending on Alliance)
     * 7 and 9 are used for attack moves.
     */

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

    /**
     * Constructor for Pawn piece.
     *
     * @param piecePosition
     * @param pieceAlliance
     */

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }


    /**
     * Creates a Collection of possible legal moves for Pawn.
     *
     * @param board
     * @return
     */

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<Move>(); //Creates empty List to hold the legal moves.

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) { //Cycles through potential moves.

            final int candidateDestinationCoordinate = this.piecePosition +
                    (this.getPieceAlliance().getDirection() * currentCandidateOffset); //Calculates if pieces go up or down the board. Direction is either -1 or 1.

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //Calculates if destination tile is legal.
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) { //Checks if tile 1 away is unoccupied
                //TODO MORE WORK HERE!! (Deal with promotions)
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));  // Adds move to list if unnoccupied

            } else if (currentCandidateOffset == 16 && this.isFirstMove() && //Checks if tile 2 away & is first move &
                    (BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || //Checks if piece  is on second row and is black OR
                    (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite())) { //Checks if piece is on seventh row and is white.
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8); //Passes destination -1)
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && //Checks both destination and -1 to destination are both empty.
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 7 && // If going for attack move

                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || // Checks piece on 8th column is black
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) { // Checks piece on 1st column is white

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) { //Checks tile is occupied
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) { //Checks target piece is of different alliance
                        //TODO more work here
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate)); //adds move to list
                    }
                }
            } else if (currentCandidateOffset == 9 && //if going for attack move

                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || //checks piece on 1st column is black
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) { //checks piece on 8th column is white

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) { //checks tile occupied
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) { //checks target is different alliance
                        //TODO more work here
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate)); //adds move to list
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Method that creates a new Pawn object when movePiece is called.
     * @param move
     * @return
     */

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }


    /**
     * toString method that returns the Pawn Piece Type
     * @return
     */

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

}




