package cs3500.freecell.controller;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Controller for a simple game of freecell which converts user input to playable moves using a
 * freecell model.
 */
public class SimpleFreecellController implements FreecellController<ICard> {

  private FreecellModel<ICard> model;
  private Readable rd;
  private Appendable ap;

  /**
   * Constructor for a SimpleFreecellController.
   *
   * @param model the FreecellModel to be used to play the game
   * @param rd    the Readable object to read inputs from
   * @param ap    the Appendable object to render to
   * @throws IllegalArgumentException if any of the given parameters are null
   */
  public SimpleFreecellController(FreecellModel<ICard> model, Readable rd, Appendable ap)
      throws IllegalArgumentException {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Controller given null argument");
    }

    this.model = model;
    this.rd = rd;
    this.ap = ap;
  }

  @Override
  public void playGame(List<ICard> deck, int numCascades, int numOpens, boolean shuffle)
      throws IllegalStateException, IllegalArgumentException {

    if (deck == null) {
      throw new IllegalArgumentException("Cannot play game with null deck");
    }

    FreecellView view = new FreecellTextView(this.model, this.ap);

    try { // Try starting the game with the given inputs
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {

      try {
        view.renderMessage("Could not start game.");
        return;
      } catch (IOException e2) {
        throw new IllegalStateException("Writing to appendable failed");
      }
    }

    Scanner scan = new Scanner(rd);
    String errorMsg = ""; // Stores any potential error messages to show in the next loop

    while (!model.isGameOver()) {

      try {

        if (!errorMsg.isEmpty()) { // Render any potential errors from last move
          view.renderMessage(errorMsg);
          errorMsg = "";
        } else {
          view.renderBoard();
        }

        view.renderMessage("\nInput move: ");

        String[] inputs = new String[3]; // Fixed-size array to store 3 parts (source, index, dest)

        // Fill inputs one at a time
        for (int i = 0; i < 3; i++) {
          if (scan.hasNext()) {
            inputs[i] = scan.next();

            boolean validInput;

            if (i == 1) {
              validInput = this.validIndexInput(inputs[1]);
            } else {
              validInput = this.validPileInput(inputs[i]);
            }

            while (!validInput) {

              if (this.quitInput(inputs[i])) { // Checks if user quits
                view.renderMessage("\nGame quit prematurely.");
                return;
              }

              // Determines contents of erros message to be rendered
              String inputType;

              switch (i) {
                case 0:
                  inputType = "source pile";
                  break;
                case 1:
                  inputType = "card index";
                  break;
                case 2:
                  inputType = "destination pile";
                  break;
                default:
                  inputType = "n/a";
              }
              view.renderMessage(
                  "\n\"" + inputs[i] + "\" is an invalid " + inputType + ". Re-input " + inputType
                      + ": \n");

              // Throw error if no more inputs to replace invalid input
              if (!scan.hasNext()) {
                throw new IllegalStateException("Reading from readable failed, ran out of tokens");
              }

              inputs[i] = scan.next();

              // Checks if input is valid depending on what type of input it must be
              if (i == 1) {
                validInput = this.validIndexInput(inputs[1]);
              } else {
                validInput = this.validPileInput(inputs[i]);
              }

            }
          } else {
            throw new IllegalStateException("Reading from readable failed, ran out of tokens");
          }
        }

        try { // Try the move with the given inputs

          model.move(this.parsePile(inputs[0].charAt(0)),
              Integer.parseInt(inputs[0].substring(1)) - 1, Integer.parseInt(inputs[1]) - 1,
              this.parsePile(inputs[2].charAt(0)), Integer.parseInt(inputs[2].substring(1)) - 1);
        } catch (IllegalArgumentException e) {
          errorMsg = "\nInvalid Move. Try again. " + e.getMessage() + "\n";
        }

      } catch (IOException e) {
        throw new IllegalStateException("Game threw IOException");
      }

    }

    try { // Render board with game over message
      view.renderBoard();
      view.renderMessage("\nGame over.");
      return;
    } catch (IOException e) {
      throw new IllegalStateException("Writing to appendable object failed");
    }

  }

  private boolean quitInput(String input) {
    return 0 == input.compareToIgnoreCase("q");
  }

  private boolean validIndexInput(String input) {

    if (input == null) {
      return false;
    }

    try {
      return Integer.parseInt(input) > 0;
    } catch (NumberFormatException e) {
      return false;
    }

  }

  private boolean validPileInput(String input) {
    if (input == null) {
      return false;
    }

    if (input.length() <= 1) {
      return false; // Needs length of at least 2 for pile-index
    } else {
      return "ofcq".contains(input.substring(0, 1).toLowerCase(Locale.ROOT)) && this
          .validIndexInput(input.substring(1));
    }
  }

  private PileType parsePile(char c) {

    char pileType = Character.toUpperCase(c);

    switch (pileType) {
      case 'F':
        return PileType.FOUNDATION;
      case 'O':
        return PileType.OPEN;
      case 'C':
        return PileType.CASCADE;
      default:
        return null;

    }

  }

}
