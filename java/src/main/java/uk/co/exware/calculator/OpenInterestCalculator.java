package uk.co.exware.calculator;

import uk.co.exware.model.Direction;
import uk.co.exware.model.Instrument;
import uk.co.exware.model.Money;
import uk.co.exware.model.Order;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

public class OpenInterestCalculator {

    public Map<Money, Long> calculate(List<Order> openOrders, Instrument instrument, Direction direction){
        return openOrders
                .stream()
                .filter(order -> order.getDirection().equals(direction) && order.getInstrument().equals(instrument))
                .collect(groupingBy(Order::getUnitPrice, summingLong(Order::getQuantity)));
    }


}
