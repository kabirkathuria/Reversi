<!-- READ WITH MD FORMATTING ON!!!! :) -->
<!-- READ WITH MD FORMATTING ON!!!! :) -->

## Overview:
This codebase aims to implement the Reversi game played on a hexagonal grid. The game follows standard Reversi rules with modifications to accommodate hexagonal cells. The design incorporates the Model-View-Controller (MVC) architecture, allowing for easy extensibility and modification.

## <u>Gui and Jar Usage</u>
#### You should run the jar from the command line. Use the following syntax:
'java -jar Hw7.jar help'

This will provide you with a quick rundown of how to format the arguments.

## <u>Under the hood</u>
Quick Start:
To instantiate a game with a default board with side length of 4, as shown below,
you can use the default constructor.

For custom board sizes, enter the side length in the constructor.

#### Coordinate System
We chose the following hexagonal Coordinate system:
0-indexed (top left to bottom right slanted column, top to bottom row)
Each tile with the R portion of the coordinate:
```
   0 0 0 0 
  1 1 1 1 1 
 2 2 2 2 2 2 
3 3 3 3 3 3 3 
 4 4 4 4 4 4  
  5 5 5 5 5   
   6 6 6 6  
   ```
Each tile with the Q portion of the coordinate:
```
   3 4 5 6 
  2 3 4 5 6 
 1 2 3 4 5 6 
0 1 2 3 4 5 6 
 0 1 2 3 4 5  
  0 1 2 3 4   
   0 1 2 3
   ```
Here is an example of the way the tiles are stored in the 2d array:
```
0-----Q-----6

      3 4 5 6   0
    2 3 4 5 6   |
  1 2 3 4 5 6   |
0 1 2 3 4 5 6   R
0 1 2 3 4 5     |
0 1 2 3 4       |
0 1 2 3         6
```

HexagonalReversi model = new HexagonalReversi(); -- uses default side length

HexagonalReversi model = new HexagonalReversi(Integer SideLength);

ReversiTextualView view = new ReversiTextualView(ObservableReversimodel, Appendable);

view.render();

#### Key Components:
- Model (ObservableReversiModel and MutableReversiModel): Represents the game's state, rules, and logic. It maintains the board state, checks for valid moves, and updates the game state.
- View (ReversiView): Visualizes the game's state. The current implementation provides a textual view (ReversiTextualView) to display the game board and game state.
- Strategy: Different strategies that choose moves in a game of reversi.

#### Key Subcomponents:
- HexaCoord: Represents a hexagonal coordinate in the game board.
- Player: Enumerated type representing the two players: BLACK and WHITE.
- HexagonalReversi: The main implementation of the game logic. It keeps track of the board, checks for valid moves, and maintains the game's state.

#### Source Organization:
- cs3500.reversi.model: Contains the core game logic and model interfaces.
  - Coordinate.java: Defines the hexagonal coordinate system.
  - HexagonalReversi.java: Provides the main game logic and state.
  - ObservableReversiModel.java: Interface for observing the game's state.
  - MutableReversiModel.java: Interface for modifying the game's state.
  - Player.java: Enumerated type representing the two players.
- cs3500.reversi.view: Contains the game visualization components.
  - ReversiTextualView.java: Provides a textual representation of the game board.
  - ReversiView.java: Interface for visualizing the game's state.
  - ReversiGuiView: JFrame that (currently) only contains the JReversiPanel.
  - JReversiPanel: Panel that visualizes a model with a GUI of hexagons. Connects to controller.
- cs3500.reversi.strategy: Contains the strategies a computer player could use to choose moves.
  - FallibleStrat: Interface for strategies that return an optional coordinate, representing a move, 
  or Optional.empty, meaning the strategy has no legal moves.
  - InFallibleStrat: Interface for strategies that return a coordinate, or throw an exception if no 
    moves are possible according to the strategy. "complete" strategies live here.
  - DynamicStrat: Interface for strategies that produce a list of possible moves. Useful when
    combined with other, subtractive strategies that restrict certain moves.
  - Infallible: Helper class that takes an infallible strategy and makes it fallible, by throwing
    an exception if the strategy returns Optional.empty.
  - MaximizeFlips: Chooses the move that flips the most pieces, or empty if no legal moves. Also
    implements dynamicstrat, meaning that it can return a list of moves, ordered by flips.
  - PlayCorners: strategy that chooses a corner, or Optional.empty.
  - AvoidCornerNeighbors: Restricts MaximizeFlips' list of moves to be non-corner neighbors.
  - TryTwo: combines two strategies. If the first returns optional.empty, it will try the second.
  - TryN: combines N strategies. Tries them in order, returning once a strategy finds a move, or
    or empty if none of the strategies could generate a move.

