package u_clean_architecture.c_ports;

import u_clean_architecture.a_domain.Return;

import java.util.List;

public interface ReturnRepository {
  List<Return> findReturnsByCustomerId(Long customerId);
}
