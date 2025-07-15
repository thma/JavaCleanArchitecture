package b_usecases;

/*
This module specifies the Use Case layer for the Reservation system.
It coordinates access to Effects and the actual a_domain logic.
The module exposes service functions that will be used by the REST API in the External layer.
Implemented Use Cases:
1. Display the number of available seats for a given day
2. Enter a reservation for a given day and keep it persistent.
   If the reservation can not be served as all seats are occupies provide a functional error message stating
   the issue.
3. Display the list of reservations for a given day.
4. Delete a given reservation from the system in case of a cancellation.
   NO functional error is required if the reservation is not present in the system.
5. Display a List of all reservation in the system.

Please note: all functions in this module are pure and total functions.
This makes it easy to test them in isolation.
 */

import a_domain.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationUseCase {

  private KeyValueStore<LocalDate, List<Reservation>> kvs;

  private Trace log;

  public ReservationUseCase(KeyValueStore<LocalDate, List<Reservation>> kvs, Trace log) {
    this.kvs = kvs;
    this.log = log;
  }

  public Trace getTrace() {
    return log;
  }

  public KeyValueStore<LocalDate, List<Reservation>> getKvs() {
    return kvs;
  }

  public int maxCapacity() {
    return 20;
  }

  /**
   * implements UseCase 1: Display number of available seats for a given day
   *
   * @param date the date
   * @return the number of available seats
   */
  public int availableSeats(LocalDate date) {
    log.trace("compute available seats for " + date);
    List<Reservation> todaysReservations = kvs.get(date).orElse(new ArrayList<>());
    return Reservation.availableSeats(maxCapacity(), todaysReservations);
  }

  public void tryReservation(Reservation reservation) throws ReservationNotPossibleException {
    log.trace("trying to reservate " + reservation.getQuantity() + " more seats on " + reservation.getDate());
    var todaysReservations = kvs.get(reservation.getDate()).get();
    var available = availableSeats(reservation.getDate());
    if (reservation.isReservationPossible(todaysReservations, maxCapacity())) {
      persistReservation(reservation, todaysReservations);
    } else {
      throw new ReservationNotPossibleException("Sorry, only " + available + " seats left on " + reservation.getDate());
    }
  }

  private void persistReservation(Reservation reservation, List<Reservation> reservationsOnSameDay) {
    var updated = new ArrayList<>(reservationsOnSameDay);
    updated.add(reservation);
    kvs.insert(reservation.getDate(), updated);
  }

}
