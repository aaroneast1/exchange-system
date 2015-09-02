package uk.co.exware.service;


import org.junit.Before;
import org.junit.Test;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;
import uk.co.exware.model.Order;
import uk.co.exware.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static uk.co.exware.model.Direction.BUY;
import static uk.co.exware.model.Direction.SELL;

public class OrderMatcherTest {

    OrderMatcher underTest;

    @Before
    public void setUp(){
        underTest = new OrderMatcher();
    }

    @Test
    public void shouldMatchUniqueBuySellOrders(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order matchingBuyOrder = new Order(BUY,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        List<Order> orders = asList(buyOrder, sellOrder);

        final Optional<Order> matchedOrder = underTest.match(matchingBuyOrder, orders);


        assertTrue("Could not find matching order", matchedOrder.isPresent());
        assertNotSame("Do not have opposing directions", matchingBuyOrder.isBuy(), matchedOrder.get().isBuy());
        assertEquals("Do not have matching RICs", matchingBuyOrder.getInstrument(), matchedOrder.get().getInstrument());
        assertEquals("Do not have matching quantities", matchingBuyOrder.getQuantity(), matchedOrder.get().getQuantity());
        assertTrue("The sell price is not less than or equal to buy price.", matchingBuyOrder.getUnitPrice().subtract(matchedOrder.get().getUnitPrice()).compareTo(Money.ZERO) >= 0);
        assertEquals("The matching sell order is incorrect.", sellOrder, matchedOrder.get());

    }

    @Test
    public void shouldMatchTheEarliestMatchedOrder(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        Order sellOrder1 = new Order(SELL,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId3"));

        Order matchingBuyOrder = new Order(BUY,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        List<Order> orders = asList(buyOrder, sellOrder, sellOrder1);

        final Optional<Order> matchedOrder = underTest.match(matchingBuyOrder, orders);


        assertTrue("Could not find matching order", matchedOrder.isPresent());
        assertNotSame("Do not have opposing directions", matchingBuyOrder.isBuy(), matchedOrder.get().isBuy());
        assertEquals("Do not have matching RICs", matchingBuyOrder.getInstrument(), matchedOrder.get().getInstrument());
        assertEquals("Do not have matching quantities", matchingBuyOrder.getQuantity(), matchedOrder.get().getQuantity());
        assertTrue("The sell price is not less than or equal to buy price.", matchingBuyOrder.getUnitPrice().subtract(matchedOrder.get().getUnitPrice()).compareTo(Money.ZERO) >= 0);
        assertEquals("The matching sell order is incorrect.", sellOrder, matchedOrder.get());
    }

    @Test
    public void shouldMatchTheHighestBuyPrice(){

        Order highestPriceBuyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("11.00"),
                new User("userId1"));

        Order buyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("9.00"),
                new User("userId1"));

        Order buyOrder1 = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        Order matchingSellOrder = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId3"));

        List<Order> orders = asList(buyOrder, highestPriceBuyOrder, buyOrder1, sellOrder);

        final Optional<Order> matchedOrder = underTest.match(matchingSellOrder, orders);

        assertTrue("Could not find matching order", matchedOrder.isPresent());
        assertNotSame("Do not have opposing directions", matchingSellOrder.getDirection(), matchedOrder.get().getDirection());
        assertEquals("Do not have matching RICs", matchingSellOrder.getInstrument(), matchedOrder.get().getInstrument());
        assertEquals("Do not have matching quantities", matchingSellOrder.getQuantity(), matchedOrder.get().getQuantity());
        assertTrue("The sell price is not less than or equal to buy price.", matchedOrder.get().getUnitPrice().subtract(matchingSellOrder.getUnitPrice()).compareTo(Money.ZERO) >= 0);
        assertEquals("Does not match the highest buy order", highestPriceBuyOrder, matchedOrder.get());

    }


    @Test
    public void shouldMatchTheLowestSellPrice(){

        Order lowestPriceSellOrder = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("9.00"),
                new User("userId1"));

        Order sellOrder = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("11.00"),
                new User("userId1"));

        Order sellOrder1 = new Order(SELL,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        Order buyOrder = new Order(BUY,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId2"));

        Order matchingBuyOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId3"));

        List<Order> orders = asList(sellOrder, lowestPriceSellOrder, sellOrder1, buyOrder);

        final Optional<Order> matchedOrder = underTest.match(matchingBuyOrder, orders);

        assertTrue("Could not find matching order", matchedOrder.isPresent());
        assertNotSame("Do not have opposing directions", matchingBuyOrder.getDirection(), matchedOrder.get().getDirection());
        assertEquals("Do not have matching RICs", matchingBuyOrder.getInstrument(), matchedOrder.get().getInstrument());
        assertEquals("Do not have matching quantities", matchingBuyOrder.getQuantity(), matchedOrder.get().getQuantity());
        assertTrue("The sell price is not less than or equal to buy price.", matchingBuyOrder.getUnitPrice().subtract(matchedOrder.get().getUnitPrice()).compareTo(Money.ZERO) >= 0);
        assertEquals("Does not match the lowest sell order", lowestPriceSellOrder, matchedOrder.get());

    }

    @Test
    public void shouldReturnEmptyOptionalIfNoOpenOrders(){
        final LinkedList<Order> emptyList = new LinkedList();
        Order newOrder = new Order(BUY,
                new Instrument("RIC2"),
                1000,
                new Money("10.00"),
                new User("userId3"));

        final Optional<Order> matchedOrder = underTest.match(newOrder, emptyList);

        assertFalse(matchedOrder.isPresent());
    }

}