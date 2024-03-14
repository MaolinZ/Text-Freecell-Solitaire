import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import java.util.Objects;

class MockModelMove extends SimpleFreecellModel {

  StringBuilder log;

  MockModelMove(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    String src = AbstractFreecellControllerTest.pileToString(source);
    String dest = AbstractFreecellControllerTest.pileToString(destination);

    this.log.append(String
        .format("SourceType: %s, PileNumber: %d, CardIndex: %d, DestType: %s, DestPileNum: %d", src,
            pileNumber, cardIndex, dest, destPileNumber));

    super.move(source, pileNumber, cardIndex, destination, destPileNumber);

  }
}