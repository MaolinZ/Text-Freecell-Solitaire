package cs3500.freecell.model.hw02;

/**
 * Interface of the Card type. Contains functions which an implementation of a Card class should
 * support.
 */
public interface ICard {

  /**
   * Retrieves the Suit of this card.
   *
   * @return the Suit of this card
   */
  Suit getSuit();

  /**
   * Retrieves the integer representation of the value of this card.
   *
   * @return the integer value of this card
   */
  int getValue();

  /**
   * Determines if this ICard and the given ICard object are the same color.
   *
   * @param that the given ICard to compare this ICard to
   * @return true if sameColor or false if they are not.
   */
  boolean sameColor(ICard that);

  /**
   * Creates a String representation of this Card.
   *
   * @return a String representation of the Card with the value, then suit symbol
   */
  @Override
  String toString();

  /**
   * Determines equality between cards. Should return equal if suit and value are the same.
   *
   * @param o the object to compare to
   * @return true for equal and false for not
   */
  @Override
  boolean equals(Object o);

}
