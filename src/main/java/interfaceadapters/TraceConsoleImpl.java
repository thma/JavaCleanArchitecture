package interfaceadapters;

import usecases.Trace;

public class TraceConsoleImpl implements Trace {

  // shell support for colors: "\\e[32m"

  @Override
  public void trace(String message) {
    System.out.println(message);
  }
}
