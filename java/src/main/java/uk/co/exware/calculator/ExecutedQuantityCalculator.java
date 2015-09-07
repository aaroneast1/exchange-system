package uk.co.exware.calculator;

import uk.co.exware.model.ExecutedOrder;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.User;

import java.util.List;

import static java.util.stream.Collectors.summingLong;

public class ExecutedQuantityCalculator {

    public long calculate(final List<ExecutedOrder> executedOrders, final Instrument instrument, final User user) {
        return executedOrders
                .stream()
                .filter(o -> o.getInstrument().equals(instrument) && (o.getBuyUser().equals(user) || o.getSellUser().equals(user)))
                .map(o -> o.getQuantity() * (o.getSellUser().equals(user) ? -1 : 1))
                .collect(summingLong(l -> l));
    }
}
