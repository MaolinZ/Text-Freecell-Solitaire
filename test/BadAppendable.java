import java.io.IOException;

/**
 * Mock appendable class which only throws IOException.
 */
class BadAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Mock appendable.");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Mock appendable.");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Mock appendable.");
  }
}