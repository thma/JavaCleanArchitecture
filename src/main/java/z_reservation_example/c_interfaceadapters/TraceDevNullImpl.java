package z_reservation_example.c_interfaceadapters;

import z_reservation_example.b_usecases.Trace;

public class TraceDevNullImpl implements Trace {

  @Override
  public void trace(String message) {
    // write to /dev/null
  }
}
