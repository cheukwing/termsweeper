import java.util.Scanner;

public class TermSweeper {
  public static void main(String[] args) {
    System.out.println("Welcome to TermSweeper!");

    Board board = new Board(6, 5);
    board.fullBoardPrint();
    System.out.println();

    Scanner sc = new Scanner(System.in);
    while(!board.isWon()) {
      board.printBoard();
      System.out.println("p x y, f x y, q x y, c x y");
      char c = sc.next().charAt(0);
      int x = sc.nextInt();
      int y = sc.nextInt();
      switch(c) {
        case 'p': {
          if (!board.play(x, y)) {
            board.printBoard();
            System.out.println("You Lose!");
            return;
          }
          break;
        }
        case 'f': {
          board.flag(x, y, Flag.FLAGGED);
          break;
        }
        case 'q': {
          board.flag(x, y, Flag.QUESTION_FLAGGED);
          break;
        }
        case 'c': {
          board.flag(x, y, Flag.EMPTY);
          break;
        }
        default: {
          System.out.println("haha what");
        }
      }
    }
    System.out.println("Weiner");
  }

}
