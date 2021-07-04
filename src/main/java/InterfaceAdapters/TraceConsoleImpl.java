package InterfaceAdapters;

import UseCases.Trace;

public class TraceConsoleImpl implements Trace {


  @Override
  public void trace(String message) {
    System.out.println(message);
  }
}
