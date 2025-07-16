package z_reservation_example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import z_reservation_example.a_domain.Reservation;
import z_reservation_example.b_usecases.KeyValueStore;
import z_reservation_example.b_usecases.Trace;
import z_reservation_example.c_interfaceadapters.KVSInMemoryImpl;
import z_reservation_example.c_interfaceadapters.ReservationService;
import z_reservation_example.c_interfaceadapters.TraceDevNullImpl;

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
