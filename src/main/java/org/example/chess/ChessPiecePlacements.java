package org.example.chess;

/**
 * Chess Grandmaster Problem: Find Valid Placements of Two Pieces
 * 
 * Problem: A chess grandmaster forgot the positions of two important pieces.
 * He knows:
 * - First piece is at (x1, y1) where xl1 <= x1 <= xr1, yl1 <= y1 <= yr1
 * - Second piece is at (x2, y2) where xl2 <= x2 <= xr2, yl2 <= y2 <= yr2
 * - Both pieces are on cells of the same color
 * - Two pieces can't be in the same location
 * 
 * Find: How many valid placements are possible?
 * 
 * Note: In chess, cells (i, j) and (k, l) have the same color if
 * (i + j) % 2 == (k + l) % 2
 */
public class ChessPiecePlacements {
    
    /**
     * Count valid placements of two pieces
     * 
     * @param xl1, xr1, yl1, yr1 Bounds for first piece
     * @param xl2, xr2, yl2, yr2 Bounds for second piece
     * @return Number of valid placements
     */
    public static long countValidPlacements(
            int xl1, int xr1, int yl1, int yr1,
            int xl2, int xr2, int yl2, int yr2) {
        
        // Count cells in each region
        long region1Size = (long)(xr1 - xl1 + 1) * (yr1 - yl1 + 1);
        long region2Size = (long)(xr2 - xl2 + 1) * (yr2 - yl2 + 1);
        
        // Count cells of each color in each region
        long region1Black = countBlackCells(xl1, xr1, yl1, yr1);
        long region1White = region1Size - region1Black;
        
        long region2Black = countBlackCells(xl2, xr2, yl2, yr2);
        long region2White = region2Size - region2Black;
        
        // Count overlapping region
        int overlapXl = Math.max(xl1, xl2);
        int overlapXr = Math.min(xr1, xr2);
        int overlapYl = Math.max(yl1, yl2);
        int overlapYr = Math.min(yr1, yr2);
        
        long overlapSize = 0;
        long overlapBlack = 0;
        long overlapWhite = 0;
        
        if (overlapXl <= overlapXr && overlapYl <= overlapYr) {
            overlapSize = (long)(overlapXr - overlapXl + 1) * (overlapYr - overlapYl + 1);
            overlapBlack = countBlackCells(overlapXl, overlapXr, overlapYl, overlapYr);
            overlapWhite = overlapSize - overlapBlack;
        }
        
        // Count valid placements:
        // 1. Both pieces on black cells: region1Black * region2Black
        //    But subtract cases where they're in the same position (overlap)
        // 2. Both pieces on white cells: region1White * region2White
        //    But subtract cases where they're in the same position (overlap)
        
        long blackPlacements = region1Black * region2Black;
        if (overlapSize > 0) {
            // Subtract cases where both are in overlap and same position
            blackPlacements -= overlapBlack;
        }
        
        long whitePlacements = region1White * region2White;
        if (overlapSize > 0) {
            // Subtract cases where both are in overlap and same position
            whitePlacements -= overlapWhite;
        }
        
        return blackPlacements + whitePlacements;
    }
    
    /**
     * Count black cells in a rectangular region
     * In chess, cell (i, j) is black if (i + j) % 2 == 0
     */
    private static long countBlackCells(int xl, int xr, int yl, int yr) {
        if (xl > xr || yl > yr) {
            return 0;
        }
        
        // Count black cells: cells where (x + y) % 2 == 0
        // Pattern: In a rectangle, roughly half are black
        long blackCount = 0;
        
        for (int x = xl; x <= xr; x++) {
            for (int y = yl; y <= yr; y++) {
                if ((x + y) % 2 == 0) {
                    blackCount++;
                }
            }
        }
        
        return blackCount;
    }
    
