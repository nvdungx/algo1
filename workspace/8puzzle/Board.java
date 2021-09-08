/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-27
 *  Description: Slider puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int size;
    private int[][] btiles;
    private int hammingValue = 0;
    private int manhattanValue = 0;
    private int blankRow;
    private int blankCol;
    private ArrayList<Integer> neighborTiles;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // n-by-n array containing the n2 integers between 0 and n2 − 1, where 0 represents the blank square
        size = tiles.length;
        btiles = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                btiles[row][col] = tiles[row][col];
                if (!(isCorrect(row, col))) hammingValue++;
                manhattanValue += distance(row, col);
                if (btiles[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    processNeighborTiles(row, col);
                }
            }
        }
    }

    private int expectedValue(int row, int col) {
        if (row * size + col == size * size - 1) return 0;
        else return (row * size + col + 1);
    }

    // private static String listCellString(Board obj, ArrayList<Integer> list) {
    //     StringBuilder temp = new StringBuilder();
    //     temp.append("[ ");
    //     for (int i = 0; i < list.size() / 2; i++) {
    //         temp.append(cellString(obj, list.get(i * 2), list.get(i * 2 + 1)));
    //         if (i + 1 != list.size() / 2)
    //             temp.append(", ");
    //     }
    //     temp.append(" ]");
    //     return temp.toString();
    // }
    //
    // private static String cellString(Board obj, int row, int col) {
    //     return "Coord{" +
    //             "row=" + row +
    //             ", col=" + col +
    //             ", currentValue=" + obj.btiles[row][col] +
    //             ", expectedValue=" + obj.expectedValue(row, col) +
    //             '}';
    // }

    // if current value = expected value for current row/col
    private boolean isCorrect(int row, int col) {
        if (btiles[row][col] != 0)
            return (btiles[row][col] == expectedValue(row, col));
        else return true;
    }

    // distance to tiles for current value
    private int distance(int row, int col) {
        if (btiles[row][col] != 0) {
            return Math.abs(((btiles[row][col] - 1) / size) - row) + Math
                    .abs(((btiles[row][col] - 1) % size) - col);
        }
        else return 0;
    }

    private void processNeighborTiles(int row, int col) {
        neighborTiles = new ArrayList<>();
        if (row == 0) {
            neighborTiles.add(row + 1);
            neighborTiles.add(col);
        }
        else if (row == size - 1) {
            neighborTiles.add(row - 1);
            neighborTiles.add(col);
        }
        else {
            neighborTiles.add(row + 1);
            neighborTiles.add(col);
            neighborTiles.add(row - 1);
            neighborTiles.add(col);
        }
        if (col == 0) {
            neighborTiles.add(row);
            neighborTiles.add(col + 1);
        }
        else if (col == size - 1) {
            neighborTiles.add(row);
            neighborTiles.add(col - 1);
        }
        else {
            neighborTiles.add(row);
            neighborTiles.add(col + 1);
            neighborTiles.add(row);
            neighborTiles.add(col - 1);
        }
    }

    // string representation of this board
    public String toString() {
        // toString() method returns a string composed of n + 1 lines.
        // The first line contains the board size n;
        // the remaining n lines contains the n-by-n grid of tiles in row-major order
        StringBuilder result = new StringBuilder();
        result.append(String.format("%d", size));
        for (int row = 0; row < size; row++) {
            result.append("\r\n");
            for (int col = 0; col < size; col++) {
                result.append(String.format("%2d ", btiles[row][col]));
            }
        }
        // result.append(String.format("\r\nHamming: %s", hammingValue));
        // result.append(String.format("\r\nManhattan: %s", manhattanValue));
        // result.append(String.format("\r\nBlank tile: %s", cellString(this, blankRow, blankCol)));
        // result.append(String.format("\r\nNeighbor tiles: %s", listCellString(this, neighborTiles)));
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // Hamming and Manhattan distances. To measure how close a board is to the goal board
    // number of tiles out of place
    public int hamming() {
        // Caching the Hamming and Manhattan priorities
        return hammingValue;
    }

    private void updateBlankTile(int row, int col) {
        // remove old val of target tile
        if (!isCorrect(row, col)) this.hammingValue--;
        this.manhattanValue -= distance(row, col);
        // update new blank to target tile
        this.btiles[this.blankRow][this.blankCol] = this.btiles[row][col];
        // update new val of target tile
        if (!isCorrect(this.blankRow, this.blankCol)) this.hammingValue++;
        this.manhattanValue += distance(this.blankRow, this.blankCol);
        this.blankRow = row;
        this.blankCol = col;
        this.btiles[row][col] = 0;
        this.processNeighborTiles(row, col);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // Caching the Hamming and Manhattan priorities
        return manhattanValue;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (0 == manhattan());
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // Two boards are equal if they are have the same size and their corresponding tiles are in the same positions.
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board temp = (Board) y;
        if (this.size == temp.size) {
            for (int row = 0; row < this.size; row++) {
                for (int col = 0; col < this.size; col++) {
                    if (this.btiles[row][col] != temp.btiles[row][col]) return false;
                }
            }
            return true;
        }
        else return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> result = new LinkedQueue<>();
        for (int i = 0; i < neighborTiles.size() / 2; i++) {
            Board neigbor = new Board(btiles);
            neigbor.updateBlankTile(neighborTiles.get(i * 2), neighborTiles.get(i * 2 + 1));
            result.enqueue(neigbor);
        }
        return result;
    }

    private void minmaxExchange() {
        int[][] tilePair = new int[2][2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (btiles[i][j] != 0) {
                    tilePair[0][0] = i;
                    tilePair[0][1] = j;
                }
            }
        }
        for (int i = size - 1; i >= 0; i--) {
            for (int j = size - 1; j >= 0; j--) {
                if (btiles[i][j] != 0) {
                    tilePair[1][0] = i;
                    tilePair[1][1] = j;
                }
            }
        }
        if (!isCorrect(tilePair[0][0], tilePair[0][1])) hammingValue--;
        if (!isCorrect(tilePair[1][0], tilePair[1][1])) hammingValue--;
        manhattanValue -= distance(tilePair[0][0], tilePair[0][1]);
        manhattanValue -= distance(tilePair[1][0], tilePair[1][1]);
        int val = btiles[tilePair[1][0]][tilePair[1][1]];
        btiles[tilePair[1][0]][tilePair[1][1]] = btiles[tilePair[0][0]][tilePair[0][1]];
        btiles[tilePair[0][0]][tilePair[0][1]] = val;
        if (!isCorrect(tilePair[0][0], tilePair[0][1])) hammingValue++;
        if (!isCorrect(tilePair[1][0], tilePair[1][1])) hammingValue++;
        manhattanValue += distance(tilePair[0][0], tilePair[0][1]);
        manhattanValue += distance(tilePair[1][0], tilePair[1][1]);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(btiles);
        twin.minmaxExchange();
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // StdOut.println(initial.hamming());
        // StdOut.println(initial.manhattan());
        // StdOut.println(initial.dimension());
        // StdOut.println(initial.isGoal());
        // StdOut.println(cellString(initial, initial.blankRow, initial.blankCol));
        // StdOut.println(listCellString(initial, initial.neighborTiles));
        StdOut.println(initial);
        Board temp = initial.twin();
        StdOut.println(temp);
        if (temp.equals(initial))
            StdOut.println("equal");

        for (Board board : initial.neighbors()) {
            StdOut.println(board);
        }
    }
}
