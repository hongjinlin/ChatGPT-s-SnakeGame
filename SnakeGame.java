import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JPanel implements KeyListener, Runnable {
    // constants
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int SIZE = 20;
    private static final int SPEED = 100;

    // game state
    private boolean running = false;
    private int direction = KeyEvent.VK_RIGHT;
    private ArrayList<Integer> snakeX = new ArrayList<>();
    private ArrayList<Integer> snakeY = new ArrayList<>();
    private int foodX;
    private int foodY;
    private Random random = new Random();

    public SnakeGame() {
        // initialize game state
        snakeX.add(0);
        snakeY.add(0);
        foodX = random.nextInt(WIDTH / SIZE) * SIZE;
        foodY = random.nextInt(HEIGHT / SIZE) * SIZE;

        // set up JPanel
        setSize(WIDTH, HEIGHT);
        setFocusable(true);
        addKeyListener(this);
    }

    public void paint(Graphics g) {
        // fill background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw food
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, SIZE, SIZE);

        // draw snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeX.size(); i++) {
            g.fillRect(snakeX.get(i), snakeY.get(i), SIZE, SIZE);
        }
    }

    public void update() {
        // update snake position
        int newX = snakeX.get(0);
        int newY = snakeY.get(0);
        switch (direction) {
            case KeyEvent.VK_UP:
                newY -= SIZE;
                break;
            case KeyEvent.VK_DOWN:
                newY += SIZE;
                break;
            case KeyEvent.VK_LEFT:
                newX -= SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                newX += SIZE;
                break;
        }

        // check if snake hit wall or itself
        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT || snakeX.contains(newX) && snakeY.contains(newY)) {
            running = false;
        }

        // check if snake ate food
        if (newX == foodX && newY == foodY) {
            // generate new food
            foodX = random.nextInt(WIDTH / SIZE) * SIZE;
            foodY = random.nextInt(HEIGHT / SIZE) * SIZE;
        } else {
            // remove tail of snake
            snakeX.remove(snakeX.size() - 1);
            snakeY.remove(snakeY.size() - 1);
        }

        // add new head to snake
        snakeX.add(0, newX);
        snakeY.add(0, newY);
    }

    public void run() {
        while (running) {
            update();
            repaint();

            // pause for a brief moment
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // start game on space key press
        if (key == KeyEvent.VK_SPACE && !running) {
            running = true;
            new Thread(this).start();
        }

        // update direction on arrow key press
        if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        }
        if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        }
        if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        }
        if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeGame());
        frame.setSize(800, 800); // set width and height of JFrame
        frame.setVisible(true);
        frame.setResizable(false);
    }
}