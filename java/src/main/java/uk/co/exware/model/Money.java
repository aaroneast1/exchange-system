package uk.co.exware.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    public static final Money ZERO = new Money("0");

    private BigDecimal amount;

    public Money(final String amount) {
        this.amount = new BigDecimal(amount).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public Money(final BigDecimal amount) {
        this.amount = amount.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public Money multiply(long quantity) {
        return new Money(new BigDecimal(quantity).multiply(this.amount));
    }

    public int compareTo(final Money money) {
        return this.amount.compareTo(money.amount);
    }

    public Money add(final Money money) {
        return new Money(money.amount.add(amount));
    }

    public Money subtract(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money divide(final Long quantity) {
        return new Money(amount.divide(new BigDecimal(quantity), RoundingMode.HALF_UP));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return !(amount != null ? !amount.equals(money.amount) : money.amount != null);

    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }

}