# <u>Part 2</u>
## Interfaces
The interfaces were already separated into read-only and mutable in my submission to assignment 5.

I changed some of the read-only methods to feature different names and reveal less information
about the Player enum, changing getPlayerAt(coordinate) to isTileBlack(coordinate), returning a
boolean instead of a Player. I also added a hasPiece(coordinate) method and changed the isInBounds 
method from private to public so that a try/catch block wouldn't be necessary to determine if a tile 
was occupied or out of bounds. 

I also added a new constructor, allowing it to take in a read-only board. Passing in a board to the 
constructor creates a fully functioning copy of the supplied board. I decided to add this because
it is very useful for creating computer players. Creating a deep copy of the board allows the
computer to play moves and observe their results, which is very useful for recursive strategies
such as minimax.

## Naming
Coordinate was renamed to HexaCoord to make it more clear what was being represented.
Nothing about the method has changed.

# <u>Part 3</u>
## Model
New addListener method added.

## View
New addListener method added.

Small internal refactor, textualView now makes use of a transmit method.

KeyPress now supported for GUI -- accidentally forgot those in the last assignment.

## Controller
#### GenericPlayer interface
Our player interface was lacking (or nonexistent) in the last assignment.
This more clearly details what a player should be responsible for doing, and what attributes they have.

#### Controller, modelStatus, and playerActions interfaces
The controller interface extends both modelStatus and playerActions.

playerActions details the functionality required to be a 'player' listener, taking in requests to
move at a certain coordinate or pass.

modelStatus details the functionality required ot be a 'model' listener. The only model notifications
at this time are notifyTurn, called when the turn changes, providing whether it's now black's turn.

#### HumanPlayer, MachinePlayer
HumanPlayer is a stub implementation of the GenerricPlayer interface. Real human players will use
the GUI view.

MachinePlayer is a full implementation of the GenericPlayer interface, supporting different
strategies through the constructor.

## <u>Testing</u>

#### ControllerSpy
This mock-like class adds itself as a listener to the provided model and view.
It reports any notifications, but doesn't interact.

#### ReversiControllerTests
Uses 
<a href="https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/awt/Robot.html" title="Robot Documentation">java.awt.Robot</a>
to simulate mouse clicks and key presses for testing.

Checks that the model, view, and controller are communicating properly.

# <u>Part 4</u>
## Command Line Usage
`java -jar <p1> <p2>`

Where p1 and p2 can be one of:
```
human
simple
intermediate
advanced
```
Note: p1 uses our view and strategy implementations, p2 uses the providers' implementations.

## Peer Provided Code Features
We created four adapters. Most functions already existed and simply required some coordinate 
translation. 

## Changes
I added a `potentialScore` method to the model interface that returns the score of a potential move.
This information was previously accessible, but tightly coupled to the model, so it was refactored.
This was done to de-couple the strategy from the model, something we didn't consider at the time.

# <u>Part 5</u>
## Command Line Usage
`java -jar <p1> <p2> <shape>`

There is now an optional third parameter, shape, which can be `square` or `hex`.

This determines the shape of the board.

## Abstraction
AbstractReversiModel abstracts the following common fields:  
`board`  
`passed`  
`gameOver`  
`turn`

and the following methods with common functionality:  
`startGame`  
`move`  
`pass`  
`throwIfGameOver`  
`validateMove`  
`isLegal`  
`flipAdjacentIfLegal`  
`hasMoveInDirection`  
`isGameOver`  
`anyValidMoves`  
`isTileBlack`  
`hasPiece`  
`isBlackTurn`  
`getBoardHeight`  
`getBoardWidth`  
`changeTurn`  
`getBlackScore`  
`getWhiteScore`  
`notifySubscribers`  
`addListener`  

The abstract model validates and performs moves on different board types because the 
hexagonal and square models use the same board storage system: a 2d-array.

The only difference between Hexagonal and Square moves is that square allows moves along
a fourth axis: ++/--. To accomodate this, the abstract model's constructor takes in
a list of direction vectors for possible moves.

## Model Interface Additions
### Mutable
`startGame` - We forgot this, though it doesn't <i>really</i> matter.
### Observable
`getCorners` - This implementation-specific method returns a list of corners for the
current board.
`getDirections` - This implementation-specific method returns a list of directions
that players can capture tiles in.