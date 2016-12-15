import java.awt.*;
import java.util.LinkedList;

public class Piece {

  public boolean active;
  public final int SIZE = 20;
  public int x;
  public int y;
  public Color color = Color.YELLOW;
  
  Piece() {
  }
  
  Piece(int init_x, int init_y) {
    this.x = init_x;
    this.y = init_y;
  }
  
  Piece(int init_x, int init_y, Color color) {
    this.x = init_x;
    this.y = init_y;
    this.color = color;
  }

  Piece(Color color) {
    this.color = color;
  }

  // Should be overridden by subclasses
  // Used to perform special power when powerUp is eaten
  public int eat(LinkedList<Piece> l) {
    return -1;
  }
  
  public int slowTimeCounter = 0;
  
  // Should be overridden by subclasses
  public void draw(Graphics g) {
    g.setColor(color);
    g.fillRect(x, y, SIZE, SIZE);
  }
  
}
