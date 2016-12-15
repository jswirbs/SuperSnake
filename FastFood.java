import java.awt.Color;
import java.util.LinkedList;

public class FastFood extends Piece {
  
  public FastFood(int x, int y) {
    this.x = x;
    this.y = y;
    this.color = Color.ORANGE;
  }
  
  public int eat(LinkedList<Piece> l) {
    l.add(new Piece(-20, -20, Color.ORANGE));
    slowTimeCounter = -30;
    
    return 6;
  }
}
