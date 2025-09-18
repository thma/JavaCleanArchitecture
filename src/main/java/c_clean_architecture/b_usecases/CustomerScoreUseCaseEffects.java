package c_clean_architecture.b_usecases;

import c_clean_architecture.a_domain.Customer;
import c_clean_architecture.a_domain.Order;
import c_clean_architecture.a_domain.Return;
import c_clean_architecture.effects.core.Eff;
import c_clean_architecture.effects.definitions.LogEffect;
import c_clean_architecture.effects.definitions.OrderRepositoryEffect;
import c_clean_architecture.effects.definitions.ReturnRepositoryEffect;

import java.util.List;

/**
 * Customer score use case implemented using algebraic effects.
 * This version separates effect declaration from interpretation,
 * making the code more testable and composable.
 */
public class CustomerScoreUseCaseEffects {

    /**
     * Calculate the score for a customer using algebraic effects.
     * The computation is pure and effects are handled by the runtime.
     *
     * @param customerId the customer ID
     * @return an Eff that will produce the score when executed
     */
    public Eff<Integer> calculateScore(Long customerId) {
        // Log the start of calculation
        return log(new LogEffect.Info("Calculating score for customer " + customerId))
            .flatMap(ignored ->
                // Fetch orders and returns in parallel
                Eff.parallel(
                    getOrders(customerId),
                    getReturns(customerId)
                ).flatMap(pair -> {
                    List<Order> orders = pair.getFirst();
                    List<Return> returns = pair.getSecond();

                    // Pure domain logic
                    Customer customer = new Customer(customerId);
                    int score = customer.calculateScore(orders, returns);

                    // Log the result
                    return log(new LogEffect.Info("Customer " + customerId + " has score " + score))
                        .map(v -> score);
                })
            );
    }

    /**
     * Alternative implementation with sequential fetching.
     */
    public Eff<Integer> calculateScoreSequential(Long customerId) {
        return for_(
            log(new LogEffect.Info("Calculating score for customer " + customerId)),
            getOrders(customerId),
            getReturns(customerId),
            (ignored, orders, returns) -> {
                Customer customer = new Customer(customerId);
                int score = customer.calculateScore(orders, returns);
                return log(new LogEffect.Info("Customer " + customerId + " has score " + score))
                    .map(v -> score);
            }
        );
    }

    /**
     * Calculate score with error handling.
     */
    public Eff<Integer> calculateScoreWithRecovery(Long customerId) {
        return calculateScore(customerId)
            .recover(error -> {
                // Log error and return default score
                log(new LogEffect.Error("Failed to calculate score for customer " + customerId, error));
                return 0; // Default score on error
            });
    }

    // Helper methods for creating effects

    private Eff<Void> log(LogEffect effect) {
        return Eff.perform(effect);
    }

    private Eff<List<Order>> getOrders(Long customerId) {
        return Eff.perform(new OrderRepositoryEffect.FindByCustomerId(customerId));
    }

    private Eff<List<Return>> getReturns(Long customerId) {
        return Eff.perform(new ReturnRepositoryEffect.FindByCustomerId(customerId));
    }

    // Monadic for-comprehension helper
    private <A, B, C, D> Eff<D> for_(
        Eff<A> effA,
        Eff<B> effB,
        Eff<C> effC,
        TriFunction<A, B, C, Eff<D>> f
    ) {
        return effA.flatMap(a ->
            effB.flatMap(b ->
                effC.flatMap(c ->
                    f.apply(a, b, c)
                )
            )
        );
    }

    @FunctionalInterface
    private interface TriFunction<A, B, C, D> {
        D apply(A a, B b, C c);
    }
}