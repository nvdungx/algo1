/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-27
 *  Description: Slider puzzle
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode min;
    private boolean solvable;

    private class SearchNode implements Comparable<SearchNode> {
        // define a search node of the game to be a board
        private final Board board;
        // number of moves made to reach the board
        private final int move;
        // the previous search node
        private final SearchNode preNode;

        private SearchNode(Board iboard, int imove, SearchNode ipreNode) {
            board = iboard;
            move = imove;
            preNode = ipreNode;
        }

        // choice of priority function for a search node
        // Hamming priority function: Hamming distance of a board plus the number of moves made so far to get to the search node.
        // a search node with a small number of tiles in the wrong position is close to the goal
        // private int getPriorityHamming() {
        //     return board.hamming() + move;
        // }

        // Manhattan priority function: Manhattan distance of a board plus the number of moves made so far
        private int getPriorityManhattan() {
            return board.manhattan() + move;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public int compareTo(SearchNode other) {
            if (getPriorityManhattan() == other.getPriorityManhattan()) return 0;
            else return (getPriorityManhattan() > other.getPriorityManhattan()) ? 1 : -1;
        }

        public Iterable<Board> boardSequence() {
            Stack<Board> boardSequence = new Stack<>();
            boardSequence.push(board);
            SearchNode temp = preNode;
            while (temp != null) {
                boardSequence.push(temp.board);
                temp = temp.preNode;
            }
            return boardSequence;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    // A* search
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("ERROR: input object can not be null");

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        // insert the initial search node (the initial board, 0 moves, and a null previous search node) into a priority queue
        pq.insert(new SearchNode(initial, 0, null));
        Board tmp = initial.twin();
        pqTwin.insert(new SearchNode(tmp, 0, null));
        SearchNode minTwin;
        do {
            // delete from the priority queue the search node with the minimum priority
            min = pq.delMin();
            minTwin = pqTwin.delMin();
            // StdOut.println("--------");
            // StdOut.printf("min board: \r\n%s\r\nprority: %d\r\n", min.board.toString(),
            //               min.getPriorityHamming());
            // StdOut.println(min.board.toString());
            // StdOut.println("--------");
            // insert onto the priority queue all neighboring search nodes
            // (those that can be reached in one move from the dequeued search node)
            for (Board neighbor : min.board.neighbors()) {
                if (min.preNode != null) {
                    if (!neighbor.equals(min.preNode.board)) {
                        SearchNode next = new SearchNode(neighbor, min.move + 1, min);
                        pq.insert(next);
                    }
                }
                else {
                    SearchNode next = new SearchNode(neighbor, min.move + 1, min);
                    pq.insert(next);
                }
            }
            for (Board neighbor : minTwin.board.neighbors()) {
                if (minTwin.preNode != null) {
                    if (!neighbor.equals(minTwin.preNode.board)) {
                        SearchNode next = new SearchNode(neighbor, minTwin.move + 1, minTwin);
                        pqTwin.insert(next);
                    }
                }
                else {
                    SearchNode next = new SearchNode(neighbor, minTwin.move + 1, minTwin);
                    pqTwin.insert(next);
                }
            }
            // Repeat this procedure until the search node dequeued corresponds to the goal board.
        } while (!min.isGoal() && !minTwin.isGoal());
        if (!min.isGoal()) solvable = false;
        else solvable = true;
        // solve the puzzle from a given search node on the priority queue,
        // the total number of moves we need to make (including those already made) is at least its priority
        // when the goal board is dequeued
        // we have discovered not only a sequence of moves from the initial board to the goal board
        // one that makes the fewest moves.
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // Detecting unsolvable boards
        // Those that can lead to the goal board ?

        // Those that can lead to the goal board
        // if we modify the initial board by swapping any pair of tiles ?
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return min.move;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) return min.boardSequence();
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
