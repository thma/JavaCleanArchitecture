package z_reservation_example;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import z_reservation_example.a_domain.Reservation;
import z_reservation_example.b_usecases.KeyValueStore;
import z_reservation_example.b_usecases.ReservationUseCase;
import z_reservation_example.b_usecases.Trace;
import z_reservation_example.c_interfaceadapters.KVSInMemoryImpl;
import z_reservation_example.c_interfaceadapters.TraceConsoleImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static z_reservation_example.a_domain.Reservation.usedCapacity;

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
