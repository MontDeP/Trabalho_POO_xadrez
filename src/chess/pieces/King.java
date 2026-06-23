package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        int[][] offsets = {
            {-1,  0}, { 1,  0}, { 0, -1}, { 0,  1},
            {-1, -1}, {-1,  1}, { 1, -1}, { 1,  1}
        };

        for (int[] offset : offsets) {
            p.setValues(position.getRow() + offset[0], position.getColumn() + offset[1]);
            if (getBoard().positionExists(p) && canMove(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // Roque pequeno (kingside)
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            Position rookPos = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(rookPos)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (!getBoard().thereIsAPiece(p1) && !getBoard().thereIsAPiece(p2)) {
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }
        }

        // Roque grande (queenside)
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            Position rookPos = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(rookPos)) {
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if (!getBoard().thereIsAPiece(p1) && !getBoard().thereIsAPiece(p2) && !getBoard().thereIsAPiece(p3)) {
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }

        return mat;
    }
}
