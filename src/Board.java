import java.util.Random;

public class Board {
  private final Square[][] board;
  private final int numMines;
  private final int width;
  private final int length;

  public Board(int width, int length) {
    this.width = width;
    this.length = length;
    board = new Square[length][width];
    Random random = new Random();
    int mineNum = 0;
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        boolean isMine = random.nextInt(100) > 90;
        if (isMine) {
          ++mineNum;
        }
        board[i][j] = new Square(isMine);
      }
    }
    numMines = mineNum;
  }

  public boolean play(int x, int y) {
    revealSurroundings(x, y);
    return !board[y][x].isMineSquare();
  }

  private void revealSurroundings(int x, int y) {
    board[y][x].reveal();
    if (getSurroundingMines(x, y) == 0) {
      revealBlanks(x, y);
    }
  }

  private void revealBlanks(int x, int y) {
    for (int i = y - 1; i < y + 1; i++) {
      for (int j = x - 1; j < x + 1; j++) {
        if (getSurroundingMines(j, i) == 0) {
          board[i][j].reveal();
          revealBlanks(j, i);
        }
      }
    }
  }

  private int getSurroundingMines(int x, int y) {
    int numMines = 0;
    for (int i = y - 1; i < y + 1; i++) {
      for (int j = x - 1; j < x + 1; j++) {
        if (i >= 0 && i < length && j >= 0 && j < width) {
          if (board[j][i].isMineSquare()) {
            numMines++;
          }
        }
      }
    }
    return numMines;
  }
}
