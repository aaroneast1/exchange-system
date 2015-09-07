package uk.co.exware.model;

import java.time.Instant;

public class ExecutedOrder {

    private final Instrument instrument;
    private final long quantity;
    private final Money unitPrice;
    private final User buyUser;
    private final User sellUser;
    private final Instant timestamp = Instant.now();

    public ExecutedOrder(Instrument instrument,
                         long quantity,
                         Money unitPrice,
                         User buyUser,
                         User sellUser) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.buyUser = buyUser;
        this.sellUser = sellUser;
    }

    public Money getTotalPrice() {
        return getUnitPrice().multiply(getQuantity());
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

    public User getBuyUser() {
        return buyUser;
    }

    public User getSellUser() {
        return sellUser;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
