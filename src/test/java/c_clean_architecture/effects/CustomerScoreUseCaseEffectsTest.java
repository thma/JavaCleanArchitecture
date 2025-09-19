package c_clean_architecture.effects;

import c_clean_architecture.a_domain.Order;
import c_clean_architecture.a_domain.Return;
import c_clean_architecture.b_usecases.CustomerScoreUseCaseEffects;
import c_clean_architecture.effects.core.Effect;
import c_clean_architecture.effects.core.Eff;
import c_clean_architecture.effects.core.EffectRuntime;
import c_clean_architecture.effects.definitions.LogEffect;
import c_clean_architecture.effects.definitions.OrderRepositoryEffect;
import c_clean_architecture.effects.definitions.ReturnRepositoryEffect;
import c_clean_architecture.effects.handlers.CollectingLogHandler;
import c_clean_architecture.effects.handlers.InMemoryOrderRepositoryHandler;
import c_clean_architecture.effects.handlers.InMemoryReturnRepositoryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the effect-based CustomerScoreUseCase.
 * These tests demonstrate how to test effect-based code using mock handlers.
 */
public class CustomerScoreUseCaseEffectsTest {

    private CustomerScoreUseCaseEffects useCase;
    private InMemoryOrderRepositoryHandler orderHandler;
    private InMemoryReturnRepositoryHandler returnHandler;
    private CollectingLogHandler logHandler;
    private EffectRuntime runtime;

    @BeforeEach
    void setUp() {
        useCase = new CustomerScoreUseCaseEffects();
        orderHandler = new InMemoryOrderRepositoryHandler();
        returnHandler = new InMemoryReturnRepositoryHandler();
        logHandler = new CollectingLogHandler();

        // Build runtime with test handlers
        runtime = EffectRuntime.builder()
            .withHandlerUnsafe(LogEffect.class, logHandler)
            .withHandlerUnsafe(OrderRepositoryEffect.FindByCustomerId.class, orderHandler)
            .withHandlerUnsafe(ReturnRepositoryEffect.FindByCustomerId.class, returnHandler)
            .build();
    }

    @Test
    void testCalculateScore_WithOrdersAndReturns() {
        // Given
        Long customerId = 123L;

        List<Order> orders = Arrays.asList(
            new Order(1L, new BigDecimal("100.00")),
            new Order(2L, new BigDecimal("200.00")),
            new Order(3L, new BigDecimal("300.00"))
        );

        List<Return> returns = Arrays.asList(
            new Return(1L, 1L, "Damaged", LocalDate.now(), new BigDecimal("50.00")),
            new Return(2L, 2L, "Wrong item", LocalDate.now(), new BigDecimal("100.00"))
        );

        orderHandler.addOrdersForCustomer(customerId, orders);
        returnHandler.addReturnsForCustomer(customerId, returns);

        // When
        Eff<Integer> scoreEffect = useCase.calculateScore(customerId);
        Integer score = scoreEffect.runWith(runtime);

        // Then
        // Total orders = 600, Total returns = 150
        // Score = (600 - 150) / 600 * 100 = 75
        assertEquals(75, score);

        // Verify logging
        assertTrue(logHandler.containsMessagePart("Calculating score for customer 123"));
        assertTrue(logHandler.containsMessagePart("Customer 123 has score 75"));
        assertEquals(2, logHandler.getCountByLevel(CollectingLogHandler.LogLevel.INFO));
    }

    @Test
    void testCalculateScore_WithNoReturns() {
        // Given
        Long customerId = 456L;

        List<Order> orders = Arrays.asList(
            new Order(1L, new BigDecimal("100.00")),
            new Order(2L, new BigDecimal("200.00"))
        );

        orderHandler.addOrdersForCustomer(customerId, orders);
        returnHandler.addReturnsForCustomer(customerId, Arrays.asList());

        // When
        Eff<Integer> scoreEffect = useCase.calculateScore(customerId);
        Integer score = scoreEffect.runWith(runtime);

        // Then
        // No returns = perfect score
        assertEquals(100, score);

        // Verify logging
        assertTrue(logHandler.containsMessage(
            CollectingLogHandler.LogLevel.INFO,
            "Customer 456 has score 100"
        ));
    }

