package u_clean_architecture.b_usecases;

import u_clean_architecture.a_domain.Return;

import java.util.List;

public interface ReturnRepository {
    List<Return> findReturnsByCustomerId(Long customerId);
}
