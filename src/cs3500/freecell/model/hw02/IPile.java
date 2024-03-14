package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;

/**
 * Interface for a pile of cards. Contains functions which a pile should be able to implement
 */
public interface IPile {

  /**
   * Determines if the current pile is empty.
   *
   * @return true if empty and false otherwise
   */
  boolean isEmpty();

  /**
   * Counts the number of cards in this pile.
   *
   * @return the number of cards in the pile
   */
  int size();

  /**
   * Determines if the given index is a valid one in the current pile. (Not necessarily just the
   * indices that can be moved in the game)
   *
   * @param i the input index to be tested
   * @return true if the index is valid or false if it is not
   */
  boolean validIndex(int i);

  /**
   * Gets the ICard at the given index.
   *
   * @param index the index to retrieve the ICard from
   * @return the ICard at the given index
   * @throws IllegalArgumentException if the given index is invalid or out of bounds
   */
  ICard getCard(int index) throws IllegalArgumentException;

  /**
   * Getter for the PileType of the pile.
   *
   * @return the PileType of this IPile
   */
  PileType getType();

  /**
   * Adds a given card to the pile at the given index.
   *
   * @param card  the card to be added
   * @param index the index to add to card at
   * @throws IllegalArgumentException if the move is invalid for the pile, the given card is null,
   *                                  or the index is invalid (less than 0 or exceeding the size of
   *                                  the pile)
   */
  void add(int index, ICard card) throws IllegalArgumentException;

  /**
   * Removes the card at the given index, returning that card as the value.
   *
   * @param index the index to remove the card from.
   * @throws IllegalArgumentException if the given index is negative or exceeds list's indices
   */
  ICard remove(int index) throws IllegalArgumentException;

}
