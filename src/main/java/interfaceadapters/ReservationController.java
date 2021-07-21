package interfaceadapters;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  private ReservationService reservationService;


  @GetMapping("/availableSeats/{date}")
  public int getAvailableSeats(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
    return reservationService.availableSeats(date);
  }

}
