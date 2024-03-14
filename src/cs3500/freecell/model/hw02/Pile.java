package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import java.util.ArrayList;

/**
 * Abstract Pile class to represent generalized implementations of Pile functions. A list of cards
 * cannot be null.
 */
abstract class Pile implements IPile {

  protected final PileType type;
  protected ArrayList<ICard> list;

  Pile(ArrayList<ICard> list, PileType type) {

    if (list == null) {
      throw new IllegalArgumentException("Pile cannot accept null list");
    }

    this.list = list;
    this.type = type;
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean validIndex(int i) {
    return i == 0 || (i > 0 && i < this.size());
  }

  @Override
  public ICard getCard(int index) {

    if (this.validIndex(index)) {
      return this.list.get(index);
    } else {
      throw new IllegalArgumentException("Index out of bounds");
    }

  }

  @Override
  public PileType getType() {
    return this.type;
  }

  @Override
  public void add(int index, ICard card) throws IllegalArgumentException {

    // Check for null card or out of bound index
    if (card == null) {
      throw new IllegalArgumentException("Given null card to add");
    } else if (this.validIndex(index) || index == this
        .size()) { // If adding to the middle or at end of pile
      this.list.add(index, card);
      return;
    } else {
      throw new IllegalArgumentException(
          "Invalid add index given: " + index + " for pile of size " + this.size());
    }

  }

  @Override
  public ICard remove(int index) throws IllegalArgumentException {

    // Throw exception if attempting to remove from an empty pile or invalid index
    if (this.validIndex(index) && !this.isEmpty()) {
      return this.list.remove(index);
    } else {
      throw new IllegalArgumentException("Index out of bounds");
    }
  }

}
