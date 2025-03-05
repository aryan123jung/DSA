import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Random;

public class Tetris extends JPanel implements ActionListener {
    private final int ROWS = 20, COLS = 10, CELL_SIZE = 30;
    private int[][] board = new int[ROWS][COLS];
    private Queue<int[][]> queue = new LinkedList<>();
    private Stack<int[][]> stack = new Stack<>();
    private Timer timer;
    private int[][] currentBlock;
    private int blockRow = 0, blockCol = COLS / 2;
    private Random random = new Random();
    private boolean gameOver = false;

    public Tetris() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setBackground(Color.BLACK);
        generateBlock();
        timer = new Timer(500, this);
        timer.start();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> moveBlock(-1);
                        case KeyEvent.VK_RIGHT -> moveBlock(1);
                        case KeyEvent.VK_DOWN -> dropBlock();
                        case KeyEvent.VK_UP -> rotateBlock();
                    }
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver) {
                    restartGame();
                }
            }
        });
        setFocusable(true);
    }

    private void generateBlock() {
        int[][][] blocks = {
            {{1, 1}, {1, 1}}, // O-block
            {{1, 1, 1}, {0, 1, 0}}, // T-block
            {{1, 1, 1, 1}}, // I-block
            {{1, 1, 0}, {0, 1, 1}}, // Z-block
            {{0, 1, 1}, {1, 1, 0}}, // S-block
            {{1, 1, 1}, {1, 0, 0}}, // L-block
            {{1, 1, 1}, {0, 0, 1}} // J-block
        };

        currentBlock = blocks[random.nextInt(blocks.length)];
        queue.offer(currentBlock);

        if (!isValidMove(blockRow, blockCol)) {
            gameOver = true;
            timer.stop();
        }
    }

    private void moveBlock(int direction) {
        if (isValidMove(blockRow, blockCol + direction)) {
            blockCol += direction;
        }
    }

    private void dropBlock() {
        if (isValidMove(blockRow + 1, blockCol)) {
            blockRow++;
        } else {
            placeBlock();
            clearRows();
            blockRow = 0;
            blockCol = COLS / 2;
            generateBlock();
        }
    }

    private boolean isValidMove(int newRow, int newCol) {
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[0].length; j++) {
                if (currentBlock[i][j] == 1) {
                    int boardRow = newRow + i;
                    int boardCol = newCol + j;
                    if (boardRow >= ROWS || boardCol < 0 || boardCol >= COLS || board[boardRow][boardCol] == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeBlock() {
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[0].length; j++) {
                if (currentBlock[i][j] == 1) {
                    board[blockRow + i][blockCol + j] = 1;
                }
            }
        }
    }

    private void clearRows() {
        for (int i = 0; i < ROWS; i++) {
            boolean fullRow = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                stack.push(board);
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new int[COLS];
            }
        }
    }

    private void rotateBlock() {
        int size = currentBlock.length;
        int[][] rotated = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[j][size - 1 - i] = currentBlock[i][j];
            }
        }
        if (isValidMove(blockRow, blockCol)) {
            currentBlock = rotated;
        }
    }

    private void restartGame() {
        board = new int[ROWS][COLS];
        queue.clear();
        stack.clear();
        blockRow = 0;
        blockCol = COLS / 2;
        gameOver = false;
        generateBlock();
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            dropBlock();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 1) {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        g.setColor(Color.RED);
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[0].length; j++) {
                if (currentBlock[i][j] == 1) {
                    g.fillRect((blockCol + j) * CELL_SIZE, (blockRow + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over - Click to Restart", COLS * CELL_SIZE / 8, ROWS * CELL_SIZE / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        Tetris game = new Tetris();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
