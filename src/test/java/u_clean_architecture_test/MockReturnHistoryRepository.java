package u_clean_architecture;

import u_clean_architecture.a_domain.Return;
import u_clean_architecture.b_usecases.ReturnHistoryRepository;

import java.util.List;

public class MockReturnHistoryRepository implements ReturnHistoryRepository {
    private final List<Return> returns;

    public MockReturnHistoryRepository(List<Return> returns) {
        this.returns = returns;
    }

    @Override
    public java.util.List<Return> findReturnsByCustomerId(Long customerId) {
        return returns;
    }

}
