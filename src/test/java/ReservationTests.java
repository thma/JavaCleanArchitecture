import domain.Reservation;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static domain.Reservation.usedCapacity;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class ReservationTests {

  private LocalDate aDay = LocalDate.of(2021,6,4);

  private Reservation fromRequestedCapacity(int requested) {
    return new Reservation(aDay,"hapy customer", "happy@customer.com", requested);
  }


  @Property
  public void checkUsedCapacity(List<Integer> ints) {
    List<Reservation> reservations = ints.stream().map(this::fromRequestedCapacity).collect(Collectors.toList());
    int expected = ints.stream()
        .map(Math::abs)
        .mapToInt(Integer::intValue)
        .sum();
    assertEquals(usedCapacity(reservations), expected);
  }
}
