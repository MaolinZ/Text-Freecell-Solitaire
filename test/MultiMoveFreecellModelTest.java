import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing class for MultiMoveFreecell. Contains only tests on behavior which differs from the
 * SimpleFreecellModel (essentially just the move method) as all SimpleFreecellModel functions are
 * tested along with similar MultiMoveFreecell functions in the AbstractModelTest class.
 */
public class MultiMoveFreecellModelTest {

  FreecellModel<ICard> model;
  FreecellModel<ICard> fourCascades;
  FreecellModel<ICard> validBuild;

  @Before
  public void initData() {
    this.model = new MultiMoveFreecellModel();
    this.fourCascades = new MultiMoveFreecellModel();
    this.validBuild = new MultiMoveFreecellModel();

    ArrayList<ICard> deck = new ArrayList<>();
    ArrayList<ICard> regDeck = new ArrayList<>();

    // Create a game with 4 cascade piles that are all valid builds
    for (ICard c : model.getDeck()) {
      regDeck.add(0, c);
    }

    for (int i = 0; i < 52 / 4; i++) {

      if (i % 2 == 0) {
        deck.add(regDeck.get((i * 4) + 1));
        deck.add(regDeck.get((i * 4)));
        deck.add(regDeck.get((i * 4) + 3));
        deck.add(regDeck.get((i * 4) + 2));
      } else {
        deck.add(regDeck.get(i * 4));
        deck.add(regDeck.get((i * 4) + 1));
        deck.add(regDeck.get((i * 4) + 2));
        deck.add(regDeck.get((i * 4) + 3));
      }
    }
    validBuild.startGame(deck, 4, 4, false);
    fourCascades.startGame(fourCascades.getDeck(), 4, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveBeforeStart() {
    this.model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNullSource() {
    this.fourCascades.move(null, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNullDestination() {
    this.fourCascades.move(PileType.CASCADE, 0, 0, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToOpenPile() {
    this.validBuild.move(PileType.CASCADE, 0, 10, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToFoundationPile() {
    this.validBuild.move(PileType.CASCADE, 0, 10, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidBuild() {
    fourCascades.move(PileType.CASCADE, 0, 5, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidBuildWithLastCard() { // Valid build but added to an invalid card
    validBuild.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    validBuild.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);

    validBuild.move(PileType.CASCADE, 2, 11, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveNotEnoughEmptyPiles() {
    validBuild.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    validBuild.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);
    validBuild.move(PileType.CASCADE, 0, 10, PileType.OPEN, 2);

    // Attempt to make a valid move (build-wise) when formula states that only two cards can be
    // moved when only one pile is open;
    validBuild.move(PileType.CASCADE, 3, 10, PileType.CASCADE, 0);
  }

  @Test
  public void testMultiMove() {

    validBuild.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    validBuild.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);

    validBuild.move(PileType.CASCADE, 3, 11, PileType.CASCADE, 0);

    ICard card1 = validBuild.getCascadeCardAt(0, 11); // Should be a 2 of Clubs
    ICard card2 = validBuild.getCascadeCardAt(0, 12); // Should be Ace of Diamonds

    assertEquals(2, card1.getValue());
    assertEquals(Suit.CLUBS, card1.getSuit());

    assertEquals(1, card2.getValue());
    assertEquals(Suit.DIAMONDS, card2.getSuit());

    // Check that the remaining cards in the source pile are unchanged
    for (int i = 10; i >= 0; i--) {
      Suit s;
      if (i % 2 == 0) {
        s = Suit.DIAMONDS;
      } else {
        s = Suit.CLUBS;
      }

      ICard card = validBuild.getCascadeCardAt(3, i);

      assertEquals(13 - i, card.getValue());
      assertEquals(s, card.getSuit());

    }

  }

}