import java.awt.Color;
import java.util.LinkedList;

public class SlowFood extends Piece {
  
  public SlowFood(int x, int y) {
    this.x = x;
    this.y = y;
    this.color = Color.BLUE;
  }
  
  public int eat(LinkedList<Piece> l) {
    l.add(new Piece(-20, -20, Color.BLUE));
    slowTimeCounter = 40;
    
    return 1;
  }
}
