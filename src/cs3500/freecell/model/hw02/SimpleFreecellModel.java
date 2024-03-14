package cs3500.freecell.model.hw02;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple freecell model, implementing FreecellModel of type ICard. Piles can only be modified by
 * the last card. Only foundation piles cannot have their last card removed. To add to an open pile,
 * the pile must be empty. Open piles have a size limit of 1 card. To add to a cascade pile, the
 * card being added must be the opposite color and exactly one greater than the current last card in
 * the pile. To add to a foundation pile, the card must be the same suit and exactly one less than
 * the last card. Any card can be added to cascade piles if they are empty. To add to an empty
 * foundation pile, the card must be an ace of any suit. The game is won when all open and cascade
 * piles are empty.
 */
public class SimpleFreecellModel implements FreecellModel<ICard> {


  // CHANGED HW04: Set fields to protected from private so subclasses can access them
  protected Status gameState = Status.PRIOR;
  protected ArrayList<OpenPile> open;
  protected ArrayList<FoundationPile> foundation;
  protected ArrayList<CascadePile> cascade;

  @Override
  public List<ICard> getDeck() {
    ArrayList<ICard> deck = new ArrayList<>();

    for (int i = 1; i < 14; i++) {
      for (Suit s : Suit.values()) {
        deck.add(new Card(s, i));
      }
    }

    return deck;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {

    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Invalid number of cascade piles");
    }
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Invalid number of open piles");
    }
    // Check deck validity
    if (!this.validDeck(deck)) {
      throw new IllegalArgumentException("Invalid deck");
    }

    // Initialize empty foundation piles
    this.foundation = new ArrayList<>(
        Arrays.asList(new FoundationPile(), new FoundationPile(), new FoundationPile(),
            new FoundationPile()));

    // Initialize empty open piles
    this.open = new ArrayList<>();

    for (int i = 0; i < numOpenPiles; i++) {
      this.open.add(new OpenPile());
    }

    ArrayList<ICard> deckCopy = new ArrayList<>(deck);

    // Shuffle if applicable
    if (shuffle) {
      Collections.shuffle(deckCopy);
    }

    this.cascade = new ArrayList<>();

    // Roundrobin dealing
    for (int i = 0; i < numCascadePiles; i++) {
      ArrayList<ICard> cards = new ArrayList<>();

      for (int x = i; x < 52; x = x + numCascadePiles) {
        cards.add(deckCopy.get(x));
      }
      this.cascade.add(new CascadePile(new ArrayList<>(cards)));
    }

