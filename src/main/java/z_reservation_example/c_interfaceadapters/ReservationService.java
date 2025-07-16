package z_reservation_example.c_interfaceadapters;

import org.springframework.stereotype.Service;
import z_reservation_example.a_domain.Reservation;
import z_reservation_example.b_usecases.KeyValueStore;
import z_reservation_example.b_usecases.ReservationUseCase;
import z_reservation_example.b_usecases.Trace;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

  private ReservationUseCase reservationUC;

  public ReservationService(KeyValueStore<LocalDate, List<Reservation>> kvs, Trace log) {
    reservationUC = new ReservationUseCase(kvs, log);
  }

  public int availableSeats(LocalDate date) {
    return reservationUC.availableSeats(date);
  }

}