    /**
     * Optimized version: Count black cells using formula
     */
    private static long countBlackCellsOptimized(int xl, int xr, int yl, int yr) {
        if (xl > xr || yl > yr) {
            return 0;
        }
        
        long width = xr - xl + 1;
        long height = yr - yl + 1;
        long total = width * height;
        
        // For a rectangle starting at (xl, yl):
        // Black cells = ceil(total / 2) if (xl + yl) % 2 == 0
        // Black cells = floor(total / 2) if (xl + yl) % 2 == 1
        
        if ((xl + yl) % 2 == 0) {
            // Starting cell is black
            return (total + 1) / 2;
        } else {
            // Starting cell is white
            return total / 2;
        }
    }
    
    /**
     * Optimized count valid placements using formula
     */
    public static long countValidPlacementsOptimized(
            int xl1, int xr1, int yl1, int yr1,
            int xl2, int xr2, int yl2, int yr2) {
        
        // Count cells of each color in each region
        long region1Black = countBlackCellsOptimized(xl1, xr1, yl1, yr1);
        long region1Size = (long)(xr1 - xl1 + 1) * (yr1 - yl1 + 1);
        long region1White = region1Size - region1Black;
        
        long region2Black = countBlackCellsOptimized(xl2, xr2, yl2, yr2);
        long region2Size = (long)(xr2 - xl2 + 1) * (yr2 - yl2 + 1);
        long region2White = region2Size - region2Black;
        
        // Count overlapping region
        int overlapXl = Math.max(xl1, xl2);
        int overlapXr = Math.min(xr1, xr2);
        int overlapYl = Math.max(yl1, yl2);
        int overlapYr = Math.min(yr1, yr2);
        
        long overlapBlack = 0;
        long overlapWhite = 0;
        
        if (overlapXl <= overlapXr && overlapYl <= overlapYr) {
            overlapBlack = countBlackCellsOptimized(overlapXl, overlapXr, overlapYl, overlapYr);
            long overlapSize = (long)(overlapXr - overlapXl + 1) * (overlapYr - overlapYl + 1);
            overlapWhite = overlapSize - overlapBlack;
        }
        
        // Valid placements:
        // Black: region1Black * region2Black - overlapBlack (same position)
        // White: region1White * region2White - overlapWhite (same position)
        
        long blackPlacements = region1Black * region2Black - overlapBlack;
        long whitePlacements = region1White * region2White - overlapWhite;
        
        return blackPlacements + whitePlacements;
    }
    
    public static void main(String[] args) {
        System.out.println("=== Chess Piece Placements Problem ===");
        
        // Example 1: Non-overlapping regions
        // Region 1: (1,1) to (2,2) - 4 cells (2 black, 2 white)
        // Region 2: (3,3) to (4,4) - 4 cells (2 black, 2 white)
        // Valid: 2*2 (both black) + 2*2 (both white) = 8
        System.out.println("\nExample 1: Non-overlapping");
        long result1 = countValidPlacementsOptimized(1, 2, 1, 2, 3, 4, 3, 4);
        System.out.println("Region 1: (1,1) to (2,2)");
        System.out.println("Region 2: (3,3) to (4,4)");
        System.out.println("Result: " + result1);
        System.out.println("Expected: 8");
        
        // Example 2: Overlapping regions
        // Region 1: (1,1) to (3,3)
        // Region 2: (2,2) to (4,4)
        System.out.println("\nExample 2: Overlapping");
        long result2 = countValidPlacementsOptimized(1, 3, 1, 3, 2, 4, 2, 4);
        System.out.println("Region 1: (1,1) to (3,3)");
        System.out.println("Region 2: (2,2) to (4,4)");
        System.out.println("Result: " + result2);
        
        // Example 3: Same region
        System.out.println("\nExample 3: Same region");
        long result3 = countValidPlacementsOptimized(1, 2, 1, 2, 1, 2, 1, 2);
        System.out.println("Both pieces in (1,1) to (2,2)");
        System.out.println("Result: " + result3);
        System.out.println("Expected: 2 (2 black cells, can't be same)");
        
        // Verify black cell counting
        System.out.println("\n=== Verifying Black Cell Counting ===");
        System.out.println("Black cells in (1,1) to (2,2): " + 
            countBlackCellsOptimized(1, 2, 1, 2));
        System.out.println("Black cells in (1,1) to (3,3): " + 
            countBlackCellsOptimized(1, 3, 1, 3));
    }
}
