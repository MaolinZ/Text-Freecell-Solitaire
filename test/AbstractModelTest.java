import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw02.Suit;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract class for testing shared behaviors between SimpleFreecellModel and MultiMoveFreecell
 * model via factory pattern.
 */
public abstract class AbstractModelTest {

  FreecellModel<ICard> fourCascade;
  FreecellModel<ICard> easyFourCascade;

  FreecellModel<ICard> shuffledGame;

  FreecellModel<ICard> newFreecellGame;

  FreecellModel<ICard> cardPerPile;

  protected abstract FreecellModel<ICard> newModel();

  // Reverse the round robin dealing to extract the original deck. Assumes no cards have been moved
  private List<ICard> reverseDeal(FreecellModel<ICard> model) {

    ArrayList<ICard> originalDeck = new ArrayList<>();
    int numPiles = model.getNumCascadePiles();
    int count = 0;
    while (count < 52) {
      for (int i = 0; i < model.getNumCardsInCascadePile(0); i++) {
        for (int x = 0; x < numPiles; x++) {
          if (model.getNumCardsInCascadePile(x) > i) {
            originalDeck.add(model.getCascadeCardAt(x, i));
          }
          count++;
        }
      }
    }

    return originalDeck;
  }

  @Before
  public void initData() {

    this.fourCascade = newModel();
    this.easyFourCascade = newModel();
    this.shuffledGame = newModel();
    this.newFreecellGame = newModel();
    this.cardPerPile = newModel();

    this.shuffledGame.startGame(this.shuffledGame.getDeck(), 7, 5, true);
    this.fourCascade.startGame(this.fourCascade.getDeck(), 4, 4, false);
    // fourCascade game should have four cascade piles, each with all the cards from one suit

    this.cardPerPile.startGame(this.cardPerPile.getDeck(), 52, 4, false);
    // One card per cascade pile

    ArrayList<ICard> deck = new ArrayList<>(easyFourCascade.getDeck());
    ArrayList<ICard> reversed = new ArrayList<>();

    for (int i = 51; i >= 0; i--) {
      reversed.add(deck.get(i));
    }

    // Creates game with four cascading piles, one suit per pile, decreasing in value
    this.easyFourCascade.startGame(reversed, 4, 4, false);


  }

  @Test
  public void testGetDeck() {

    List<ICard> deck = this.fourCascade.getDeck();
    boolean validDeck = true;

    if (deck == null) { // If getDeck returns null
      validDeck = false;
    } else if (deck.size() == 52) { // Check size

      // Nested for loop to go through every valid value of every valid suit
      for (Suit suit : Suit.values()) {
        for (int i = 1; i <= 13; i++) {
          int value = i;

          // removeIf returning false signals a deck not containing one of the required
          // combinations of suits and values (Either an invalid card present or duplicates)
          if (!deck.removeIf(c -> (c.getSuit().equals(suit) && c.getValue() == value))) {
            validDeck = false;
          }

        }
      }
    } else { // If size not exactly 52
      validDeck = false;
    }

    assertTrue(validDeck);

  }

  @Test
  public void testStartGameUnshuffled() {

    // Unshuffled deck
    newFreecellGame.startGame(newFreecellGame.getDeck(), 4, 4, false);

    // Test length of piles after starting game
    assertEquals(0, newFreecellGame.getNumCardsInOpenPile(1));
    assertEquals(0, newFreecellGame.getNumCardsInFoundationPile(1));
    assertEquals(13, newFreecellGame.getNumCardsInCascadePile(1));

    // Check that deck is not shuffled
    assertEquals(newFreecellGame.getDeck(), this.reverseDeal(newFreecellGame));
  }

