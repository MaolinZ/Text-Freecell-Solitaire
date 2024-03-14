import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import java.util.List;
import java.util.Objects;

/**
 * Mock model which logs the inputs to start game for verification.
 */
class MockModelStartGame implements FreecellModel<ICard> {

  final StringBuilder log;

  MockModelStartGame(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public List<ICard> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {

    log.append(String
        .format("Deck: %s, NumCascade: %d, NumOpen: %d, Shuffle: %b", deck, numCascadePiles,
            numOpenPiles, shuffle));

  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    return;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  // Methods below this point don't matter for testing

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public int getNumCascadePiles() {
    return 0;
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public int getNumOpenPiles() {
    return 0;
  }

  @Override
  public ICard getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public ICard getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public ICard getOpenCardAt(int pileIndex) throws IllegalArgumentException, IllegalStateException {
    return null;
  }
}