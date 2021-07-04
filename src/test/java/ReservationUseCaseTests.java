import Domain.Reservation;
import InterfaceAdapters.KVSInMemoryImpl;
import InterfaceAdapters.TraceConsoleImpl;
import UseCases.KeyValueStore;
import UseCases.ReservationUseCase;
import UseCases.Trace;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.javatuples.Pair;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Domain.Reservation.usedCapacity;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class ReservationUseCaseTests {

  private LocalDate aDay = LocalDate.of(2021,6,4);

  private Reservation fromRequestedCapacity(int requested) {
    return new Reservation(aDay,"hapy customer", "happy@customer.com", requested);
  }

  private KeyValueStore<LocalDate, List<Reservation>> kvs = new KVSInMemoryImpl<>();

  private Trace log = new TraceConsoleImpl();


  @Property
  public void checkUsedCapacity(List<Integer> ints) {
    List<Reservation> reservations = ints.stream().map(this::fromRequestedCapacity).collect(Collectors.toList());
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
