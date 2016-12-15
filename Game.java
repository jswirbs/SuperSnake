import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {

  public final static JTextField nick = new JTextField("Nickname", 6);
  public static String getNick() {
    if ((nick.getText() != null) && (nick.getText() != "")) {
      String nickFormatted = nick.getText() + "          ";
      return nickFormatted.substring(0, 10);
    }
    return "Nickname  ";
  }
  
  
  @Override
  public void run() {
    final JFrame frame = new JFrame("SuperSnake");
    frame.setLocation(300, 200);
    
    final JLabel score = new JLabel();

    final GameBoard board = new GameBoard(score);
    board.setPreferredSize(board.getPreferredSize());
    frame.add(board, BorderLayout.NORTH);
    
    final JPanel controlPanel = new JPanel();
    frame.add(controlPanel, BorderLayout.SOUTH);

    final JButton start = new JButton("Start");
    start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!board.playing) {
          board.snake.clear();
          board.start();
          start.setText("Reset");
        } else {
          board.playing = false;
          start.setText("Start");
        }
      }
    });
    controlPanel.add(start, BorderLayout.CENTER);
    
    controlPanel.add(nick, BorderLayout.WEST);
    controlPanel.add(score, BorderLayout.SOUTH); 
    controlPanel.setPreferredSize(new Dimension(board.getWidth(), 40));
    
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setResizable(false);
    
  }
  
  public static void main(String [] args) {
    SwingUtilities.invokeLater(new Game());
  }

}
