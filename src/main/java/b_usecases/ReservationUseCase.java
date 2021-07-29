package b_usecases;

/*
This module specifies the Use Case layer for the Reservation system.
It coordinates access to Effects and the actual domain logic.
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
All Effects are specified as Polysemy Members.
Interpretation of Effects is implemented on the level of application assembly, or in the context of unit tests.
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

  /*
  -- | try to add a reservation to the table.
-- | Return Just the modified table if successful, else return Nothing
-- | implements UseCase 2.
tryReservation :: (Member Persistence r, Member (Error ReservationError) r, Member Trace r) => Dom.Reservation -> Sem r ()
tryReservation res@(Dom.Reservation date _ _ requestedQuantity)  = do
  trace $ "trying to reservate " ++ show requestedQuantity ++ " more seats on " ++ show date
  todaysReservations <- fetch date
  let available = Dom.availableSeats maxCapacity todaysReservations
  if Dom.isReservationPossible res todaysReservations maxCapacity
    then persistReservation res
    else throw $ ReservationNotPossible ("Sorry, only " ++ show available ++ " seats left on " ++ show date)

  where
    -- | persist a reservation to the reservation table.
    persistReservation :: (Member (KVS Day [Dom.Reservation]) r, Member Trace r)  => Dom.Reservation -> Sem r ()
    persistReservation r@(Dom.Reservation day _ _ _ ) = do
      trace $ "enter a new reservation to KV store: " ++ show r
      rs <- fetch day
      let updated = Dom.addReservation r rs
      trace $ "storing: " ++ show updated
      insertKvs day updated
   */

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
