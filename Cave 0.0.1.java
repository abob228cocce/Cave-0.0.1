import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SimpleMinecraft extends JPanel implements KeyListener {
    private static final int BLOCK_SIZE = 20;
    private static final int ROWS = 20;
    private static final int COLUMNS = 30;
    private int[][] world = new int[ROWS][COLUMNS];
    private int playerX = 1;
    private int playerY = 1;
    private ArrayList<int[]> monsters = new ArrayList<>();
    private Random rand = new Random();

    public SimpleMinecraft() {
        JFrame frame = new JFrame("Cave 0.0.1");
        frame.setSize(COLUMNS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);
        generateWorld();
        spawnMonsters();
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveMonsters();
                repaint();
            }
        });
        timer.start();
    }

    private void generateWorld() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (y > 15) {
                    world[y][x] = 1;  // Ground blocks
                } else {
                    world[y][x] = 0;  // Air blocks
                }
            }
        }
    }

    private void spawnMonsters() {
        for (int i = 0; i < 5; i++) {  // Spawn 5 monsters
            int x = rand.nextInt(COLUMNS);
            int y = rand.nextInt(ROWS);
            monsters.add(new int[]{x, y});
        }
    }

    private void moveMonsters() {
        for (int[] monster : monsters) {
            int direction = rand.nextInt(4);
            switch (direction) {
                case 0: if (monster[1] > 0) monster[1]--; break;  // Move up
                case 1: if (monster[1] < ROWS - 1) monster[1]++; break;  // Move down
                case 2: if (monster[0] > 0) monster[0]--; break;  // Move left
                case 3: if (monster[0] < COLUMNS - 1) monster[0]++; break;  // Move right
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (world[y][x] == 1) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        g.setColor(Color.BLUE);
        g.fillRect(playerX * BLOCK_SIZE, playerY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

        g.setColor(Color.RED);
        for (int[] monster : monsters) {
            g.fillRect(monster[0] * BLOCK_SIZE, monster[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP && playerY > 0) {
            playerY--;
        } else if (code == KeyEvent.VK_DOWN && playerY < ROWS - 1) {
            playerY++;
        } else if (code == KeyEvent.VK_LEFT && playerX > 0) {
            playerX--;
        } else if (code == KeyEvent.VK_RIGHT && playerX < COLUMNS - 1) {
            playerX++;
        } else if (code == KeyEvent.VK_SPACE && playerY < ROWS - 1 && world[playerY + 1][playerX] == 1) {
            world[playerY + 1][playerX] = 0;  // Remove block
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SimpleMinecraft();
    }
}
