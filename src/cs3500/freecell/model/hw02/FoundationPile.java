package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import java.util.ArrayList;

/**
 * Represents a Foundation Pile object in the game freecell. Foundations piles have unlimited size
 * and can only be added to if the card added is the same suit and exactly 1 greater than the last
 * card in the pile. (If pile is empty, can be any suit, but card must be an Ace).
 */
class FoundationPile extends Pile {

  public FoundationPile(ArrayList<ICard> list) {
    super(list, PileType.FOUNDATION);
  }

  public FoundationPile() {
    this(new ArrayList<>());
  }

  /**
   * Adds the given ICard to this pile at the given index. (Only the last index is valid for a
   * foundation pile)
   *
   * @param index the index to add the ICard at
   * @param card  the given ICard to be added
   * @throws IllegalArgumentException if the index is invalid or the ICard is null or an invalid
   *                                  addition (not same suit or not exactly 1 larger)
   */
  @Override
  public void add(int index, ICard card) throws IllegalArgumentException {
    if (this.isEmpty()) {
      if (card.getValue() == 1) {
        super.add(index, card); // Can take any Ace if empty foundation pile
      } else {
        throw new IllegalArgumentException("First card of a foundation pile must be an ace");
      }

    } else if (index == this.size()) { // If adding to end of pile
      ICard curLast = this.list.get(this.size() - 1);

      // Check if same suit and greater by exactly 1
      if (curLast.getSuit().equals(card.getSuit()) && curLast.getValue() + 1 == card.getValue()) {
        super.add(index, card);
      } else {
        throw new IllegalArgumentException("Invalid foundation card addition");
      }
    } else { // If not adding to end of pile
      throw new IllegalArgumentException("Must add card to last index of an foundation pile");
    }

  }

}
