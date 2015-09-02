package uk.co.exware.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.exware.BigDecimalUtil;

import java.math.BigDecimal;
import java.time.Instant;

import static uk.co.exware.BigDecimalUtil.addScale;

public class Order {

    private final Direction direction;
    private final Instrument instrument;
    private final long quantity;
    private final Money unitPrice;
    private final User user;
    private final Instant timestamp = Instant.now();

    public Order(Direction direction,
                 Instrument instrument,
                 long quantity,
                 Money unitPrice,
                 User user) {
        this.direction = direction;
        this.instrument = instrument;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.user = user;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isBuy(){
        return Direction.BUY.equals(direction);
    }

    public boolean isSell(){
        return !isBuy();
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public long getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public User getUser() {
        return user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
       return new HashCodeBuilder()
               .append(direction)
               .append(instrument)
               .append(quantity)
               .append(unitPrice)
               .append(user)
               .append(timestamp)
               .toHashCode();
    }
}
