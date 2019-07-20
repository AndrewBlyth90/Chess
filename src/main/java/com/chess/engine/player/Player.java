package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    protected final boolean isInCheck;

    /**
     * Player Constructor
     *
     * @param board
     * @param legalMoves
     * @param opponentMoves
     */

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        //Checks if one of the legal attack moves would attack king
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }


    /**
     * Method that receives the players KING piece and a list of opponents legal moves. Returns a list of moves
     * that are possible on King piece.
     *
     * @param piecePosition
     * @param moves
     * @return
     */
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<Move>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    /**
     * Method for establishing the King piece
     *
     * @return
     */

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("This shouldn't have happened. This isn't a legal board!");
    }


    /**
     * Getter method for King piece
     *
     * @return
     */
    public King getPlayerKing() {
        return this.playerKing;
    }

    /**
     * Getter method for list of legal moves
     *
     * @return
     */

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    /**
     * Returns true if collection contains legal moves
     *
     * @param move
     * @return
     */

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    /**
     * Getter method for isInCheck.
     *
     * @return
     */

    public boolean isInCheck() {
        return this.isInCheck;
    }

    /**
     * Getter method for Check Mate. If King piece is in check and has no
     *
     * @return
     */

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    /**
     * If no attacks on king and king has no moves that will remove them from harms way
     *
     * @return
     */
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    /**
     * Checks if able to make move
     *
     * @return
     */

    private boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    //TODO implement below methods!!!!

    public boolean isCastled() {
        return false;
    }

    /**
     * Method that returns a moveTransition that indicates if the piece was able to move and if so, if in check.
     *
     * @param move
     * @return
     */

    public MoveTransition makeMove(final Move move) {

        if (!isMoveLegal(move)) { //Checks if move is NOT legal
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE); //Passes a MoveTransition stating an illegal Move
        }

        final Board transitionBoard = move.execute(); //Cretes new transitionBoard

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves()); //checks if any attacks are to be made on current player king

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK); //If Moves are not empty move leaves player in Check
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE); //Passes a Move Transition to show move is Done
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);


}
