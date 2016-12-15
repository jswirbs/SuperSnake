import java.awt.Color;
import java.util.LinkedList;

// Adds 5 pieces to the end of the snake instead of 1
public class SuperFood extends Piece {
  
  public SuperFood(int x, int y) {
    this.x = x;
    this.y = y;
    this.color = Color.GREEN;
  }
  
  public int eat(LinkedList<Piece> l) {
    l.add(new Piece(-20, -20, Color.GREEN));
    l.add(new Piece(-20, -20, Color.GREEN));
    l.add(new Piece(-20, -20, Color.GREEN));
    l.add(new Piece(-20, -20, Color.GREEN));
    
    return 5;
  }
  
}
