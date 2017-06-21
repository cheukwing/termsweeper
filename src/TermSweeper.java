import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Scanner;

public class TermSweeper {
  public static void main(String[] args) {
    System.out.println("Welcome to TermSweeper!");
    System.out.println("To reveal a tile, type \"p <x> <y>\"");
    System.out.println("To flag, qFlag or clear type \"f/q/c <x> <y> \"");

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the board width: ");
    int width = sc.nextInt();
    System.out.println("Enter the board length: ");
    int length = sc.nextInt();

    Board board = new Board(width, length);
    System.out.println();

    while(!board.isWon()) {
      board.printBoard(false);
      char c;
      do {
        System.out.println("Enter your move: ");
        c = sc.next().charAt(0);
      } while (!Character.isAlphabetic(c));
      int x = sc.nextInt();
      int y = sc.nextInt();
      switch(c) {
        case 'p': {
          if (!board.play(x, y)) {
            board.printBoard(true);
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
          System.out.println("Unrecognised Move.");
        }
      }
    }

    board.printBoard(false);
    System.out.println("You Win!");
  }

}
