import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiMoveFreecellModel;
import org.junit.Test;

/**
 * Testing class for FreecellModelCreator.
 */
public class FreecellModelCreatorTest {

  FreecellModelCreator c = new FreecellModelCreator();

  @Test
  public void testCreateSingleMove() {

    assertEquals(SimpleFreecellModel.class, c.create(GameType.SINGLEMOVE).getClass());

  }

  @Test
  public void testCreateMultiMove() {

    assertEquals(MultiMoveFreecellModel.class, c.create(GameType.MULTIMOVE).getClass());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalid() {
    c.create(null);
  }

}