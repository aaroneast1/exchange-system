package uk.co.exware.model;

import org.junit.Test;
import uk.co.exware.BigDecimalUtil;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.exware.BigDecimalUtil.newBigDecimal;
import static uk.co.exware.model.Direction.BUY;


public class OrderTest {

    @Test
    public void shouldSuccessfullyCreateBuyOrder(){

        Instrument instrument = new Instrument("RIC1");
        long quantity = 10000;
        Money unitPrice = new Money("10.00");
        User user = new User("userId");

        Order order = new Order(BUY, instrument, quantity, unitPrice, user);

        assertTrue("Failed to create a buy order.", order.isBuy());
        assertEquals(Direction.BUY, order.getDirection());
        assertEquals(instrument, order.getInstrument());
        assertEquals(quantity, order.getQuantity());
        assertEquals(unitPrice, order.getUnitPrice());
        assertEquals(user, order.getUser());

    }

}
