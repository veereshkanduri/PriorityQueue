import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] blocks;

    /**
     * construct a board from an n-by-n array of blocks, (where blocks[i][j] = block in row i, column j)
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        this.blocks = blocks;
    }

    /**
     * board dimension n
     *
     * @return
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * @param matrix
     * @return
     */
    private int[][] copy(int[][] matrix) {
        int length = matrix.length;
        int[][] copyMatrix = new int[length][length];
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                copyMatrix[x][y] = matrix[x][y];
            }
        }
        return copyMatrix;
    }

    /**
     * number of blocks out of place
     *
     * @return
     */
    public int hamming() {
        int size = dimension();
        int pointer = 1;
        int hammingValue = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (blocks[x][y] != 0) {
                    if (blocks[x][y] != pointer) {
                        hammingValue++;
                    }
                }
                pointer++;

            }
        }
        return hammingValue;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return
     */
    public int manhattan() {
        int size = blocks.length * blocks.length;
        int[] coordinates = new int[2];
        int[] originalCoordinates = new int[2];
        int manhattanValue = 0;
        int pointer = 1;
        int[][] goalBlock = new int[blocks.length][blocks.length];
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks.length; y++) {
                goalBlock[x][y] = pointer;
                pointer++;
            }
        }
        goalBlock[blocks.length - 1][blocks.length - 1] = 0;
        for (int i = 1; i < size; i++) {

            coordinates = findCoordinates(i, blocks);
            originalCoordinates = findCoordinates(i, goalBlock);
            manhattanValue = manhattanValue + Math.abs(originalCoordinates[0] - coordinates[0]) + Math.abs(originalCoordinates[1] - coordinates[1]);
        }
        return manhattanValue;
    }

    /**
     * find co-ordinates of a values in given matrix
     *
     * @param i
     * @return
     */
    private int[] findCoordinates(int i, int[][] matrix) {
        int size = matrix.length;
        int[] coordinates = new int[2];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (matrix[x][y] == i) {
                    coordinates[0] = x;
                    coordinates[1] = y;
                }
            }
        }
        return coordinates;
    }

    /**
     * is this board the goal board?
     *
     * @return
     */
    public boolean isGoal() {
        return (hamming() == 0);

    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     *
     * @return
     */
    public Board twin() {
        int[][] twinBlock = new int[dimension()][dimension()];

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int oneD = dimension() * i + j;
                if ((blocks[i][j] != 0)) {
                    int x = (oneD + 1) / dimension();
                    int y = (oneD + 1) % dimension();
                    if (blocks[x][y] != 0) {
                        twinBlock = swap(copy(blocks), i, j, x, y);
                        return new Board(twinBlock);
                    }
                }

            }
        }
        return new Board(twinBlock);
    }

    private int[][] swap(int[][] swapBlock, int i, int j, int i1, int j1) {
        int temp = swapBlock[i][j];
        swapBlock[i][j] = swapBlock[i1][j1];
        swapBlock[i1][j1] = temp;
        return swapBlock;
    }


    /**
     * does this board equal y?
     *
     * @param y
     * @return
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if(!(y instanceof Board)){
            return false;
        }
        else {
            Board newObj;
            newObj = (Board) y;

            if (this.dimension() != newObj.dimension()) {
                return false;
            }

            for (int x = 0; x < dimension(); x++) {
                for (int z = 0; z < dimension(); z++) {
                    if (this.blocks[x][z] != newObj.blocks[x][z]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * all neighboring boards
     *
     * @return
     */
    public Iterable<Board> neighbors() {
        int[] coordinatesZero = new int[2];
        coordinatesZero = findCoordinates(0, blocks);
        int x = coordinatesZero[0];
        int y = coordinatesZero[1];
        List<Board> neighbor = new ArrayList<Board>();

        if (y < dimension() - 1) {
            neighbor.add(new Board(swap(copy(blocks), x, y, x, y + 1)));
        }
        if (y > 0) {
            neighbor.add(new Board(swap(copy(blocks), x, y, x, y - 1)));
        }
        if (x > 0) {
            neighbor.add(new Board(swap(copy(blocks), x, y, x - 1, y)));
        }
        if (x < dimension() - 1) {
            neighbor.add(new Board(swap(copy(blocks), x, y, x + 1, y)));
        }
        return neighbor;
    }

    /**
     * string representation of this board (in the output format specified below)
     *
     * @return
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = blocks.length;
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }


}
