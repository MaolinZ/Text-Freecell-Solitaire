package cs3500.freecell;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import java.io.InputStreamReader;

/**
 * Main method for SimpleFreecell for interactive testing of controller.
 */
public class SimpleFreecellMain {

  /**
   * Main method for interactive testing.
   *
   * @param args Arguments
   */
  public static void main(String[] args) {
    FreecellModel<ICard> model = new MultiMoveFreecellModel();
    FreecellController<ICard> controller = new SimpleFreecellController(model,
        new InputStreamReader(System.in), System.out);

    controller.playGame(model.getDeck(), 52, 4, false);
  }
}
