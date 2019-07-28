package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    /**
     * Piece Constructor.
     *
     * @param pieceType
     * @param piecePosition
     * @param pieceAlliance
     */

    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance,
          final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }


    /**
     * Calculates hashcode for piece
     *
     * @return
     */

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }


    /**
     * Overrides equals method to equate two pieces
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == ((Piece) other).getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    /**
     * Overrides hashcode
     *
     * @return
     */

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    /**
     * Returns piece Position
     *
     * @return
     */

    public int getPiecePosition() {
        return this.piecePosition;
    }


    /**
     * Returns piece alliance
     *
     * @return
     */
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    /**
     * Returns if piece first move
     *
     * @return
     */

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Returns the piece type
     *
     * @return
     */

    public PieceType getPieceType() {
        return this.pieceType;
    }


    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }


    /**
     * Abstract method for calculating legal moves
     *
     * @param board
     * @return
     */

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);


    /**
     * Enum for Piece Types
     */
    public enum PieceType {

        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };


        private int pieceValue;
        private String pieceName;


        /**
         * Constructor for PieceType
         *
         * @param pieceName
         */

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        /**
         * toString method that returns piece name
         *
         * @return
         */
        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }


        public abstract boolean isKing();

        public abstract boolean isRook();
    }

}
