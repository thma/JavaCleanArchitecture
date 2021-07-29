package c_interfaceadapters;

import b_usecases.Trace;

public class TraceDevNullImpl implements Trace {

  @Override
  public void trace(String message) {
    // write to /dev/null
  }
}
