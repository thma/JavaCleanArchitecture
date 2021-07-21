package domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * This module implements the business logic for seat reservations in a very small boutique restaurant.
 * The restaurant has only one table with 20 tables.
 * Each day the restaurants accepts only 20 seat reservations. (There is no limited time-slot for each guest.)
 * Please note: all functions in this module are pure and total.
 * This makes it easy to test them in isolation.
 */
public class Reservation {

  /**
   * the date of the reservation
   */
  private final LocalDate date;

  /**
   * the name of the guest placing the reservation
   */
  private final String name;

  /**
   * the email address of the guest
   */
  private final String emailAddress;

  /**
   * the number of seats requested. must be non-negative!
   */
  private final int quantity;


  public Reservation(LocalDate date, String name, String emailAddress, int quantity) {
    this.date = date;
    this.name = name;
    this.emailAddress = emailAddress;
    this.quantity = Math.abs(quantity);
  }

  /**
   * a key value map holding a list of reservations for any given day
   */
  public class ReservationMap extends HashMap<String, Reservation> {
  }

  /**
   *   computes the number of reserved seats for a list of reservations
   */
  public static int usedCapacity(List<Reservation> reservations){
    if (reservations == null || reservations.isEmpty()) {
      return 0;
    } else {
      var acc = 0;
      for (Reservation res: reservations) {
        acc+= res.quantity;
      }
      return acc;
    }
  }

  /**
   * check whether it is possible to add a reservation to the table.
   * Return True if successful, else return False
   * @param reservationsOnDay
   * @param maxCapacity
   * @return
   */
  public boolean isReservationPossible(List<Reservation> reservationsOnDay, int maxCapacity) {
    return (availableSeats(maxCapacity, reservationsOnDay) >= this.quantity);
  }

  /**
   * returns the number of available seats given a maximum capacity and a list of active reservations
   * @param maxCapacity
   * @param reservationsOnDay
   * @return the number of available seats or zero if max capacity is already exceeded
   */
  public static int availableSeats(int maxCapacity, List<Reservation> reservationsOnDay) {
    int max = Math.abs(maxCapacity);
    if (max < usedCapacity(reservationsOnDay)) {
      return 0;
    } else {
      return max - usedCapacity(reservationsOnDay);
    }
  }

  /**
   * add this reservation to a given list of reservations
   * @param reservations
   * @return
   */
  public void addReservation(List<Reservation> reservations) {
    reservations.add(this);
  }

  /**
   * cancel a reservation for a given list of reservations.
   * @param reservations
   */
  public void cancelReservation(List<Reservation> reservations) {
    reservations.remove(this);
  }

}
