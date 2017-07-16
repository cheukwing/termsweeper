import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class TermSweeper {

  private static boolean checkNumericString(String str) {
    int len = str.length();
    for (int i = 0; i < len; i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private static void game(Scanner sc, JFrame frame) {
    String input;
    do {
      System.out.println("Enter the board width: ");
      input = sc.nextLine();
    } while (input.length() < 1 || !checkNumericString(input));
    int width = Integer.parseInt(input);

    do {
      System.out.println("Enter the board length: ");
      input = sc.nextLine();
    } while (input.length() < 1 || !checkNumericString(input));
    int length = Integer.parseInt(input);

    char difficultySelection;
    // TODO: CHECK IF EMPTY INPUT
    do {
      System.out.println("Enter your difficulty: <E>asy <M>edium <H>ard");
      difficultySelection = Character.toLowerCase(sc.nextLine().charAt(0));
    } while (difficultySelection != 'e' && difficultySelection != 'm'
        && difficultySelection != 'h');
    Difficulty difficulty;
    switch (difficultySelection) {
      case 'e': {
        difficulty = Difficulty.EASY;
        break;
      }
      case 'm': {
        difficulty = Difficulty.MEDIUM;
        break;
      }
      case 'h':
        // fall-through
      default:
        difficulty = Difficulty.HARD;
    }

    Board board = new Board(width, length, difficulty);
    System.out.println();

    frame.getContentPane().removeAll();
    GridLayout grid = new GridLayout(length, width);
    frame.setLayout(grid);

    JButton[][] tiles = new JButton[length][width];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        tiles[i][j] = new JButton("BLANK");
        frame.add(tiles[i][j]);
      }
    }

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    while (!board.hasFinished()) {
      board.printBoard(false, tiles);

      String[] tokens;
      do {
        System.out.println("Enter your move: ");
        tokens = sc.nextLine().split(" ");
      } while (!(tokens.length == 3 && Character.isAlphabetic(tokens[0].charAt(0))
          && checkNumericString(tokens[1]) && checkNumericString(tokens[2])));
      char c = tokens[0].charAt(0);
      int x = Integer.parseInt(tokens[1]);
      int y = Integer.parseInt(tokens[2]);

      switch (c) {
        case 'p': {
          board.play(x, y);
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

    board.printBoard(true, tiles);
    if (board.hasWon()) {
      System.out.println("You Win!");
    } else {
      System.out.println("You Lose!");
    }
  }

  public static void main(String[] args) {
    System.out.println("Welcome to TermSweeper!");
    System.out.println("To reveal a tile, type \"p <x> <y>\"");
    System.out.println("To flag, qFlag or clear type \"f/q/c <x> <y> \"");

    Scanner sc = new Scanner(System.in);
    JFrame frame = new JFrame("TermSweeper");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    String input;
    do {
      game(sc, frame);
      System.out.println("Do you want to play again? <y>/<n>");
      input = sc.nextLine();
    } while (input.length() > 0 && Character.toLowerCase(input.charAt(0)) == 'y');
    sc.close();
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }
}
