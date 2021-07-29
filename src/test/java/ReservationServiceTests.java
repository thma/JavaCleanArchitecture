import a_domain.Reservation;
import b_usecases.KeyValueStore;
import b_usecases.Trace;
import c_interfaceadapters.KVSInMemoryImpl;
import c_interfaceadapters.ReservationService;
import c_interfaceadapters.TraceDevNullImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationServiceTests {

  @Autowired
  private ReservationService reservationService;

  @Test
  void simpleTest() {
    assertEquals(20, reservationService.availableSeats(LocalDate.now()));
  }

  @SpringBootConfiguration
  static class ApplicationConfiguration {
    @Bean
    KeyValueStore<LocalDate, List<Reservation>> getKvs() {
      return new KVSInMemoryImpl<>();
    }

    @Bean
    Trace getTrace() {
      return new TraceDevNullImpl();
    }

    @Bean
    ReservationService getReservationService() {
      return new ReservationService(getKvs(), getTrace());
    }
  }

}
