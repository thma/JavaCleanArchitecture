package interfaceadapters;

import usecases.Trace;

public class TraceDevNullImpl implements Trace {

  @Override
  public void trace(String message) {
    // write to /dev/null
  }
}
