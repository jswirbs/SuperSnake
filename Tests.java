import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import org.junit.Test;


public class Tests {
  
  JLabel testScore = new JLabel();
  
  @Test
  public void snakeMovingNormally() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    for (int i = 0; i < 5; i++)
      testGame.tick();
    assertEquals(180, testGame.snake.get(0).x);
    assertEquals(160, testGame.snake.get(1).x);
    assertEquals(140, testGame.snake.get(2).x);
  }
  
  @Test
  public void snakeHitsWall() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    for (int i = 0; i < 15; i++)
      testGame.tick();
    assertFalse(testGame.playing);
  }
  
  @Test
  public void snakeHitsTail() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    for (int i = 0; i < 5; i++) {   // Makes the snake long enough to hit itself
      testGame.snake.add(new Piece());
      testGame.tick();
    }
    // Loop the snake around into its tail
    testGame.right = false;
    testGame.up = true;
    testGame.lastRight = false;
    testGame.tick();
    testGame.up = false;
    testGame.left = true;
    testGame.tick();
    testGame.left = false;
    testGame.down = true;
    testGame.tick();
    
    assertFalse(testGame.playing);
  }
  
  @Test
  public void snakeEatsPiece() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food = new Piece(100,160);
    testGame.tick();

    assertEquals(4, testGame.snake.size());
    assertEquals(1, testGame.score);
  }
  
  @Test
  public void snakeEatsSuperFood() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food.x = 0; // Just in case food randomly spawns right in front of snake
    testGame.powerUp = new SuperFood(100,160);
    testGame.powerUpActive = true;
    testGame.tick();

    assertEquals(11, testGame.snake.size());
    assertEquals(5, testGame.score);
  }
  
  @Test
  public void snakeEatsPoisonFood() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food.x = 0;
    for (int i = 0; i < 10; i++) {   // Makes the snake long enough to not go to zero
      testGame.score = 5;
      testGame.snake.add(new Piece());
    }
    testGame.powerUp = new PoisonFood(100,160);
    testGame.powerUpActive = true;
    testGame.tick();

    assertEquals(5, testGame.snake.size());
    assertEquals(2, testGame.score);
  }
  
  @Test
  public void snakeEatsPoisonFoodAtStart() { //Length needs to stay above 1 and score above 0
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food.x = 0;
    testGame.powerUp = new PoisonFood(100,160);
    testGame.powerUpActive = true;
    testGame.tick();

    assertEquals(2, testGame.snake.size());
    assertEquals(0, testGame.score);
  }
  
  @Test
  public void snakeEatsSlowFood() { 
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food.x = 0;
    testGame.powerUp = new SlowFood(100,160);
    testGame.powerUpActive = true;
    testGame.tick();
    testGame.tick();
    testGame.tick();

    assertEquals(120, testGame.snake.get(0).x); // Moved 1/2 as far as normal
    assertEquals(1, testGame.score);
  }
  
  @Test
  public void snakeEatsFastFood() { 
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.food.x = 0;
    testGame.powerUp = new FastFood(100,160);
    testGame.powerUpActive = true;
    testGame.tick();
    testGame.tick();
    testGame.tick();

    assertEquals(160, testGame.snake.get(0).x); // Moves faster than normal
    assertEquals(6, testGame.score);
  }
  
  
/*  Tests writing the highscore. The test passes, but it writes to the highscore file each time
 *  it is run, so it is temporarily commented out as to not interfere with the current leaderboard.
  @Test
  public void writeReadHighscore() {
    GameBoard testGame = new GameBoard(testScore);
    testGame.start();
    testGame.score = 999;
    for (int i = 0; i < 15; i++)
      testGame.tick();
    assertFalse(testGame.playing);
    
    try {
      BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
      String line = br.readLine().substring(0,3);
      br.close();
      assertEquals("999", line);
    } catch (IOException e) {
      assertTrue(false);
    }
  }
*/  
  
  @Test
  public void writeHighscoreFakeFile() {

    try {
      BufferedReader br = new BufferedReader(new FileReader("fakeFile.txt"));
      String line = br.readLine().substring(0,3);
      br.close();
      assertEquals("999", line);
    } catch (IOException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void soundFileFormatWorks() {
    try {
      File sound = new File("eatingSound.wav");
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(sound));
      clip.start();
      assertTrue(true);
    } catch (Exception e) {
      assertTrue(false);
    }
  }
  
  @Test
  public void dontSetNick() {
    assertEquals("Nickname  ", Game.getNick());
  }
  
  @Test
  public void setNickTooLong() {
    Game.nick.setText("abcdefghijklmnop");
    assertEquals("abcdefghij", Game.getNick());
  }
 
}
