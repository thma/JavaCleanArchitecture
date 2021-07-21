package interfaceadapters;

import domain.Reservation;
import usecases.KeyValueStore;
import usecases.ReservationUseCase;
import usecases.Trace;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

  public ReservationService(KeyValueStore<LocalDate, List<Reservation>> kvs, Trace log) {
    reservationUC = new ReservationUseCase(kvs, log);
  }

  private ReservationUseCase reservationUC;

  public int availableSeats(LocalDate date) {
    return reservationUC.availableSeats(date, reservationUC.getKvs(), reservationUC.getTrace());
  }

}