    @Test
    void testCalculateScore_WithNoOrders() {
        // Given
        Long customerId = 789L;

        orderHandler.addOrdersForCustomer(customerId, Arrays.asList());
        returnHandler.addReturnsForCustomer(customerId, Arrays.asList());

        // When
        Eff<Integer> scoreEffect = useCase.calculateScore(customerId);
        Integer score = scoreEffect.runWith(runtime);

        // Then
        // No orders = perfect score (as per business logic)
        assertEquals(100, score);
    }

    @Test
    void testEffectStructure_WithoutExecution() {
        // Given
        Long customerId = 999L;

        // When - Create effect without running it
        Eff<Integer> scoreEffect = useCase.calculateScore(customerId);

        // Then - Verify effect structure
        List<Effect<?>> effects = scoreEffect.collectEffects();

        // With FlatMap-based effects, we can only collect the first effect
        // before execution, since subsequent effects are generated dynamically
        // based on the results of previous effects
        assertTrue(effects.size() >= 1, "Should have at least the initial effect");

        // First effect should be info log
        assertInstanceOf(LogEffect.Info.class, effects.get(0));
        LogEffect.Info firstLog = (LogEffect.Info) effects.get(0);
        assertTrue(firstLog.message().contains("Calculating score"));

        // We can't verify the repository effects without execution,
        // as they're generated inside the flatMap continuation
    }

    @Test
    void testCalculateScoreSequential() {
        // Given
        Long customerId = 123L;

        List<Order> orders = Arrays.asList(
            new Order(1L, new BigDecimal("500.00"))
        );

        List<Return> returns = Arrays.asList(
            new Return(1L, 1L, "Defective", LocalDate.now(), new BigDecimal("100.00"))
        );

        orderHandler.addOrdersForCustomer(customerId, orders);
        returnHandler.addReturnsForCustomer(customerId, returns);

        // When
        Eff<Integer> scoreEffect = useCase.calculateScoreSequential(customerId);
        Integer score = scoreEffect.runWith(runtime);

        // Then
        // Score = (500 - 100) / 500 * 100 = 80
        assertEquals(80, score);
    }

    @Test
    void testCalculateScoreWithRecovery_Success() {
        // Given
        Long customerId = 123L;

        List<Order> orders = Arrays.asList(
            new Order(1L, new BigDecimal("100.00"))
        );

        orderHandler.addOrdersForCustomer(customerId, orders);
        returnHandler.addReturnsForCustomer(customerId, Arrays.asList());

        // When
        Eff<Integer> scoreEffect = useCase.calculateScoreWithRecovery(customerId);
        Integer score = scoreEffect.runWith(runtime);

        // Then
        assertEquals(100, score);

        // Should not have error logs
        assertEquals(0, logHandler.getCountByLevel(CollectingLogHandler.LogLevel.ERROR));
    }

    @Test
    void testMultipleCustomers() {
        // Given - Setup data for multiple customers
        Long customer1 = 1L;
        Long customer2 = 2L;

        orderHandler.addOrdersForCustomer(customer1, Arrays.asList(
            new Order(1L, new BigDecimal("100.00"))
        ));
        returnHandler.addReturnsForCustomer(customer1, Arrays.asList());

        orderHandler.addOrdersForCustomer(customer2, Arrays.asList(
            new Order(2L, new BigDecimal("200.00"))
        ));
        returnHandler.addReturnsForCustomer(customer2, Arrays.asList(
            new Return(1L, 2L, "Wrong size", LocalDate.now(), new BigDecimal("50.00"))
        ));

        // When
        Integer score1 = useCase.calculateScore(customer1).runWith(runtime);
        Integer score2 = useCase.calculateScore(customer2).runWith(runtime);

        // Then
        assertEquals(100, score1); // No returns
        assertEquals(75, score2);  // (200 - 50) / 200 * 100 = 75

        // Verify separate log entries for each customer
        assertTrue(logHandler.containsMessagePart("Customer 1 has score 100"));
        assertTrue(logHandler.containsMessagePart("Customer 2 has score 75"));
    }
}