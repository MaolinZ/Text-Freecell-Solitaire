import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Mock readable class which only throws IOException.
 */
class BadReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException("Mock readable.");
  }
}