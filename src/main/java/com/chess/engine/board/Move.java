package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();


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
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    /**
     * Method to return coordinate of moved piece
     * @return
     */

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    /**
     * Override method that creates a hashcode.
     * @return
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }


    /**
     * Override method that compares two objects.
     * @param other
     * @return
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return  getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());

    }

    /**
     * Returns the destination coordinate
     *
     * @return
     */

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }


    /**
     * Getter Method for Moved Piece
     *
     * @return
     */
    public Piece getMovedPiece() {
        return this.movedPiece;
    }


    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }


    /**
     * Method used for making Moves. Creates total new board.
     *
     * @return
     */

    public Board execute() {
        final Builder builder = new Builder(); //Creates new builder object
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) { //Gets all active pieces
            if (!this.movedPiece.equals(piece)) { //If not the moved piece
                builder.setPiece(piece); //Adds to builder
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { //Gets opponents pieces
            builder.setPiece(piece); //adds to builder
        }
        //Move the moved piece!
        builder.setPiece(this.movedPiece.movePiece(this)); //Adds moved piece to builder
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); //Sets next player
        return builder.build(); // builds new board
    }

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
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }



    public static class AttackMove extends Move {

        final Piece attackedPiece;

        /**
         * Constructor for AttackMove that is used if target destination contains an enemy piece
         */

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }


        /**
         * Overrides the hashcode method.
         * @return
         */

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        /**
         * Overrides the Equals method.
         * @param other
         * @return
         */

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        /**
         * Getter method for Attacked Piece
         * @return
         */

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {

        /**
         * Constructor for PawnMove class.
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         */

        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends AttackMove {

        /**
         * Constructor for Pawn Attack move.
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         * @param attackedPiece
         */

        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

    }



    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        /**
         * Constructor for En Passant Move
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         * @param attackedPiece
         */

        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

    }

    public static final class PawnJump extends Move {

        /**
         * Constructor for Pawn jump.
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         */

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Overridden method used to execute a move. Creates new builder and passes in moved Pawn.
         * @return
         */

        @Override
        public Board execute() {
            final Builder builder = new Builder(); //Creates new builder object

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this); //Sets selected pawn as variable
            builder.setPiece(movedPawn); //Sets pawn as piece to be moved
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); //sets move maker
            return builder.build(); //builds new board
        }

    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        /**
         * Constructor for CastleMove
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         * @param castleRook
         * @param castleRookStart
         * @param castleRookDestination
         */

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        /**
         * Getter method for castleRook variable
         * @return
         */

        public Rook getCastleRook(){
            return this.castleRook;
        }

        /**
         * Returns true for Castling move.
         * @return
         */

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        /**
         * Overridden method for execute. Executes a move by creating a builder class and building a new board.
         * @return
         */

        @Override
        public Board execute(){

            final Builder builder = new Builder(); //Creates new board
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) { //Cycles active pieces
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) { //If not moved or castleRook
                    builder.setPiece(piece); //Adds to builder class
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { //Cycles opponents pieces
                builder.setPiece(piece); //Adds to builder
            }
            builder.setPiece(this.movedPiece.movePiece(this)); //adds moved piece
            //TODO look into the first move on normal pieces
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination)); //creates new rook
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); //sets move maker
            return builder.build(); //creates board
        }

    }



    public static final class KingSideCastleMove extends CastleMove {

        /**
         * Constructor for KingSideCastleMove
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         * @param castleRook
         * @param castleRookStart
         * @param castleRookDestination
         */

        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        /**
         * Overrides toString method to return the recognised code for a kingside castle move.
         * @return
         */

        @Override
        public String toString(){
            return "0-0";
        }

    }

    public static final class QueenSideCastleMove extends CastleMove {

        /**
         * Constructor for the QueenSideCastleMove
         * @param board
         * @param movedPiece
         * @param destinationCoordinate
         * @param castleRook
         * @param castleRookStart
         * @param castleRookDestination
         */

        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook, castleRookStart, castleRookDestination);
        }


        /**
         * Overrides the toString to return the offical identifier for queen side castling.
         * @return
         */
        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {

        /**
         * Nulls move
         */

        public NullMove() {
            super(null, null, -1);
        }

        /**
         * Overriden execute method that stops NullMove being initialised.
         * @return
         */

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }

    }



    public static class MoveFactory {

        /**
         * Throws error when move factory is initialised
         */
        private MoveFactory() {
            throw new RuntimeException("Not Instantiable!");
        }

        /**
         * Method that identifies and returns the move in a list of legal moves.
         * @param board
         * @param currentCoordinate
         * @param destinationCoordinate
         * @return
         */

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) { //Cycles all legal moves
                if (move.getCurrentCoordinate() == currentCoordinate && //Checks if move matches
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move; //returns move
                }
            }
            return NULL_MOVE;
        }
    }


}
