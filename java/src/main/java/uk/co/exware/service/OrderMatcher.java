package uk.co.exware.service;

import uk.co.exware.model.Money;
import uk.co.exware.model.Order;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderMatcher {

    private Comparator<Order> byEarliest = (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp());

    private Money getHighestPrice(final List<Order> orders){
        return orders.stream().map(Order::getUnitPrice).max(Money::compareTo).get();
    }

    private Money getLowestPrice(final List<Order> orders){
        return orders.stream().map(Order::getUnitPrice).min(Money::compareTo).get();
    }

    private boolean isPriceMatch(Order newOrder, Order existingOrder){
        return (newOrder.isBuy() && newOrder.getUnitPrice().compareTo(existingOrder.getUnitPrice()) >= 0)
                || (newOrder.isSell() && newOrder.getUnitPrice().compareTo(existingOrder.getUnitPrice()) <=  0);
    }

    public Optional<Order> match(Order newOrder, List<Order> openOrders) {
        final List<Order> matches = openOrders.stream()
                .filter(o -> !newOrder.getDirection().equals(o.getDirection())
                                && newOrder.getInstrument().equals(o.getInstrument())
                                && newOrder.getQuantity() == o.getQuantity()
                                && isPriceMatch(newOrder, o)
                ).collect(Collectors.toList());

        if(matches.size() > 0) {
            if (matches.size() == 1) {
                return Optional.of(matches.get(0));
            } else if (newOrder.isSell()) {
                final Money highestPrice = getHighestPrice(matches);
                return matches.stream()
                        .filter(o -> o.getUnitPrice().equals(highestPrice))
                        .sorted(byEarliest).findFirst();
            } else if (newOrder.isBuy()) {
                final Money highestPrice = getLowestPrice(matches);
                return matches.stream()
                        .filter(o -> o.getUnitPrice().equals(highestPrice))
                        .sorted(byEarliest).findFirst();
            }
        }

        return Optional.empty();

    }
}

