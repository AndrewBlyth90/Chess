package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;


    /**
     * Constructor for Move class
     *
     * @param board
     * @param movedPiece
     * @param destinationCoordinate
     */

    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    /**
     * Returns the destination coordinate
     * @return
     */

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /**
     * Abstract execute method
     * @return
     */

    public abstract Board execute();

    /**
     * Constructor for MajorMove that is used if target destination has no 'enemy' piece
     */

    public static final class MajorMove extends Move {

        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    /**
     * Constructor for AttackMove that is used if target destination contains an enemy piece
     */

    public static final class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }


}
