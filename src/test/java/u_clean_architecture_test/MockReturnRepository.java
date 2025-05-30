package u_clean_architecture_test;

import u_clean_architecture.a_domain.Return;
import u_clean_architecture.c_ports.ReturnRepository;

import java.util.List;

public class MockReturnRepository implements ReturnRepository {
    private final List<Return> returns;

    public MockReturnRepository(List<Return> returns) {
        this.returns = returns;
    }

    @Override
    public java.util.List<Return> findReturnsByCustomerId(Long customerId) {
        return returns;
    }

}
