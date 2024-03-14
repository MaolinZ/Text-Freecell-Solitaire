import static org.junit.Assert.assertEquals;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing class for a SimpleFreecell Controller.
 */
public abstract class AbstractFreecellControllerTest {

  private FreecellModel<ICard> model;
  private FreecellController<ICard> controller;

  private StringReader stringIn;
  private StringBuilder stringOut;

  static String pileToString(PileType p) {
    switch (p) {
      case CASCADE:
        return "CASCADE";
      case FOUNDATION:
        return "FOUNDATION";
      case OPEN:
        return "OPEN";
      default:
        return "Invalid";
    }
  }

  protected abstract FreecellModel<ICard> newModel();

  @Before
  public void initData() {
    stringIn = new StringReader("q");
    stringOut = new StringBuilder();

    model = newModel();
    controller = new SimpleFreecellController(this.model, stringIn, stringOut);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullModel() {
    SimpleFreecellController badControl = new SimpleFreecellController(null, stringIn, stringOut);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullReadable() {
    SimpleFreecellController badControl = new SimpleFreecellController(this.model, null, stringOut);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullAppendable() {
    SimpleFreecellController badControl = new SimpleFreecellController(null, stringIn, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameNullModel() {
    this.controller.playGame(null, 4, 4, false);
  }

  @Test
  public void testPlayGameInvalidCascade() {
    this.controller.playGame(this.model.getDeck(), 2, 4, false);
    assertEquals("Could not start game.", stringOut.toString());
  }

  @Test
  public void testPlayGameInvalidOpen() {
    this.controller.playGame(this.model.getDeck(), 4, 0, false);
    assertEquals("Could not start game.", stringOut.toString());
  }

  @Test
  public void testPlayGameEndGame() {
    StringBuilder log = new StringBuilder();

    FreecellModel<ICard> newModel = new MockModelAlwaysWon();
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new StringReader("q"), log);

    newController.playGame(this.model.getDeck(), 4, 4, false);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "Game over.", log.toString());

  }

  @Test
  public void testPlayGameStartGameInput() {
    StringBuilder log = new StringBuilder();
    FreecellModel<ICard> newModel = new MockModelStartGame(log);
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new StringReader("q"),
        new StringBuilder());

    newController.playGame(this.model.getDeck(), 4, 4, true);

    assertEquals(
        "Deck: [A♣, A♦, A♥, A♠, 2♣, 2♦, 2♥, 2♠, 3♣, 3♦, 3♥, 3♠, 4♣, 4♦, 4♥, 4♠, 5♣, 5♦, "
            + "5♥, 5♠, 6♣, 6♦, 6♥, 6♠, 7♣, 7♦, 7♥, 7♠, 8♣, 8♦, 8♥, 8♠, 9♣, 9♦, 9♥, 9♠, 10♣, 10♦, "
            + "10♥, 10♠, J♣, J♦, J♥, J♠, Q♣, Q♦, Q♥, Q♠, K♣, K♦, K♥, K♠], "
            + "NumCascade: 4, NumOpen: 4, Shuffle: true",
        log.toString());

    log = new StringBuilder(); // Recreate a new log and model for the next test
    newModel = new MockModelStartGame(log);
    newController = new SimpleFreecellController(newModel, new StringReader("q"),
        new StringBuilder());
    newController.playGame(new ArrayList<>(), 52, 8, false);

    assertEquals("Deck: [], NumCascade: 52, NumOpen: 8, Shuffle: false", log.toString());

  }

  @Test
  public void testPlayGameGoodSourceBadIndex() {
    StringBuilder log = new StringBuilder();
    SimpleFreecellController testController = new SimpleFreecellController(this.model,
        new StringReader("C1 BadIndex q"), log);

    testController.playGame(this.model.getDeck(), 4, 4, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\n\"BadIndex\" is an invalid card index. Re-input card index: \n"
        + "\nGame quit prematurely.", log.toString());

  }

  @Test
  public void testPlayGameQuitAtSource() {
    StringBuilder log = new StringBuilder();

    // Readable includes a valid move after to demonstrate that tokens after the quit character
    // are not parsed
    SimpleFreecellController control = new SimpleFreecellController(this.model,
        new StringReader("q C1 1 O1"), log);

    control.playGame(this.model.getDeck(), 4, 4, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\nGame quit prematurely.", log.toString());
    // Game quit prematurely message is always followed by a return statement in the code

  }

  @Test
  public void testPlayGameQuitAtIndex() {
    StringBuilder log = new StringBuilder();

    // Readable includes a valid move after to demonstrate that tokens after the quit character
    // are not parsed
    SimpleFreecellController control = new SimpleFreecellController(this.model,
        new StringReader("C1 q 1 O1"), log);

    control.playGame(this.model.getDeck(), 4, 4, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\nGame quit prematurely.", log.toString());
    // Game quit prematurely message is always followed by a return statement in the code

  }

  @Test
  public void testPlayGameQuitAtDest() {
    StringBuilder log = new StringBuilder();

    // Readable includes a valid move after to demonstrate that tokens after the quit character
    // are not parsed
    SimpleFreecellController control = new SimpleFreecellController(this.model,
        new StringReader("C1 1 q O1"), log);

    control.playGame(this.model.getDeck(), 4, 4, false);

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\nGame quit prematurely.", log.toString());
    // Game quit prematurely message is always followed by a return statement in the code

  }

  @Test
  public void testPlayGameMoveInput() {
    StringBuilder log = new StringBuilder();
    FreecellModel<ICard> newModel = new MockModelMove(log);
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new StringReader("c1 1 o1 q"), new StringBuilder());
    newController.playGame(this.model.getDeck(), 52, 4, false);

    assertEquals("SourceType: CASCADE, PileNumber: 0, CardIndex: 0, DestType: OPEN, DestPileNum: 0",
        log.toString());
  }

  @Test
  public void testPlayGameInvalidMove() {
    StringBuilder log = new StringBuilder();

    FreecellModel<ICard> newModel = new MockModelMove(new StringBuilder());
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new StringReader("c1 9 o1 q"), log);
    newController.playGame(this.model.getDeck(), 4, 4, false);

    // Expects view to render invalid move error msg and ask for new input.
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\nInvalid Move. Try again. Invalid card index 8"
        + "\n\nInput move: \n"
        + "Game quit prematurely.", log.toString());
  }

  @Test
  public void testPlayGameQuit() {

    this.controller.playGame(this.model.getDeck(), 4, 4, false);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠"
        + "\nInput move: "
        + "\nGame quit prematurely.", stringOut.toString());

  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameReadFails() {
    FreecellModel<ICard> newModel = newModel();
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new BadReadable(),
        new StringBuilder());
    newController.playGame(newModel.getDeck(), 4, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameAppendFails() {
    FreecellModel<ICard> newModel = newModel();
    FreecellController<ICard> newController = new SimpleFreecellController(newModel,
        new StringReader("c1 1 o1"),
        new BadAppendable());
    newController.playGame(newModel.getDeck(), 4, 4, false);
  }

  /**
   * Concrete class for testing SimpleFreecellController with a MultiMoveFreecellModel.
   */
  public static final class MultiMoveTest extends AbstractFreecellControllerTest {

    @Override
    public FreecellModel<ICard> newModel() {
      return new MultiMoveFreecellModel();
    }

  }

  /**
   * Concrete class for testing SimpleFreecellController with a SimpleFreecellModel.
   */
  public static final class SimpleFreecellTest extends AbstractFreecellControllerTest {

    @Override
    public FreecellModel<ICard> newModel() {
      return new SimpleFreecellModel();
    }

  }
}

