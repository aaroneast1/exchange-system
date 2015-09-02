package uk.co.exware.service;

import org.junit.Test;
import uk.co.exware.calculator.AverageExecutionPriceCalculator;
import uk.co.exware.calculator.ExecutedQuantityCalculator;
import uk.co.exware.calculator.OpenInterestCalculator;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;
import uk.co.exware.model.Order;
import uk.co.exware.model.User;
import uk.co.exware.repository.InMemoryExecutedOrderRepository;
import uk.co.exware.repository.InMemoryOpenOrderRepository;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.exware.model.Direction.BUY;
import static uk.co.exware.model.Direction.SELL;

public class ExchangeSystemIT {

    final ExchangeSystem exchangeSystem = new ExchangeSystem(
            new OrderMatcher(),
            new InMemoryOpenOrderRepository(),
            new InMemoryExecutedOrderRepository(),
            new AverageExecutionPriceCalculator(),
            new ExecutedQuantityCalculator(),
            new OpenInterestCalculator());

    @Test
    public void shouldSuccessfullyExecuteSequenceOfOrders() {

        final Instrument vodInstrument = new Instrument("VOD.L");

        // New Order: SELL 1000 VOD.L @ 100.2 User1
        exchangeSystem.addOrder(new Order(SELL, vodInstrument, 1000L, new Money("100.2"), new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateOpenInterest(vodInstrument, SELL).get(new Money("100.2")).longValue());
        assertEquals(0L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));

        // New Order: BUY 1000 VOD.L @ 100.2 User2 // matches existing buy order, executed @ 100.2
        exchangeSystem.addOrder(new Order(BUY, vodInstrument, 1000L, new Money("100.2"), new User("User2")));
        assertEquals(new Money("100.2000"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(-1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));

        // New Order: BUY 1000 VOD.L @ 99 User1
        exchangeSystem.addOrder(new Order(BUY, vodInstrument, 1000L, new Money("99"), new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateOpenInterest(vodInstrument, BUY).get(new Money("99")).longValue());
        assertEquals(new Money("100.2000"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(-1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));

        // New Order: BUY 1000 VOD.L @ 101 User1
        exchangeSystem.addOrder(new Order(BUY, vodInstrument, 1000L, new Money("101"), new User("User1")));
        Map<Money,Long> openBuyInterest = exchangeSystem.calculateOpenInterest(vodInstrument, BUY);
        assertEquals(1000L, openBuyInterest.get(new Money("99")).longValue());
        assertEquals(1000L, openBuyInterest.get(new Money("101")).longValue());
        assertEquals(new Money("100.2000"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(-1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));

        // New Order: SELL 500 VOD.L @ 102 User2
        exchangeSystem.addOrder(new Order(SELL, vodInstrument, 500L, new Money("102"), new User("User2")));
        openBuyInterest = exchangeSystem.calculateOpenInterest(vodInstrument, BUY);
        assertEquals(1000L, openBuyInterest.get(new Money("99")).longValue());
        assertEquals(1000L, openBuyInterest.get(new Money("101")).longValue());
        assertEquals(500L, exchangeSystem.calculateOpenInterest(vodInstrument, SELL).get(new Money("102")).longValue());
        assertEquals(new Money("100.2000"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(-1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(1000L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));

        // New Order: BUY 500 VOD.L @ 103 User1 // matches existing SELL @ 102, executed @ 103
        exchangeSystem.addOrder(new Order(BUY, vodInstrument, 500L, new Money("103"), new User("User1")));
        openBuyInterest = exchangeSystem.calculateOpenInterest(vodInstrument, BUY);
        assertEquals(1000L, openBuyInterest.get(new Money("99")).longValue());
        assertEquals(1000L, openBuyInterest.get(new Money("101")).longValue());
        assertTrue(exchangeSystem.calculateOpenInterest(vodInstrument, SELL).isEmpty());
        assertEquals(new Money("101.1333"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(-500L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(500L, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));

        // New Order: SELL 1000 VOD.L @ 98 User2 // matches existing BUY @ 101, executed @ 98
        exchangeSystem.addOrder(new Order(SELL, vodInstrument, 1000, new Money("98"), new User("User2")));
        assertEquals(1000, exchangeSystem.calculateOpenInterest(vodInstrument, BUY).get(new Money("99")).longValue());
        assertTrue(exchangeSystem.calculateOpenInterest(vodInstrument, SELL).isEmpty());
        assertEquals(new Money("99.8800"), exchangeSystem.calculateAverageExecutionPrice(vodInstrument));
        assertEquals(500, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User1")));
        assertEquals(-500, exchangeSystem.calculateExecutedQuantity(vodInstrument, new User("User2")));
    }

}
