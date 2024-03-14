package cs3500.freecell.view;

import cs3500.freecell.model.FreecellModelState;
import java.io.IOException;

/**
 * Text view implementation of a FreecellView. Can represent a given freecell model as a string
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  private final Appendable ap;

  /**
   * Constructor for FreecellTextView. Automatically initializes appendable to System.out
   *
   * @param model the model of freecell to be represented.
   */
  public FreecellTextView(FreecellModelState<?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;

    this.ap = System.out;
  }

  /**
   * Alternate constructor, taking in an appendable to render to as well. An invalid(null)
   * appendable automatically sets this view to render to System.out.
   *
   * @param model the model of freecell to be represented.
   * @param ap    the appendable object to render the view to
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable ap) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;

    if (ap == null) {
      this.ap = System.out;
    } else {
      this.ap = ap;
    }
  }

  // CHANGED HW04: [Bugfix] - previous implementation allowed IllegalStateExceptions from function
  // calls before game start to be thrown in the toString method when the documentation does not
  // specify for toString to throw any exceptions. Wrapped in a try/catch to return an empty string
  // in the case of exceptions.
  @Override
  public String toString() {

    try {
      StringBuilder result = new StringBuilder();

      // Constructs string representation of foundation piles
      for (int f = 0; f < 4; f++) {

        StringBuilder pileStr = new StringBuilder();
        int numCards = model.getNumCardsInFoundationPile(f);

        for (int i = 0; i < numCards; i++) {
          pileStr.append(" " + model.getFoundationCardAt(f, i).toString());

          if (i < numCards - 1) {
            pileStr.append(",");
          }

        }

        result.append("F" + (f + 1) + ":" + pileStr + "\n");
      }

      // Construct open pile representation
      // CHANGED HW04: [Bugfix] - for loop conditional had a typo that was not caught by
      // prior HW tests
      for (int o = 0; o < model.getNumOpenPiles(); o++) {

        StringBuilder pileStr = new StringBuilder();
        int numCards = model.getNumCardsInOpenPile(o);

        for (int i = 0; i < numCards; i++) {
          pileStr.append(" " + model.getOpenCardAt(o).toString());
        }

        result.append("O" + (o + 1) + ":" + pileStr + "\n");
      }

      // Constructs string representation of cascade piles
      for (int c = 0; c < model.getNumCascadePiles(); c++) {

        StringBuilder pileStr = new StringBuilder();
        int numCards = model.getNumCardsInCascadePile(c);

        for (int i = 0; i < numCards; i++) {
          pileStr.append(" " + model.getCascadeCardAt(c, i).toString());

          if (i < numCards - 1) {
            pileStr.append(",");
          }
        }

        result.append("C" + (c + 1) + ":" + pileStr);

        if (c != model.getNumCascadePiles() - 1) {
          result.append("\n");
        }

      }

      return result.toString();
    } catch (IllegalStateException e) {
      return "";
    }


  }

  @Override
  public void renderBoard() throws IOException {
    this.ap.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.ap.append(message);
  }

}