  @Test
  public void testStartGameShuffled() {

    // Shuffled deck
    newFreecellGame.startGame(newFreecellGame.getDeck(), 4, 4, true);

    // Test length of piles after starting game
    assertEquals(0, newFreecellGame.getNumCardsInOpenPile(1));
    assertEquals(0, newFreecellGame.getNumCardsInFoundationPile(1));
    assertEquals(13, newFreecellGame.getNumCardsInCascadePile(1));

    // Check if shuffled (Deck used to deal should not be the same as a fresh unshuffled deck)
    assertNotEquals(newFreecellGame.getDeck(), this.reverseDeal(newFreecellGame));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck1() {
    newFreecellGame.startGame(new ArrayList<>(), 4, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck2() {
    List<ICard> deck = newFreecellGame.getDeck();
    deck.set(0, deck.get(1)); // Create duplicate card in deck

    newFreecellGame.startGame(deck, 4, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    newFreecellGame.startGame(null, 4, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidCascade() {
    newFreecellGame.startGame(newFreecellGame.getDeck(), 3, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidOpen() {
    newFreecellGame.startGame(newFreecellGame.getDeck(), 4, 0, true);
  }

  @Test
  public void testMoveToCascade() {
    cardPerPile.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 4);

    // Source pile should be empty (Size 0) and dest pile should be size 2 with both cards
    assertEquals(0, cardPerPile.getNumCardsInCascadePile(1));

    assertEquals(2, cardPerPile.getNumCardsInCascadePile(4));

    ICard addedCard = cardPerPile.getCascadeCardAt(4, 1);
    ICard originalCard = cardPerPile.getCascadeCardAt(4, 0);

    assertEquals(Suit.DIAMONDS, addedCard.getSuit());
    assertEquals(1, addedCard.getValue());

    assertEquals(Suit.CLUBS, originalCard.getSuit());
    assertEquals(2, originalCard.getValue());

    // Should then be able to add any card back into the now empty cascade pile w/o restriction
    cardPerPile.move(PileType.CASCADE, 3, 0, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToCascade1() {
    // Moves invalid colored ace to pile with 1 of Clubs
    cardPerPile.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToCascade2() {
    // Moves invalid value (correct color) card to cascade pile
    cardPerPile.move(PileType.CASCADE, 5, 0, PileType.CASCADE, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToCascade3() {
    // Moves invalid color and invalid value card to Cascade pile
    cardPerPile.move(PileType.CASCADE, 7, 0, PileType.CASCADE, 4);
  }

  @Test
  public void testMoveToFoundation() {
    cardPerPile.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    cardPerPile.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);

    cardPerPile.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);

    // Test contents of foundation pile 1 and size of source piles
    assertEquals(0, cardPerPile.getNumCardsInCascadePile(0));
    assertEquals(0, cardPerPile.getNumCardsInCascadePile(4));

    assertEquals(Suit.CLUBS, cardPerPile.getFoundationCardAt(0, 0).getSuit());
    assertEquals(1, cardPerPile.getFoundationCardAt(0, 0).getValue());

    assertEquals(Suit.CLUBS, cardPerPile.getFoundationCardAt(0, 1).getSuit());
    assertEquals(2, cardPerPile.getFoundationCardAt(0, 1).getValue());

    // Test contents of foundation pile 2 and size of source pile
    assertEquals(0, cardPerPile.getNumCardsInCascadePile(1));

    assertEquals(Suit.DIAMONDS, cardPerPile.getFoundationCardAt(1, 0).getSuit());
    assertEquals(1, cardPerPile.getFoundationCardAt(1, 0).getValue());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToEmptyFoundation() {
    // Non-ace card
    fourCascade.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToFoundation1() {
    // Fill first foundation pile with Ace of Clubs
    easyFourCascade.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

    // Invalid suit
    easyFourCascade.move(PileType.CASCADE, 7, 0, PileType.FOUNDATION, 0);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToFoundation2() {
    // Fill first foundation pile with Ace of Clubs
    easyFourCascade.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

    // Invalid value
    easyFourCascade.move(PileType.CASCADE, 8, 0, PileType.FOUNDATION, 0);

  }

  @Test
  public void testInvalidMoveToOpen() {
    fourCascade.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);

    int numCards = fourCascade.getNumCardsInCascadePile(0);

    // Ensure all other cards in original pile untouched
    for (int i = 0; i < numCards; i++) {
      assertEquals(Suit.CLUBS, fourCascade.getCascadeCardAt(0, i).getSuit());
      assertEquals(i + 1, fourCascade.getCascadeCardAt(0, i).getValue());
    }

    assertEquals(Suit.CLUBS, fourCascade.getOpenCardAt(0).getSuit());
    assertEquals(13, fourCascade.getOpenCardAt(0).getValue());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveCascadeToFullOpen() {
    fourCascade.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    fourCascade.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveBeforeStart() {
    newFreecellGame.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
  }

  @Test
  public void testIsGameOver() {

    assertFalse(easyFourCascade.isGameOver());

    // Move all cards except for one into foundation piles (store on in open)
    for (int p = 0; p < 4; p++) {

      for (int i = easyFourCascade.getNumCardsInCascadePile(p) - 1; i >= 0; i--) {

        if (p == 3 && i == 0) {
          easyFourCascade.move(PileType.CASCADE, 3, 0, PileType.OPEN, 0);
        } else {
          easyFourCascade.move(PileType.CASCADE, p, i, PileType.FOUNDATION, p);
        }
      }
    }

    assertFalse(easyFourCascade.isGameOver());

    // Move last card from open to foundation pile
    easyFourCascade.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 3);

    assertTrue(easyFourCascade.isGameOver());

    assertFalse(fourCascade.isGameOver());
    assertFalse(shuffledGame.isGameOver());
    assertFalse(newFreecellGame.isGameOver());

  }

  @Test
  public void testGetNumCardsInFoundationPile() {
    assertEquals(0, easyFourCascade.getNumCardsInFoundationPile(0));

    easyFourCascade.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    assertEquals(1, easyFourCascade.getNumCardsInFoundationPile(0));

    easyFourCascade.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    assertEquals(2, easyFourCascade.getNumCardsInFoundationPile(0));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInFoundationPileInvalidIndex1() {
    fourCascade.getNumCardsInFoundationPile(-3); // Negative index
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInFoundationPileInvalidIndex2() {
    fourCascade.getNumCardsInFoundationPile(6); // Negative index exceeds # of piles
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInFoundationPileBeforeStart1() {
    newFreecellGame.getNumCardsInFoundationPile(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInFoundationPileBeforeStart2() {
    newFreecellGame.getNumCardsInFoundationPile(-3);
  }

  @Test
  public void testGetNumCardsInCascadePile() {

    assertEquals(13, fourCascade.getNumCardsInCascadePile(0));
    assertEquals(13, fourCascade.getNumCardsInCascadePile(1));
    assertEquals(13, fourCascade.getNumCardsInCascadePile(2));
    assertEquals(13, fourCascade.getNumCardsInCascadePile(3));

    assertEquals(8, shuffledGame.getNumCardsInCascadePile(0));
    assertEquals(7, shuffledGame.getNumCardsInCascadePile(5));


  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInCascadePileInvalidIndex1() {
    fourCascade.getNumCardsInCascadePile(-3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumCardsInCascadePileInvalidIndex2() {
    fourCascade.getNumCardsInCascadePile(4);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInCascadePileBeforeStart() {
    newFreecellGame.getNumCardsInCascadePile(-3);
  }

  @Test
  public void testGetNumCardsInOpenPile() {
    assertEquals(0, easyFourCascade.getNumCardsInOpenPile(0));
    assertEquals(0, easyFourCascade.getNumCardsInOpenPile(3));
    easyFourCascade.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(1, easyFourCascade.getNumCardsInOpenPile(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInOpenPileBeforeStart1() {

    newFreecellGame.getNumCardsInOpenPile(0);

  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumCardsInOpenPileBeforeStart2() {

    // Should check for game start before index validity
    newFreecellGame.getNumCardsInOpenPile(-3);

  }

  @Test
  public void testGetNumOpenPiles() {

    assertEquals(-1, newFreecellGame.getNumOpenPiles()); // Before game start
    assertEquals(5, shuffledGame.getNumOpenPiles());
    assertEquals(4, fourCascade.getNumOpenPiles());

  }

  @Test
  public void testGetNumCascadePiles() {

    assertEquals(-1, newFreecellGame.getNumCascadePiles()); // Before game start
    assertEquals(7, shuffledGame.getNumCascadePiles());
    assertEquals(4, fourCascade.getNumCascadePiles());

  }

  @Test
  public void testGetFoundationCardAt() {

    easyFourCascade.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    easyFourCascade.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);

    easyFourCascade.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 3);
    easyFourCascade.move(PileType.CASCADE, 1, 11, PileType.FOUNDATION, 3);

    ICard card1 = easyFourCascade.getFoundationCardAt(0, 1);
    ICard card2 = easyFourCascade.getFoundationCardAt(3, 0);

    assertEquals(Suit.SPADES, card1.getSuit());
    assertEquals(2, card1.getValue());

    assertEquals(Suit.HEARTS, card2.getSuit());
    assertEquals(1, card2.getValue());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtInvalidIndex1() {
    fourCascade.getFoundationCardAt(0, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtInvalidIndex2() {
    fourCascade.getFoundationCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtInvalidIndex3() {
    fourCascade.getFoundationCardAt(-2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetFoundationCardAtInvalidIndex4() {
    fourCascade.getFoundationCardAt(4, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetFoundationCardAtBeforeStart1() {
    newFreecellGame.getFoundationCardAt(0, -3);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetFoundationCardAtBeforeStart2() {
    newFreecellGame.getFoundationCardAt(4, 0);
  }

  @Test
  public void testGetCascadeCardAt() {

    ICard card1 = fourCascade.getCascadeCardAt(0, 0);
    ICard card2 = fourCascade.getCascadeCardAt(2, 11);

    assertEquals(Suit.CLUBS, card1.getSuit());
    assertEquals(1, card1.getValue());

    assertEquals(Suit.HEARTS, card2.getSuit());
    assertEquals(12, card2.getValue());


  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtInvalidCardIndex1() {
    fourCascade.getCascadeCardAt(0, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtInvalidCardIndex2() {
    fourCascade.getCascadeCardAt(0, 13);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtInvalidPileIndex1() {
    fourCascade.getCascadeCardAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCascadeCardAtInvalidPileIndex2() {
    fourCascade.getCascadeCardAt(5, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCascadeCardAtBeforeStart1() {
    newFreecellGame.getCascadeCardAt(0, -3);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCascadeCardAtBeforeStart2() {
    newFreecellGame.getCascadeCardAt(-4, 0);
  }

  @Test
  public void testGetOpenCardAt() {
    fourCascade.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    ICard card = fourCascade.getOpenCardAt(0);

    assertEquals(Suit.CLUBS, card.getSuit());
    assertEquals(13, card.getValue());
  }

  @Test
  public void testGetOpenCardAtEmptyPile() {
    assertNull(fourCascade.getOpenCardAt(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOpenCardAtInvalidPileIndex1() {
    fourCascade.getOpenCardAt(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOpenCardAtInvalidPileIndex2() {
    fourCascade.getOpenCardAt(-2);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOpenCardAtBeforeStart1() {
    newFreecellGame.getOpenCardAt(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOpenCardAtBeforeStart2() {
    newFreecellGame.getOpenCardAt(-3); // Check game start before check index validity
  }

  /**
   * Concrete class for testing MultiMoveFreecellModel implementation of FreecellModel.
   */
  public static final class MultiMoveTest extends AbstractModelTest {

    @Override
    public FreecellModel<ICard> newModel() {
      return new MultiMoveFreecellModel();
    }

  }

  /**
   * Concrete class for testing SimpleFreecellModel implementation of FreecellModel.
   */
  public static final class SingleMoveTest extends AbstractModelTest {

    @Override
    public FreecellModel<ICard> newModel() {
      return new SimpleFreecellModel();
    }

  }

}
