package c_interfaceadapters;

import a_domain.Reservation;
import b_usecases.KeyValueStore;
import b_usecases.ReservationUseCase;
import b_usecases.Trace;
import org.springframework.stereotype.Service;

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
