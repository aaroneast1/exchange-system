package uk.co.exware.calculator;

import uk.co.exware.model.ExecutedOrder;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;

import java.util.List;

import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toList;

public class AverageExecutionPriceCalculator {

    public Money calculate(final List<ExecutedOrder> executedOrders, final Instrument instrument) {

        final List<ExecutedOrder> filteredByInstrument = executedOrders.stream()
                .filter(order -> order.getInstrument().equals(instrument))
                .collect(toList());

        final Money totalPrice = filteredByInstrument.stream()
                .map(ExecutedOrder::getTotalPrice)
                .reduce(Money.ZERO, Money::add);

        final Long totalQuantity = filteredByInstrument.stream()
                .collect(summingLong(ExecutedOrder::getQuantity));

        return totalPrice.divide(totalQuantity);
    }

}
