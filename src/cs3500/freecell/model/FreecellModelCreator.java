package cs3500.freecell.model;

import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;

/**
 * FreecellModelCreator class.
 */
public class FreecellModelCreator {

  /**
   * Creates a new FreecellModel depending on the specified GameType.
   *
   * @param m GameType
   * @return a FreecellModel of the specified GameType
   */
  public static FreecellModel<ICard> create(GameType m) {

    if (m == null) {
      throw new IllegalArgumentException();
    }

    switch (m) {

      case SINGLEMOVE:
        return new SimpleFreecellModel();
      case MULTIMOVE:
        return new MultiMoveFreecellModel();
      default:
        throw new IllegalArgumentException();

    }
  }

  /**
   * GameType enum for creating models with factory pattern.
   */
  public static enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

}
