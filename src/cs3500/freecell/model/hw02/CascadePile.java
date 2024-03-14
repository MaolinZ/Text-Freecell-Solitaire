package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import java.util.ArrayList;

/**
 * Represents a Cascade pile object in the game freecell. Cascade Piles can no limit on size. Adding
 * can only be done on the end of the pile if the added card is exactly one less and the opposite
 * suit as the last card in the pile
 */
class CascadePile extends Pile {

  public CascadePile(ArrayList<ICard> list) {
    super(list, PileType.CASCADE);
  }

  /**
   * Modified add method to follow Cascade Pile definition. The index must be the index of the last
   * card in the pile (or 0 if empty).
   *
   * @param index the index to add to
   * @param card  the given card to add
   * @throws IllegalArgumentException if the index is not the last in the pile, the method is given
   *                                  a null card, or the card does not meet criteria for adding to
   *                                  a cascade pile
   */
  @Override
  public void add(int index, ICard card) throws IllegalArgumentException {

    if (this.isEmpty()) { // No conditions to add to empty pile
      super.add(index, card);
    } else if (index == this.size()) { // If adding to end of cascade pile
      ICard curLast = this.list.get(this.size() - 1);

      if (card.getValue() + 1 != curLast.getValue()) {
        throw new IllegalArgumentException("Given card is not exactly 1 less than the last card");
      }

      // If black or red suits contain both the last card in the pile and the card being added
      if (card.sameColor(curLast)) {
        throw new IllegalArgumentException("Given card is not the opposite color");
      } else {
        super.add(index, card);
      }
    } else {
      throw new IllegalArgumentException("Must add to the end of a cascade pile");
    }

  }

}
