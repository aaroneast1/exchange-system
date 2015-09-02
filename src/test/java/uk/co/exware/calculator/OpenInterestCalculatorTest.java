package uk.co.exware.calculator;

import org.junit.Before;
import org.junit.Test;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;
import uk.co.exware.model.Order;
import uk.co.exware.model.User;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.exware.model.Direction.BUY;

public class OpenInterestCalculatorTest {

    OpenInterestCalculator underTest;

    @Before
    public void setUp(){
        underTest = new OpenInterestCalculator();
    }

    @Test
    public void shouldSuccessfullyCalculateOpenInterest(){
        Order buyOrder = new Order(BUY,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId1"));
        Order buyOrder1 = new Order(BUY,
                new Instrument("RIC1"),
                2000,
                new Money("11.00"),
                new User("userId2"));
        Order buyOrder2 = new Order(BUY,
                new Instrument("RIC1"),
                1000,
                new Money("10.00"),
                new User("userId1"));

        final Map<Money, Long> result = underTest.calculate(asList(buyOrder, buyOrder1, buyOrder2), new Instrument("RIC1"), BUY);

        assertTrue(result.containsKey(new Money("10.00")));
        assertEquals(2000L, result.get(new Money("10.00")).longValue());
        assertTrue(result.containsKey(new Money("11.00")));
        assertEquals(2000L, result.get(new Money("11.00")).longValue());
    }

}