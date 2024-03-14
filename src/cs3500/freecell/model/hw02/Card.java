package cs3500.freecell.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Card implementation of ICard, representing a Card in the game of Freecell. Cards have a value
 * representing A, 1, ... , K and a suit (one of CLUBS, SPADES, DIAMONDS, or HEARTS).
 */
class Card implements ICard {

  private final Suit suit;
  private final int value;

  /**
   * Constructs a Card object with its suit and card number.
   *
   * @param suit  the suit of the card
   * @param value the value of the card (Ace, 2-10, etc.). Non-numerical values are represented as
   *              integers (ex. Ace = 1, King = 13)
   * @throws IllegalArgumentException if {@code suit} is null or {@code value} is not greater than 0
   *                                  and less than 14
   */
  public Card(Suit suit, int value) throws IllegalArgumentException {

    // If statements check for invalid inputs
    if (suit == null) {
      throw new IllegalArgumentException("Suit cannot be null");
    }
    if (!((value > 0) && (value < 14))) {
      throw new IllegalArgumentException("Invalid card value given.");
    }

    this.suit = suit;
    this.value = value;
  }

  @Override
  public Suit getSuit() {
    return this.suit;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public boolean sameColor(ICard that) {
    ArrayList<Suit> black = new ArrayList<>(Arrays.asList(Suit.CLUBS, Suit.SPADES));
    ArrayList<Suit> red = new ArrayList<>(Arrays.asList(Suit.HEARTS, Suit.DIAMONDS));

    // If black or red suits contain both the last card in the pile and the card being added
    return ((black.contains(this.getSuit()) && black.contains(that.getSuit())) || (
        red.contains(this.getSuit()) && red.contains(that.getSuit())));
  }

  @Override
  public String toString() {

    String result;

    // If/else handles conversion to letter value if applicable
    if (this.value < 11 && this.value > 1) {
      result = Integer.toString(this.value);
    } else {

      switch (this.value) {
        case 1:
          result = "A";
          break;
        case 11:
          result = "J";
          break;
        case 12:
          result = "Q";
          break;
        case 13:
          result = "K";
          break;
        default:
          throw new IllegalStateException("Card has invalid value");
      }
    }

    switch (this.suit) {
      case CLUBS:
        result = result + "♣";
        break;
      case DIAMONDS:
        result = result + "♦";
        break;
      case HEARTS:
        result = result + "♥";
        break;
      case SPADES:
        result = result + "♠";
        break;
      default:
        throw new IllegalStateException();
    }

    return result;

  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof Card)) {
      return false;
    }
    Card card = (Card) o;
    return this.getValue() == card.getValue() && this.getSuit() == card.getSuit();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSuit(), getValue());
  }
}
