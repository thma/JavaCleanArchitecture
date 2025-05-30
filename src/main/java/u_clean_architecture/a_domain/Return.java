package u_clean_architecture.domain;

import java.time.LocalDate;

public class Return {
  private Long id;
  private Long orderId;
  private String reason;
  private LocalDate createdAt;

  public Return(Long id, Long orderId, String reason, LocalDate createdAt) {
    this.id = id;
    this.orderId = orderId;
    this.reason = reason;
    this.createdAt = createdAt;
  }

  public Return(Long id, LocalDate createdAt) {
    this.id = id;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public String getReason() {
    return reason;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }
}
