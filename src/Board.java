import javax.swing.*;
import java.util.Random;

public class Board {
  private final Square[][] board;
  private final int numMines;
  private final int width;
  private final int length;
  private int numRevealed;
  private boolean hasLost;

  public static final int EASY_PROBABILITY = 90;
  public static final int MEDIUM_PROBABILITY = 80;
  public static final int HARD_PROBABILITY = 70;

  public Board(int width, int length, Difficulty difficulty) {
    this.width = width;
    this.length = length;
    this.numRevealed = 0;
    this.hasLost = false;
    this.board = new Square[length][width];
    Random random = new Random();
    int probability;
    switch (difficulty) {
      case EASY: {
        probability = EASY_PROBABILITY;
        break;
      }
      case MEDIUM: {
        probability = MEDIUM_PROBABILITY;
        break;
      }
      case HARD:
        // fall-through
      default:
        probability = HARD_PROBABILITY;
    }

    int mineNum = 0;
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        boolean isMine = random.nextInt(100) > probability;
        if (isMine) {
          ++mineNum;
        }
        board[i][j] = new Square(isMine);
      }
    }

    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j].setNumber(getSurroundingMines(j, i));
      }
    }

    this.numMines = mineNum;
  }

  public void play(int x, int y) {
    if (!withinBounds(x, y)) {
      System.out.println("Tile out of bounds!");
    } else if (board[y][x].getFlag() != Flag.EMPTY) {
      System.out.println("Cannot reveal a flagged tile!");
    } else {
      revealSurroundings(x, y);
      hasLost = board[y][x].isMineSquare();
    }
  }

  public void flag(int x, int y, Flag flag) {
    if (!withinBounds(x, y)) {
      System.out.println("Tile out of bounds!");
    } else if (board[y][x].isRevealedSquare()) {
      System.out.println("Tile is already revealed!");
    } else {
      board[y][x].setFlag(flag);
    }
  }

  private void revealSurroundings(int x, int y) {
    board[y][x].reveal();
    ++numRevealed;
    if (board[y][x].getNumber() == 0) {
      revealBlanks(x, y);
    }
  }

  private void revealBlanks(int x, int y) {
    for (int i = y - 1; i <= y + 1; i++) {
      for (int j = x - 1; j <= x + 1; j++) {
        if (withinBounds(j, i) && !board[i][j].isRevealedSquare() && !board[i][j].isMineSquare()) {
          ++numRevealed;
          board[i][j].reveal();
          if (board[i][j].getNumber() == 0) {
            revealBlanks(j, i);
          }
        }
      }
    }
  }

  private int getSurroundingMines(int x, int y) {
    int surroundingMines = 0;
    for (int i = y - 1; i <= y + 1; i++) {
      for (int j = x - 1; j <= x + 1; j++) {
        if (withinBounds(j, i) && board[i][j].isMineSquare()) {
          ++surroundingMines;
        }
      }
    }
    return surroundingMines;
  }

  public boolean withinBounds(int x, int y) {
    return y >= 0 && y < length && x >= 0 && x < width;
  }

  public boolean hasFinished() {
    return numRevealed == length * width - numMines || hasLost;
  }

  public boolean hasWon() {
    return hasFinished() && !hasLost;
  }

  public void printBoard(boolean revealBombs) {
    System.out.println("Board:");
    System.out.printf("\\ ");
    for (int i = 0; i < width; i++) {
      System.out.printf(i + " ");
    }
    System.out.printf("\n");

    for (int i = 0; i < length; i++) {
      System.out.printf(i + " ");
      for (int j = 0; j < width; j++) {
        //JButton tile = tiles[i][j];
        Square square = board[i][j];
        Flag flag = square.getFlag();
        if (flag != Flag.EMPTY) {
          if (flag == Flag.FLAGGED) {
            System.out.printf("f ");
            //tile.setText("f");
          } else {
            System.out.printf("? ");
            //tile.setText("?");
          }
        } else if (square.isMineSquare() && (revealBombs || square.isRevealedSquare())) {
          System.out.printf("x ");
          //tile.setText("x");
        } else if (!square.isRevealedSquare()) {
          System.out.printf(". ");
          //tile.setText(".");
        } else {
          System.out.printf(square.getNumber() + " ");
          //tile.setText(Integer.toString(square.getNumber()));
        }
      }
      System.out.printf("\n");
    }
  }

  public void fullBoardPrint() {
    System.out.println("Full Board:");
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        Square square = board[i][j];
        if (square.isMineSquare()) {
          System.out.printf("x ");
        } else {
          System.out.printf(square.getNumber() + " ");
        }
      }
      System.out.printf("\n");
    }
  }
}
