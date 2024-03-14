import cs3500.freecell.model.hw02.SimpleFreecellModel;

/**
 * Mock freecell model for use in testing which sets gameover to always return true.
 * Helps in testing that the controller correctly ends the game and calls the view
 * to render the game over message.
 */
class MockModelAlwaysWon extends SimpleFreecellModel {

  // Only this method is used for testing correct appending of the game over message and
  // ending of the game
  @Override
  public boolean isGameOver() {
    return true; // Always return true to test for
  }

}