    this.gameState = Status.PLAYING;

  }

  /**
   * Determines if a given deck is valid.
   *
   * @param deck the given List representation of a deck of Cards
   * @return true for valid deck, false for invalid
   */
  private boolean validDeck(List<ICard> deck) {

    if (deck == null) { // Check if given deck is null
      return false;
    }

    // Return false if invalid size
    if (deck.size() != 52) {
      return false;
    }

    // For each suit
    for (Suit s : Suit.values()) {
      // Ensure there are cards with value 1-13 (Ace -> King, or values 1 -> 13)
      for (int i = 1; i < 14; i++) {
        if (!deck.contains(new Card(s, i))) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    // Check for game start
    if (!this.hasStarted()) {
      throw new IllegalStateException("Cannot attempt move before game starts");
    }

    // Check for validity of inputs
    if (source == null || destination == null) { // Null inputs
      throw new IllegalArgumentException("Cannot accept null piletype for move parameters");
    } else if (source.equals(PileType.FOUNDATION)) { // Moving out of foundation pile
      throw new IllegalArgumentException("Cannot move card out of foundation pile");
    } else if (source.equals(destination) && pileNumber == destPileNumber) { // Moving to same pile
      return; // Do nothing
    } else {
      IPile sourcePile = this.selectPile(source, pileNumber);
      IPile destPile = this.selectPile(destination, destPileNumber);

      if (sourcePile.isEmpty()) { // Check if source pile empty
        throw new IllegalArgumentException("Cannot move card from an empty source pile");
      }

      // Simple model only operates (adds/removes) on the last index of a pile
      if (sourcePile.size() - 1 == cardIndex) {
        ICard sourceCard = sourcePile.getCard(cardIndex);

        destPile.add(destPile.size(), sourceCard);
        sourcePile.remove(cardIndex);
      } else {
        throw new IllegalArgumentException("Invalid card index " + cardIndex);
      }

    }
  }

  private IPile selectPile(PileType type, int pileNumber) throws IllegalArgumentException {

    // Ensure non negative index
    if (pileNumber < 0) {
      throw new IllegalArgumentException("Pile number must be positive");
    }

    switch (type) {

      case OPEN:
        if (pileNumber < this.open.size()) {
          return this.open.get(pileNumber);
        }
        break;
      case FOUNDATION:
        if (pileNumber < this.foundation.size()) {
          return this.foundation.get(pileNumber);
        }
        break;
      case CASCADE:
        if (pileNumber < this.cascade.size()) {
          return this.cascade.get(pileNumber);
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid PileType");
    }
    throw new IllegalArgumentException(
        "Pile number out of bounds. Given " + pileNumber + " for " + this.open.size() + " open, "
            + this.cascade.size() + " cascade, " + this.foundation.size() + " foundation");
  }

  @Override
  public boolean isGameOver() { // Game is over if no cards are left in the open or cascade piles

    if (this.hasStarted()) {

      for (CascadePile c : this.cascade) { // Check cascade piles
        if (!c.isEmpty()) {
          return false;
        }
      }

      for (OpenPile p : this.open) { // Check open piles
        if (!p.isEmpty()) {
          return false;
        }
      }

      return true;

    } else {
      return false; // Game has not started
    }

  }

  // CHANGED HW04: Access modifier set to protected to let subclasses use this helper method

  /**
   * Determines if the game has started yet.
   *
   * @return true if the game is in progress or won and false otherwise.
   */
  protected boolean hasStarted() {
    return !gameState.equals(Status.PRIOR);
  }

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return this.getNumCards(PileType.FOUNDATION, index);
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return this.getNumCards(PileType.CASCADE, index);
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return this.getNumCards(PileType.OPEN, index);
  }

  /**
   * Gets the number of cards in a pile given the type and index of the pile in the model.
   *
   * @param type  the PileType of the pile
   * @param index the index of this PileType to count cards from
   * @return the number of cards in the pile at the given index and PileType
   * @throws IllegalStateException    if the game has not started
   * @throws IllegalArgumentException if the given index is invalid
   */
  private int getNumCards(PileType type, int index)
      throws IllegalStateException, IllegalArgumentException {

    if (this.hasStarted()) {
      return this.selectPile(type, index).size();
    } else {
      throw new IllegalStateException("Game has not started");
    }

  }

  @Override
  public int getNumOpenPiles() {
    if (this.hasStarted()) {
      return this.open.size();
    } else {
      return -1;
    }
  }

  @Override
  public int getNumCascadePiles() {
    if (this.hasStarted()) {
      return this.cascade.size();
    } else {
      return -1;
    }
  }

  @Override
  public ICard getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return this.getCardAt(PileType.FOUNDATION, pileIndex, cardIndex);
  }

  @Override
  public ICard getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return this.getCardAt(PileType.CASCADE, pileIndex, cardIndex);
  }

  @Override
  public ICard getOpenCardAt(int pileIndex) throws IllegalArgumentException, IllegalStateException {

    if (!this.hasStarted()) {
      throw new IllegalStateException("Cannot query card in open pile before game starts");
    }

    IPile pile = this.selectPile(PileType.OPEN, pileIndex);

    if (pile.isEmpty()) { // Check if open pile is empty, return null if true
      return null;
    } else {
      return this.getCardAt(PileType.OPEN, pileIndex, 0);
    }
  }

  private ICard getCardAt(PileType type, int pileIndex, int cardIndex) {
    if (this.hasStarted()) {
      IPile source = this.selectPile(type, pileIndex);

      if (source.isEmpty()) { // throw exception if pile is empty
        throw new IllegalArgumentException("Cannot retrieve card from empty pile");
      } else {
        if (cardIndex == 0 || (cardIndex > 0 && cardIndex < source
            .size())) { // Check for valid cardIndex
          return source.getCard(cardIndex);
        } else {
          throw new IllegalArgumentException(
              "Invalid pile index. Pile size is " + source.size() + ", given index " + cardIndex);
        }
      }


    } else {
      throw new IllegalStateException("Game has not started");
    }
  }

}
