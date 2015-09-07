package uk.co.exware.calculator;

import org.junit.Before;
import org.junit.Test;
import uk.co.exware.model.ExecutedOrder;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;
import uk.co.exware.model.User;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AverageExecutionPriceCalculatorTest {

    AverageExecutionPriceCalculator underTest;

    @Before
    public void setUp(){
        underTest = new AverageExecutionPriceCalculator();
    }

    @Test
    public void shouldCalculateAverageExcecutionPrice(){
        Instrument instrument = new Instrument("RIC1");

        ExecutedOrder exOrder = new ExecutedOrder(
                instrument,
                10L,
                new Money("10.00"),
                new User("userId1"),
                new User("userId2"));

        ExecutedOrder exOrder1 = new ExecutedOrder(
                instrument,
                10L,
                new Money("25.00"),
                new User("userId1"),
                new User("userId2"));

        ExecutedOrder exOrder2 = new ExecutedOrder(
                instrument,
                20L,
                new Money("50.00"),
                new User("userId1"),
                new User("userId2"));


        final Money result = underTest.calculate(asList(exOrder, exOrder1, exOrder2), instrument);

        assertEquals(new Money("33.75"), result);

    }

}