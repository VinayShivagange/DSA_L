package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Chess Grandmaster Forgot Position of Two Important Pieces Problem
 * 
 * Problem: A chess grandmaster forgot the positions of two important pieces on the board.
 * Given the current board state and constraints (like check/checkmate conditions, 
 * legal moves, etc.), determine where the two missing pieces should be placed.
 * 
 * This solution uses backtracking to find valid positions for the missing pieces.
 */
public class ChessMissingPieces {
    
    // Chess board size
    private static final int BOARD_SIZE = 8;
    
    // Piece types
    public enum PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EMPTY
    }
    
    // Piece colors
    public enum Color {
        WHITE, BLACK, NONE
    }
    
    // Represents a chess piece
    static class Piece {
        PieceType type;
        Color color;
        
        Piece(PieceType type, Color color) {
            this.type = type;
            this.color = color;
        }
        
        boolean isEmpty() {
            return type == PieceType.EMPTY || color == Color.NONE;
        }
        
        @Override
        public String toString() {
            if (isEmpty()) return ".";
            String symbol = "";
            switch (type) {
                case KING: symbol = "K"; break;
                case QUEEN: symbol = "Q"; break;
                case ROOK: symbol = "R"; break;
                case BISHOP: symbol = "B"; break;
                case KNIGHT: symbol = "N"; break;
                case PAWN: symbol = "P"; break;
                case EMPTY: return ".";
                default: return ".";
            }
            return color == Color.WHITE ? symbol : symbol.toLowerCase();
        }
    }
    
    // Represents a position on the board
    static class Position {
        int row;
        int col;
        
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }
        
        @Override
        public String toString() {
            return (char)('a' + col) + "" + (8 - row);
        }
    }
    
    private Piece[][] board;
    private List<Position> emptyPositions;
    private PieceType missingPiece1;
    private PieceType missingPiece2;
    private Color missingPiece1Color;
    private Color missingPiece2Color;
    private List<Solution> solutions;
    
    // Represents a solution with positions for both missing pieces
    static class Solution {
        Position piece1Pos;
        Position piece2Pos;
        
        Solution(Position piece1Pos, Position piece2Pos) {
            this.piece1Pos = piece1Pos;
            this.piece2Pos = piece2Pos;
        }
        
        @Override
        public String toString() {
            return "Piece 1 at: " + piece1Pos + ", Piece 2 at: " + piece2Pos;
        }
    }
    
    public ChessMissingPieces(Piece[][] board, PieceType missingPiece1, Color color1,
                             PieceType missingPiece2, Color color2) {
        this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = board[i][j] != null ? board[i][j] : 
                    new Piece(PieceType.EMPTY, Color.NONE);
            }
        }
        this.missingPiece1 = missingPiece1;
        this.missingPiece2 = missingPiece2;
        this.missingPiece1Color = color1;
        this.missingPiece2Color = color2;
        this.emptyPositions = new ArrayList<>();
        this.solutions = new ArrayList<>();
        
        // Find all empty positions
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.board[i][j].isEmpty()) {
                    emptyPositions.add(new Position(i, j));
                }
            }
        }
    }
    
    /**
     * Find all valid positions for the two missing pieces
     */
    public List<Solution> findMissingPiecePositions() {
        if (emptyPositions.size() < 2) {
            System.out.println("Not enough empty positions for two pieces!");
            return solutions;
        }
        
        // Try all combinations of positions for the two pieces
        for (int i = 0; i < emptyPositions.size(); i++) {
            for (int j = i + 1; j < emptyPositions.size(); j++) {
                Position pos1 = emptyPositions.get(i);
                Position pos2 = emptyPositions.get(j);
                
                // Try both orders (piece1 at pos1, piece2 at pos2) and vice versa
                if (isValidPlacement(pos1, missingPiece1, missingPiece1Color,
                                    pos2, missingPiece2, missingPiece2Color)) {
                    solutions.add(new Solution(pos1, pos2));
                }
                
                if (isValidPlacement(pos2, missingPiece1, missingPiece1Color,
                                    pos1, missingPiece2, missingPiece2Color)) {
                    solutions.add(new Solution(pos2, pos1));
                }
            }
        }
        
        return solutions;
    }
    
    /**
     * Check if placing pieces at given positions is valid
     */
    private boolean isValidPlacement(Position pos1, PieceType type1, Color color1,
                                    Position pos2, PieceType type2, Color color2) {
        // Place pieces temporarily
        Piece original1 = board[pos1.row][pos1.col];
        Piece original2 = board[pos2.row][pos2.col];
        
        board[pos1.row][pos1.col] = new Piece(type1, color1);
        board[pos2.row][pos2.col] = new Piece(type2, color2);
        
        boolean isValid = true;
        
        // Check if positions are not attacking each other (if same color)
        if (color1 == color2) {
            if (isAttacking(pos1, pos2, type1) || isAttacking(pos2, pos1, type2)) {
                isValid = false;
            }
        }
        
        // Check if kings are not adjacent (kings cannot be next to each other)
        if (type1 == PieceType.KING && type2 == PieceType.KING) {
            if (areAdjacent(pos1, pos2)) {
                isValid = false;
            }
        }
        
        // Additional validation: Check if the board state is legal
        // (e.g., no king in check from opponent, pieces not overlapping, etc.)
        if (isValid) {
            isValid = isLegalBoardState();
        }
        
        // Restore original state
        board[pos1.row][pos1.col] = original1;
        board[pos2.row][pos2.col] = original2;
        
        return isValid;
    }
    
    /**
     * Check if a piece at pos1 can attack pos2
     */
    private boolean isAttacking(Position pos1, Position pos2, PieceType type) {
        int rowDiff = Math.abs(pos1.row - pos2.row);
        int colDiff = Math.abs(pos1.col - pos2.col);
        
        switch (type) {
            case KING:
                return rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0);
            case QUEEN:
                return (rowDiff == 0 || colDiff == 0 || rowDiff == colDiff);
            case ROOK:
                return (rowDiff == 0 || colDiff == 0);
            case BISHOP:
                return rowDiff == colDiff;
            case KNIGHT:
                return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
            case PAWN:
                // Simplified: pawns attack diagonally forward
                return rowDiff == 1 && colDiff == 1;
            default:
                return false;
        }
    }
    
    /**
     * Check if two positions are adjacent
     */
    private boolean areAdjacent(Position pos1, Position pos2) {
        int rowDiff = Math.abs(pos1.row - pos2.row);
        int colDiff = Math.abs(pos1.col - pos2.col);
        return rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0);
    }
    
    /**
     * Check if the current board state is legal
     * (Simplified version - can be extended with more rules)
     */
    private boolean isLegalBoardState() {
        // Check that both kings exist and are not adjacent
        Position whiteKing = null, blackKing = null;
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board[i][j];
                if (piece.type == PieceType.KING) {
                    if (piece.color == Color.WHITE) {
                        whiteKing = new Position(i, j);
                    } else if (piece.color == Color.BLACK) {
                        blackKing = new Position(i, j);
                    }
                }
            }
        }
        
        // Both kings must exist
        if (whiteKing == null || blackKing == null) {
            return false;
        }
        
        // Kings cannot be adjacent
        if (areAdjacent(whiteKing, blackKing)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Print the board
     */
    public void printBoard() {
        System.out.println("\n  a b c d e f g h");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println((8 - i));
        }
        System.out.println("  a b c d e f g h\n");
    }
    
    /**
     * Print board with missing pieces placed
     */
    public void printBoardWithSolution(Solution solution) {
        Piece original1 = board[solution.piece1Pos.row][solution.piece1Pos.col];
        Piece original2 = board[solution.piece2Pos.row][solution.piece2Pos.col];
        
        board[solution.piece1Pos.row][solution.piece1Pos.col] = 
            new Piece(missingPiece1, missingPiece1Color);
        board[solution.piece2Pos.row][solution.piece2Pos.col] = 
            new Piece(missingPiece2, missingPiece2Color);
        
        printBoard();
        
        board[solution.piece1Pos.row][solution.piece1Pos.col] = original1;
        board[solution.piece2Pos.row][solution.piece2Pos.col] = original2;
    }
    
    public static void main(String[] args) {
        // Example: Find positions for two missing kings
        Piece[][] board = new Piece[8][8];
        
        // Initialize empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece(PieceType.EMPTY, Color.NONE);
            }
        }
        
        // Place some pieces on the board
        board[0][0] = new Piece(PieceType.ROOK, Color.WHITE);
        board[0][7] = new Piece(PieceType.ROOK, Color.WHITE);
        board[7][0] = new Piece(PieceType.ROOK, Color.BLACK);
        board[7][7] = new Piece(PieceType.ROOK, Color.BLACK);
        
        // Create problem: Two kings are missing
        ChessMissingPieces problem = new ChessMissingPieces(
            board,
            PieceType.KING, Color.WHITE,
            PieceType.KING, Color.BLACK
        );
        
        System.out.println("=== Chess Missing Pieces Problem ===");
        System.out.println("Missing: White King and Black King");
        System.out.println("\nInitial Board:");
        problem.printBoard();
        
        List<Solution> solutions = problem.findMissingPiecePositions();
        
        System.out.println("Found " + solutions.size() + " valid solution(s):");
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("\nSolution " + (i + 1) + ":");
            System.out.println(solutions.get(i));
            problem.printBoardWithSolution(solutions.get(i));
        }
        
        // Example 2: Find positions for a missing queen and rook
        System.out.println("\n\n=== Example 2: Missing Queen and Rook ===");
        Piece[][] board2 = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board2[i][j] = new Piece(PieceType.EMPTY, Color.NONE);
            }
        }
        
        board2[0][4] = new Piece(PieceType.KING, Color.WHITE);
        board2[7][4] = new Piece(PieceType.KING, Color.BLACK);
        
        ChessMissingPieces problem2 = new ChessMissingPieces(
            board2,
            PieceType.QUEEN, Color.WHITE,
            PieceType.ROOK, Color.BLACK
        );
        
        System.out.println("Missing: White Queen and Black Rook");
        System.out.println("\nInitial Board:");
        problem2.printBoard();
        
        List<Solution> solutions2 = problem2.findMissingPiecePositions();
        System.out.println("Found " + solutions2.size() + " valid solution(s):");
        if (solutions2.size() > 0) {
            System.out.println("\nFirst solution:");
            System.out.println(solutions2.get(0));
            problem2.printBoardWithSolution(solutions2.get(0));
        }
    }
}
