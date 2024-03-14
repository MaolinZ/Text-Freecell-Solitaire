import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing class for FreecellTextView implementation.
 */
public abstract class AbstractFreecellTextViewTest {

  FreecellModel<ICard> model;
  FreecellModel<ICard> oneCardCascade;

  FreecellView view;
  FreecellView easyGame;

  protected abstract FreecellModel<ICard> newModel();

  @Before
  public void initData() {
    model = newModel();
    model.startGame(model.getDeck(), 4, 4, false);
    view = new FreecellTextView(model);

    oneCardCascade = newModel();
    oneCardCascade.startGame(oneCardCascade.getDeck(), 52, 4, false);
    easyGame = new FreecellTextView(oneCardCascade);
  }

  @Test
  public void testToString() {
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
        + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠", view.toString());

    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: A♣\n"
        + "C2: A♦\n"
        + "C3: A♥\n"
        + "C4: A♠\n"
        + "C5: 2♣\n"
        + "C6: 2♦\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10: 3♦\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", easyGame.toString());

    // Fill first and fourth open piles
    oneCardCascade.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    oneCardCascade.move(PileType.CASCADE, 2, 0, PileType.OPEN, 3);

    // Fill third Foundation pile with Spades Ace of Spades
    oneCardCascade.move(PileType.CASCADE, 3, 0, PileType.FOUNDATION, 2);

    // Fill first foundation pile with Ace of Diamonds
    oneCardCascade.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 0);
    oneCardCascade.move(PileType.CASCADE, 5, 0, PileType.FOUNDATION, 0);
    oneCardCascade.move(PileType.CASCADE, 9, 0, PileType.FOUNDATION, 0);

    assertEquals("F1: A♦, 2♦, 3♦\n"
        + "F2:\n"
        + "F3: A♠\n"
        + "F4:\n"
        + "O1: A♣\n"
        + "O2:\n"
        + "O3:\n"
        + "O4: A♥\n"
        + "C1:\n"
        + "C2:\n"
        + "C3:\n"
        + "C4:\n"
        + "C5: 2♣\n"
        + "C6:\n"
        + "C7: 2♥\n"
        + "C8: 2♠\n"
        + "C9: 3♣\n"
        + "C10:\n"
        + "C11: 3♥\n"
        + "C12: 3♠\n"
        + "C13: 4♣\n"
        + "C14: 4♦\n"
        + "C15: 4♥\n"
        + "C16: 4♠\n"
        + "C17: 5♣\n"
        + "C18: 5♦\n"
        + "C19: 5♥\n"
        + "C20: 5♠\n"
        + "C21: 6♣\n"
        + "C22: 6♦\n"
        + "C23: 6♥\n"
        + "C24: 6♠\n"
        + "C25: 7♣\n"
        + "C26: 7♦\n"
        + "C27: 7♥\n"
        + "C28: 7♠\n"
        + "C29: 8♣\n"
        + "C30: 8♦\n"
        + "C31: 8♥\n"
        + "C32: 8♠\n"
        + "C33: 9♣\n"
        + "C34: 9♦\n"
        + "C35: 9♥\n"
        + "C36: 9♠\n"
        + "C37: 10♣\n"
        + "C38: 10♦\n"
        + "C39: 10♥\n"
        + "C40: 10♠\n"
        + "C41: J♣\n"
        + "C42: J♦\n"
        + "C43: J♥\n"
        + "C44: J♠\n"
        + "C45: Q♣\n"
        + "C46: Q♦\n"
        + "C47: Q♥\n"
        + "C48: Q♠\n"
        + "C49: K♣\n"
        + "C50: K♦\n"
        + "C51: K♥\n"
        + "C52: K♠", easyGame.toString());

  }

  /**
   * Concrete class for testing FreecellTextView with a SimpleFreecellModel.
   */
  public static final class SimpleTest extends AbstractFreecellTextViewTest {

    @Override
    protected FreecellModel<ICard> newModel() {
      return new SimpleFreecellModel();
    }
  }

  /**
   * Concrete class for testing FreecellTextView with a MultiMoveFreecellModel.
   */
  public static final class MultiMoveTest extends AbstractFreecellTextViewTest {

    @Override
    protected FreecellModel<ICard> newModel() {
      return new MultiMoveFreecellModel();
    }
  }

}

