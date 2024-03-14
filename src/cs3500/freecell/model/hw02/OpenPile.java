package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import java.util.ArrayList;

/**
 * Open Pile object, capable of storing no more than one card.
 */
class OpenPile extends Pile {

  public OpenPile(ArrayList<ICard> list) {
    super(list, PileType.OPEN);

    if (list.size() > 1) {
      throw new IllegalArgumentException("OpenPile exceeds length of 1");
    }
  }

  public OpenPile() {
    this(new ArrayList<ICard>());
  }

  @Override
  public boolean validIndex(int i) {
    return i == 0; // Open pile can only have up to one card, so only the first index can be used
  }

  /**
   * Adds the given card to this open pile at the given index. (Due to the nature of open piles, the
   * only valid index is zero on an empty pile).
   *
   * @param card  the given ICard implementation to add
   * @param index the index to add the ICard at
   * @throws IllegalArgumentException if the given card is null or the index is invalid
   */
  @Override
  public void add(int index, ICard card) {

    if (this.isEmpty()) {
      super.add(index, card);
    } else {
      throw new IllegalArgumentException("Open Pile already occupied");
    }
  }

  /**
   * Removes and returns the ICard at the given index. (Due to the nature of open piles, the only
   * valid index is zero on a non-empty open pile).
   *
   * @param index the index to retrieve the card from
   * @return the ICard at the given index
   * @throws IllegalArgumentException if attempted to remove from an empty open pile or given an
   *                                  invalid index
   */
  @Override
  public ICard remove(int index) {

    if (this.isEmpty()) {
      throw new IllegalArgumentException("Cannot remove from empty open pile");
    } else {
      return super.remove(index);
    }

  }

}
