=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: swirbulj
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

  1. Collections
	I decided to use a LinkedList to model the snake itself. The list is of type Piece, which is a class I created that stores it’s position, color, size, etc. This implementation works well because it is easy to iterate through the list to get positions and draw the snake. It is also very easy to add new Piece objects to the end when the player eats a Piece, or remove Piece objects (PoisonFood powerUp).

  2. I/O
	I use I/O in order to implement high scores. The user can input their nickname in a field in the bottom controlPanel. When the game ends, their nickname and score are written to a file and ordered by score from high to low. On the home screen, the current top five scores and the associated nicknames are displayed. 

  3. Inheritance/Subtyping
	I used subtyping to model the objects that appear on the board. I created a Piece class, which includes variables/methods such as position, color, eat(), etc. I then created subclasses which model the various power ups. These are like normal food (Piece objects), but have different colors, perform different actions when the eat method is called, and return different point values to add to the score. 

  4. Testable component
	I use JUnit testing to test various aspects of the game such as the proper reaction to events like the snake running into itself or the wall, or eating food. I test the various power up abilities to make sure things are implemented correctly. I ran into a few unknown bugs when first implementing the power ups, the testing I did helped very much in finding and fixing them. 


=========================
=: Your Implementation :=
=========================

- Game: Contains the main method. Has the run method which lays out the application, setting up the various panels and buttons and initializing the game board. There is a JTextField and associated method to get information stored in it which are both outside of the run method. These are used to store/get the current user’s nickname so it can be used by the run method and by methods in the GameBoard class.

GameBoard: This class extends JPanel and is the rectangle in which the game is played. The constructor starts a timer and adds a key listener for keyboard controls. The start method is called when the start button is pressed which sets the playing boolean to true and resets the necessary variables to their initial state. The tick method (called by the timer) calls the move method and collision method, sets the score, and repaints the board. move() moves the snake head in the current direction and puts each following piece where the one in front of it just was. Collision checks for the snake head colliding with the edge of the board, itself, or a Piece object and either ends the game or adds a Piece to the end of the snake. The PaintComponent method draws the title screen if playing=false, else it draws the various game objects in their current positions.

Piece: This class models a Piece object, with fields for size, position, and color, and the methods eat and draw. These Pieces are spawned as food and can be eaten and added to the snake.

SuperFood: extends Piece. It is green (compared to the default Piece color yellow), and its eat method adds multiple pieces to the snake instead of just one. It also returns 5, which is added to the score once eaten instead of just one point.

PoisonFood: extends Piece. It is red. Its eat method removes multiple pieces from the snake and subtracts 3 points off of the score. 

SlowFood: extends Piece. It is blue. Its eat method sets the slowTimeCounter to 40. Now for 40 ticks, the move method is called half the time and the snake moves slower. It adds two Piece objects to the snake and one point to the score.

FastFood: extends Piece. It is orange. Its eat method sets the slowTimeCounter to -30. To for 30 ticks, the move method is called twice as often and the snake moves faster. It adds 2 Piece objects to the snake and 6 points to the score.



- Documentation of roadblocks during coding.

I initially thought of using a 2D array to model the game board and store info about the snake and pieces by coordinates in the 2D array. I quickly abandoned this approach for a better implementation using a LinkedList<Piece>. This allowed me to easily store coordinates and more info in Piece objects, add them to the list when necessary, and iterate through it with ease.

I had a random bug appear where the width of the application would stretch to the size of the screen for no apparent reason when the application was started. This popped up randomly, and random text appeared on my controlPanel the first time, but not again. I was finally able to get setPreferredSize() to work and the screen now has the correct dimensions.



- Evaluation of my design

I think the design is well implemented. Changing the model from a 2D array to a LinkedList made things much easier and simpler. The GameBoard class is long and has many methods, but I like having all the related functions together in one class. This seems simpler, more functional, and less likely to produce an error to me as long as I maintained order and clarity within the class. There isn’t anything major I would refactor (I made the necessary changed in the beginning). I might try to use subtyping more efficiently for the Piece objects and various power ups to better implement eating and spawning, but I am happy with the way it is now. 


========================
=: External Resources :=
========================

I used the Java Documentation to implement various things that were new to me and to find the correct usage of certain things. stackoverflow.com was helpful the few times I had random bugs/error messages. 

I used this video to help get my sounds working: https://www.youtube.com/watch?v=QVrxiJyLTqU

Image/Sound file sources
- mute icon: http://icons.webtoolhub.com/icon-n92023-detail.aspx
- start sound: https://www.youtube.com/watch?v=xQjRtGO0Ajc
- eating sound: https://www.youtube.com/watch?v=CwZPo2xx4iY
- power up sound: https://www.youtube.com/watch?v=33FKY1uIrG0
- game over sound: https://www.youtube.com/watch?v=wT-8Dm1VThc


