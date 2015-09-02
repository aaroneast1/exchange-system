package uk.co.exware.service;

import org.junit.Before;
import org.junit.Test;
import uk.co.exware.BigDecimalUtil;
import uk.co.exware.calculator.AverageExecutionPriceCalculator;
import uk.co.exware.calculator.ExecutedQuantityCalculator;
import uk.co.exware.calculator.OpenInterestCalculator;
import uk.co.exware.model.*;
import uk.co.exware.repository.ExecutedOrderRepository;
import uk.co.exware.repository.OpenOrderRepository;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static uk.co.exware.BigDecimalUtil.newBigDecimal;
import static uk.co.exware.model.Direction.BUY;
import static uk.co.exware.model.Direction.SELL;

public class ExchangeSystemTest {

    ExchangeSystem underTest;
    OrderMatcher orderMatcher = mock(OrderMatcher.class);
    OpenOrderRepository openOrderRepository = mock(OpenOrderRepository.class);
    ExecutedOrderRepository executedOrderRepository = mock(ExecutedOrderRepository.class);
    AverageExecutionPriceCalculator averageExecutionPriceCalculator = mock(AverageExecutionPriceCalculator.class);
    ExecutedQuantityCalculator executedQuantityCalculator = mock(ExecutedQuantityCalculator.class);
    OpenInterestCalculator openInterestCalculator = mock(OpenInterestCalculator.class);

    @Before
    public void setUp(){
        underTest = new ExchangeSystem(
                orderMatcher,
                openOrderRepository,
                executedOrderRepository,
                averageExecutionPriceCalculator,
                executedQuantityCalculator,
                openInterestCalculator);
    }

    @Test
    public void shouldAddBuyOrderToOpenOrdersRepositoryWhenNotMatched(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        List<Order> openOrders = new LinkedList<>();

        when(openOrderRepository.getAll()).thenReturn(openOrders);
        when(orderMatcher.match(buyOrder, openOrders)).thenReturn(Optional.empty());

        underTest.addOrder(buyOrder);

        verify(openOrderRepository).add(buyOrder);
    }

    @Test
    public void shouldAddBuyOrderToExecutedOrdersRepositoryWhenMatched(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        List<Order> openOrders = asList(sellOrder);

        when(openOrderRepository.getAll()).thenReturn(openOrders);
        when(openOrderRepository.remove(sellOrder)).thenReturn(true);

        when(orderMatcher.match(buyOrder, openOrders)).thenReturn(Optional.of(sellOrder));

        underTest.addOrder(buyOrder);

        verify(openOrderRepository).remove(sellOrder);
        verify(executedOrderRepository).add(any(ExecutedOrder.class));
    }

    @Test
    public void shouldRetryAddOrderIfMatchedOrderIsNoLongerPresentInOpenOrderRepository(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        final List<Order> openOrders = asList(sellOrder);
        final LinkedList<Order> emptyList = new LinkedList<Order>();

        when(openOrderRepository.getAll()).thenReturn(openOrders, emptyList);
        when(openOrderRepository.remove(sellOrder)).thenReturn(false);

        when(orderMatcher.match(buyOrder, openOrders)).thenReturn(Optional.of(sellOrder));
        when(orderMatcher.match(buyOrder, emptyList)).thenReturn(Optional.empty());

        underTest.addOrder(buyOrder);

        verify(openOrderRepository).remove(sellOrder);
        verify(openOrderRepository).add(buyOrder);
    }


}
