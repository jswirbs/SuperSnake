import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
  
  public static final int BOARD_WIDTH = 340;
  public static final int BOARD_HEIGHT = 340;
  public static final int INTERVAL = 120;
  
  public boolean playing = false;
  public boolean mute = false;
  public int score = 0;

// Keep track of the current direction of the snake's movement
  public boolean right = true;
  public boolean left = false;
  public boolean up = false;
  public boolean down = false;

// Keep track of the last direction the snake moved in.
// This is needed because the snake's direction cannot switch to right
// if it was just left and it cannot switch to up if it was just down
// and vice versa. 
  public boolean lastRight = true;
  public boolean lastLeft = false;
  public boolean lastUp = false;
  public boolean lastDown = false;
  
  LinkedList<Piece> snake = new LinkedList<Piece>();
  
  private JLabel scoreLabel;
  
  public GameBoard(JLabel score) {
    this.scoreLabel = score;
    scoreLabel.setText("Score: " + score);
    
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
    Timer timer = new Timer(INTERVAL, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tick();
      }
    });
    timer.start();
    
    setFocusable(true);
    
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_LEFT) && !lastRight) {
          if (up) {
            up = false;
          } else if (down) {
            down = false;
          }
          left = true;
        } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && !lastLeft) {
          if (up) {
            up = false;
          } else if (down) {
            down = false;
          }
          right = true;
        } else if ((e.getKeyCode() == KeyEvent.VK_DOWN) && !lastUp) {
          if (right) {
            right = false;
          } else if (left) {
            left = false;
          }
          down = true; 
        } else if ((e.getKeyCode() == KeyEvent.VK_UP) && !lastDown) {
          if (right) {
            right = false;
          } else if (left) {
            left = false;
          }
          up = true; 
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
          if (mute) {
            mute = false;
          } else {
            mute = true;
          }
        }
      }
    });

  }
  
  // Initial snake at the beginning of the game
  private Piece head;
  private Piece two;
  private Piece three;
  
  // Piece of food that is displayed on board
  Piece food = new Piece(0, 0);
  
  // Piece that will be subtyped with powerUp classes
  // They will randomly spawn on the board at certain intervals,
  // and when eaten enact their special power
  Piece powerUp = new SuperFood(-20, -20);
  // True if a powerUp is active and on screen at the moment
  boolean powerUpActive = false;
  
  // Called when the player presses the start button.
  // It initializes variables, starts the timer, and begins the game.
  public void start() {
    playSound("startSound.wav");
    playing = true;
    powerUpActive = false;
    score = 0;
    scoreLabel.setText("Score: 00" + score);
    powerUp.slowTimeCounter = 0;
    
    right = true;
    left = false;
    up = false;
    down = false;
    lastRight = true;
    lastLeft = false;
    lastUp = false;
    lastDown = false;
    
    head = new Piece(80, 160);
    two = new Piece(60, 160);
    three = new Piece(40, 160);
    
    snake = new LinkedList<Piece>();
    snake.add(head);
    snake.add(two);
    snake.add(three);
    
    spawn();
    
    requestFocusInWindow();
  }
  
  // Moves the snake in the current direction
  public void move() {
    int lastx = head.x;
    int lasty = head.y;
    for (Piece element : snake) {
      if (element == head) {
        if (right) {
          element.x += element.SIZE;
          lastRight = true;
          lastLeft = false;
          lastUp = false;
          lastDown = false;
        } else if (left) {
          element.x -= element.SIZE;
          lastRight = false;
          lastLeft = true;
          lastUp = false;
          lastDown = false;
        } else if (up) {
          element.y -= element.SIZE;
          lastRight = false;
          lastLeft = false;
          lastUp = true;
          lastDown = false;
        } else if (down) {
          element.y += element.SIZE;
          lastRight = false;
          lastLeft = false;
          lastUp = false;
          lastDown = true;
        }
      } else {
        int tempx = element.x;
        int tempy = element.y;
        element.x = lastx;
        element.y = lasty;
        lastx = tempx;
        lasty = tempy;
      }
    
    }
  }
  
  // Spawns a piece randomly on the game board
  public void spawn() {
    int randomx = (int) (Math.random() * BOARD_WIDTH);
    int randomy = (int) (Math.random() * BOARD_HEIGHT);
    int xcoord = randomx - (randomx % food.SIZE);
    int ycoord = randomy - (randomy % food.SIZE);
    food = new Piece(xcoord, ycoord);
    
    for (Piece element : snake) {
      if (((food.x == element.x) && (food.y == element.y)) ||
            ((food.x == powerUp.x) && (food.y == powerUp.y))) {
        spawn();
      }
    }
  }
  
  // Spawns a powerUp piece randomly on the game board
  public void spawnPowerUp() {
    int randomx = (int) (Math.random() * BOARD_WIDTH);
    int randomy = (int) (Math.random() * BOARD_HEIGHT);
    int xcoord = randomx - (randomx % powerUp.SIZE);
    int ycoord = randomy - (randomy % powerUp.SIZE);
    
    double rand = Math.random();
    if (rand < .25) {
      powerUp = new SuperFood(xcoord, ycoord);
    } else if ((rand >= .25) && (rand < .5)) {
      powerUp = new PoisonFood(xcoord, ycoord);
    } else if ((rand >= .5) && (rand < .75)) {
      powerUp = new SlowFood(xcoord, ycoord);
    } else if (rand >= .75) {
      powerUp = new FastFood(xcoord, ycoord);
    }
    
    powerUp.active = true;
    
    for (Piece element : snake) {
      if (((powerUp.x == element.x) && (powerUp.y == element.y)) || 
              (powerUp.x == food.x) && (powerUp.y == food.y)) {
        spawnPowerUp();
      }
    }
    
    powerUpActive = true;
  }
  
  // Checks for collisions with walls, the snake, food, or powerups
  public void collision() {
    if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
      playing = false;
      playSound("dyingSound.wav");
      writeHighscore();
    } 
    for (Piece element : snake) {
      if (element != head) {
        if ((head.x == element.x) && (head.y == element.y)) {
          playing = false;
          playSound("dyingSound.wav");
          writeHighscore();
        }
      }
    }
    
    if ((head.x == food.x) && (head.y == food.y)) {
      playSound("eatingSound.wav");
      snake.add(food);
      spawn();
      score++;
    }
    if (powerUpActive && (head.x == powerUp.x) && (head.y == powerUp.y)) {
      playSound("powerUpSound.wav");
      powerUp.eat(snake);
      score = Math.max(score + powerUp.eat(snake), 0);
      powerUpActive = false;
    }
  }
  
  // Plays sound file when called with name of file
  public void playSound(String source) {
    if (!mute) {
      try {
        File sound = new File(source);
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(sound));
        clip.start();
      } catch (Exception e) {
        System.out.println("Exception caught: error playing sound - " + e);
      }
    }
  }
  
  // Writes the score to the leaderboard.txt file (if it's in the top 5)
  public void writeHighscore() {
    String formattedScore;
    if (score < 10) {
      formattedScore = "00" + score;
    } else if (score < 100) {
      formattedScore = "0" + score;
    } else {
      formattedScore = score + "";
    }
    
    try { 
      ArrayList<String> l = new ArrayList<String>();
      BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
      String line;
      while ((line=br.readLine()) != null) {
        l.add(line);
      }
      
      boolean alreadyAdded = false;
      for (int i = 0; i < 5; i++) {
        if ((l.size()>i) && (Integer.parseInt(l.get(i).substring(0, 3)) < score) && !alreadyAdded) {
          l.add(i, (formattedScore + " " + Game.getNick()));
        alreadyAdded = true;
        } else if ((i>=l.size()) && !alreadyAdded) {
          l.add(formattedScore + " " + Game.getNick());
          alreadyAdded = true;
        }
      }
      
      BufferedWriter bw = new BufferedWriter(new FileWriter("leaderboard.txt"));
      for (String element : l) {
        bw.write(element);
        bw.newLine();
      }
      
      br.close();
      bw.close();
    } catch (IOException e) {
      System.out.println("IOException: error reading/writing leaderboard.txt - " + e);
      
    }
  }
  
  private int powerUpCounter = 49;
  
  void tick() {
    if (playing) {
      if (powerUp.slowTimeCounter == 0) {
        move();
        collision();
      } else if (powerUp.slowTimeCounter > 0) {
        if ((powerUp.slowTimeCounter % 2) == 0) {
          move();
          collision();
        }
        powerUp.slowTimeCounter--;
      } else if (powerUp.slowTimeCounter < 0) {
        move();
        collision();
        if ((powerUp.slowTimeCounter % 2) == 0) {
          move();
          collision();
        }
        powerUp.slowTimeCounter++;
      }

      if (score < 10) {
        scoreLabel.setText("Score: 00" + score);
      } else if (score < 100) {
        scoreLabel.setText("Score: 0" + score);
      } else {
        scoreLabel.setText("Score: " + score);
      }
      
      if (powerUpCounter == 50) {
        powerUpActive = false;
      } else if (powerUpCounter == 0) {
        spawnPowerUp();
        powerUpCounter = 100;
      }
      powerUpCounter--; 

      repaint();
    } else {
      snake.clear();
      
      repaint();
    }
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Piece element : snake) {
      element.draw(g);
    }
    if (playing) {
      food.draw(g);
      if (powerUpActive) {
        powerUp.draw(g);
      } 
    }
    
    // Displays title screen (instructions and leaderboard) when not playing (in menu)
    if (!playing) {
      try {
        g.drawImage(ImageIO.read(new File("titleScreen.png")), 0, 0, BOARD_WIDTH, BOARD_HEIGHT, 
                  null);
      } catch (IOException e) {
        System.out.println("IOException: error displaying title screen - " + e);
      }
      
      // Reads file called leaderboard.txt which has stores high scores
      int heightCount = 145;
      int lineCount = 1;
      Font medium = new Font("Futura Medium", Font.PLAIN, 13);
      g.setFont(medium);
      try {
        BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
        String line = null;
        while (((line=br.readLine()) != null) && (lineCount<=5)) {
          g.drawString(lineCount + line.substring(3), 35, (BOARD_HEIGHT / 3) + heightCount);
          g.drawString(line.substring(0, 3), 35 + 90, (BOARD_HEIGHT / 3) + heightCount);
          heightCount += 16;
          lineCount++;
        }
        br.close();
      } catch (IOException e) {
        System.out.println("IOException: error reading leaderboard.txt file");
      }
      
    }
    
    // Displays mute icon if m was pressed and mute=true
    if (mute) {
      try {
        g.drawImage(ImageIO.read(new File("mute.png")), BOARD_WIDTH-22, 5, 20, 20, null);
      } catch (IOException e) {
        System.out.println("IOException: error displaying mute icon - " + e);
      }
    }
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
  }
  
}
