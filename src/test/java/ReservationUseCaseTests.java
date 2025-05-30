import a_domain.Reservation;
import b_usecases.KeyValueStore;
import b_usecases.ReservationUseCase;
import b_usecases.Trace;
import c_interfaceadapters.KVSInMemoryImpl;
import c_interfaceadapters.TraceConsoleImpl;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static a_domain.Reservation.usedCapacity;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class ReservationUseCaseTests {

  private LocalDate aDay = LocalDate.now();
  private KeyValueStore<LocalDate, List<Reservation>> kvs = new KVSInMemoryImpl<>();
  private Trace log = new TraceConsoleImpl();

  private Reservation fromRequestedCapacity(int requested) {
    return new Reservation(aDay, "happy customer", "happy@customer.com", requested);
  }

  @Property
  public void checkUsedCapacity(List<Integer> ints) {
    List<Reservation> reservations = ints.stream().map(this::fromRequestedCapacity).toList();
    int expected = ints.stream()
        .map(Math::abs)
        .mapToInt(Integer::intValue)
        .sum();
    assertEquals(expected, usedCapacity(reservations));
  }


  @Property
  public void checkInitialCapacity() {
    ReservationUseCase uc = new ReservationUseCase(kvs, log);
    assertEquals(uc.maxCapacity(), uc.availableSeats(aDay));
  }

}
