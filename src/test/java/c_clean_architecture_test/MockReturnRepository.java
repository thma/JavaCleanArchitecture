package c_clean_architecture_test;

import c_clean_architecture.a_domain.Return;
import c_clean_architecture.b_usecases.ReturnRepository;

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
