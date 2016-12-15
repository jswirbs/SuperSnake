import java.awt.Color;
import java.util.LinkedList;

// Removes pieces and lowers the score
public class PoisonFood extends Piece {

  public PoisonFood(int x, int y) {
    this.x = x;
    this.y = y;
    this.color = Color.RED;
  }
  
  public int eat(LinkedList<Piece> l) {
    if (l.size() > 1)
      l.removeLast();
    if (l.size() > 1)
      l.removeLast();
    if (l.size() > 1)
      l.removeLast();
    if (l.size() > 1)
      l.removeLast();
    if (l.size() > 1)
      l.removeLast();
    l.add(new Piece(-20, -20, Color.RED));
    
    return -3;
  }
  
}
