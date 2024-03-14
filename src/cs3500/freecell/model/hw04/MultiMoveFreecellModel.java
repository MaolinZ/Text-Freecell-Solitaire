package cs3500.freecell.model.hw04;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.IPile;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A MultiMove freecell model. In other words, a simple freecell model with the ability to move
 * multiple cards between cascade piles if there are sufficient empty piles and the cards being
 * moved form a valid build both by themselves and when added to the last card of the destination
 * pile. All other moves (Cascade -> Open, etc.) must be performed one card at a time. All other
 * behavior is identical to SimpleFreecellModel
 */
public class MultiMoveFreecellModel extends SimpleFreecellModel {

  /**
   * Functionally the same as the SimpleFreecellModel implementation of move, however this move
   * method also has the ability to move multiple cards at a time if moving from one cascade pile to
   * another and the cards form a valid build before and after the move. Such a move is only allowed
   * if the number of cards moved is less than the number defined by (N + 1) * 2^K where N is the
   * number of empty open piles and K is the number of empty cascade piles.
   *
   * @param source         the PileType of the source pile
   * @param pileNumber     the pile number of the given source PileType
   * @param cardIndex      the index of the card in the pile
   * @param destination    the PileType of the destination pile
   * @param destPileNumber the pile number of the given destination PileType
   * @throws IllegalArgumentException if source or destination piletypes are null, the source type
   *                                  is a foundation pile, or a multimove is invalid (not enough
   *                                  open piles, cards do not form valid build, any other
   *                                  conditions that carry over from SimpleFreecellModel)
   * @throws IllegalStateException    if the game has not started
   */
  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException {

    if (!this.hasStarted()) {
      throw new IllegalStateException("Cannot attempt move(s) before game starts");
    }

    if (source == null || destination == null) {
      throw new IllegalArgumentException("MultiMove given null input");
    }
    if (source.equals(PileType.FOUNDATION)) {
      throw new IllegalArgumentException("Cannot move card(s) out of foundation pile.");
    }

    int numMoved = this.getNumCardsInCascadePile(pileNumber) - cardIndex;

    // Cannot be multimove if not Cascade -> Cascade, so treat as a normal move input.
    // Moving one card between cascades is also treated as a normal move
    if (!(source.equals(PileType.CASCADE) && destination.equals(PileType.CASCADE))
        || numMoved == 1) {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
    } else {

      if (pileNumber == destPileNumber) { // Moving to same cascade pile
        return;
      }

      if (numMoved > (this.numEmptyOpen() + 1) * Math.pow(2, this.numEmptyCascade())) {
        throw new IllegalArgumentException(
            "Number of cards exceeds allowed limit by empty intermediate piles");
      }

      ArrayList<ICard> cards = new ArrayList<>();
      for (int i = 0; i < numMoved; i++) {
        cards.add(
            this.getCascadeCardAt(pileNumber, this.getNumCardsInCascadePile(pileNumber) - i - 1));
      }

      if (this.validBuild(cards)) {
        if (this.getNumCardsInCascadePile(pileNumber) == 0) { // If destination is empty
          return;
        } else { // Check that added cards form valid build with last card
          ICard last = this.getCascadeCardAt(destPileNumber,
              this.getNumCardsInCascadePile(destPileNumber) - 1);
          ICard toAdd = cards.get(0);
          if (!(toAdd.getValue() == last.getValue() - 1 && !toAdd.sameColor(last))) {
            throw new IllegalArgumentException(
                "Selected cards do not form a valid build with the last existing card");
          }
        }
        IPile sourcePile = this.cascade.get(pileNumber);
        IPile destPile = this.cascade.get(destPileNumber);

        for (ICard c : cards) {
          sourcePile.remove(sourcePile.size() - 1);
          destPile.add(destPile.size(), c);
        }

      } else {
        throw new IllegalArgumentException(
            "Selected cards form an invalid build and cannot be moved: " + cards.toString());
      }
    }

  }

  private boolean validBuild(List<ICard> cards) {

    if (cards == null) {
      throw new IllegalArgumentException();
    }
    if (cards.size() == 0) {
      return true;
    }
    cards.sort(new DescendingCards());

    ArrayList<ICard> copyCards = new ArrayList<>(cards);
    ICard last = copyCards.remove(0);

    for (ICard c : copyCards) {
      if (c.getValue() != last.getValue() - 1 || c.sameColor(last)) {
        return false;
      }
      last = c;
    }
    return true;
  }

  private int numEmptyCascade() {

    int n = 0;

    for (int i = 0; i < this.getNumCascadePiles(); i++) {
      if (this.getNumCardsInCascadePile(i) == 0) {
        n += 1;
      }
    }
    return n;
  }

  private int numEmptyOpen() {

    int n = 0;

    for (int i = 0; i < this.getNumOpenPiles(); i++) {
      if (this.getNumCardsInOpenPile(i) == 0) {
        n += 1;
      }
    }
    return n;
  }

  private static class DescendingCards implements Comparator<ICard> {

    @Override
    public int compare(ICard o1, ICard o2) {
      if (o1.getValue() > o2.getValue()) {
        return -1;
      } else if (o1.getValue() == o2.getValue()) {
        return 0;
      } else {
        return 1;
      }
    }
  }

}
