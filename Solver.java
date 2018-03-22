import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solver {

    private final Board initialBoard;
    private Node searchNode;
    private boolean solvable;

    /**
     * find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        initialBoard = initial;
        solve();


    }

    private void solve() {
        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> pqTwin = new MinPQ<Node>();

        searchNode = new Node(initialBoard, null);
        Node newTwinNode = new Node(initialBoard.twin(), null);

        boolean check = true;
        while (check) {
            searchNode = nextStep(searchNode, pq);
            if (searchNode.board.isGoal()) {
                solvable = true;
                check = false;
            }

            newTwinNode = nextStep(newTwinNode, pqTwin);
            if (newTwinNode.board.isGoal()) {
                solvable = false;
                check = false;
            }

        }
    }

    private Node nextStep(Node searchNode, MinPQ<Node> pq) {
        Iterable<Board> neighbors = searchNode.board.neighbors();

        for (Board board : neighbors) {
            if (searchNode.parent == null || !board.equals(searchNode.parent.board)) {
                pq.insert(new Node(board, searchNode));
            }
        }
        return pq.delMin();

    }

    private class Node implements Comparable<Object> {
        private final Board board;
        private final Node parent;
        private final int moves;
        private final int priority;

        private Node(Board board, Node parent) {
            this.board = board;
            this.parent = parent;
            if (parent == null) {
                this.moves = 0;
            } else {
                this.moves = parent.moves + 1;
            }
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(Object o) {
            if (this.priority < ((Node) o).priority) {
                return -1;
            } else if (this.priority > ((Node) o).priority) {
                return 1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return board.toString();
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return
     */
    public boolean isSolvable() {
        return solvable;

    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return
     */
    public int moves() {
        if (isSolvable()) {
            return searchNode.moves;
        } else {
            return -1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     *
     * @return
     */
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Stack<Board> result = new Stack<>();
        Node n = searchNode;
        while (n != null) {
            result.add(n.board);
            n = n.parent;
        }

        List<Board> finalL = new ArrayList<>();
        while (!result.empty()) {
            finalL.add(result.pop());
        }
        return finalL;
    }

    /**
     * solve a slider puzzle (given below)
     *
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);

            }


        }
    }
}
