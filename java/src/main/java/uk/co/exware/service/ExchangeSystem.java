package uk.co.exware.service;

import uk.co.exware.calculator.AverageExecutionPriceCalculator;
import uk.co.exware.calculator.ExecutedQuantityCalculator;
import uk.co.exware.calculator.OpenInterestCalculator;
import uk.co.exware.model.*;
import uk.co.exware.repository.ExecutedOrderRepository;
import uk.co.exware.repository.OpenOrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExchangeSystem {

    private final OrderMatcher orderMatcher;
    private final OpenOrderRepository openOrderRepository;
    private final ExecutedOrderRepository executedOrderRepository;
    private final AverageExecutionPriceCalculator averageExecutionPriceCalculator;
    private final ExecutedQuantityCalculator executedQuantityCalculator;
    private final OpenInterestCalculator openInterestCalculator;

    public ExchangeSystem(OrderMatcher orderMatcher,
                          OpenOrderRepository openOrderRepository,
                          ExecutedOrderRepository executedOrderRepository, AverageExecutionPriceCalculator averageExecutionPriceCalculator, ExecutedQuantityCalculator executedQuantityCalculator, OpenInterestCalculator openInterestCalculator) {
        this.orderMatcher = orderMatcher;
        this.openOrderRepository = openOrderRepository;
        this.executedOrderRepository = executedOrderRepository;
        this.averageExecutionPriceCalculator = averageExecutionPriceCalculator;
        this.executedQuantityCalculator = executedQuantityCalculator;
        this.openInterestCalculator = openInterestCalculator;
    }

    public void addOrder(Order order){
        final List<Order> openOrders = openOrderRepository.getAll();
        final Optional<Order> match = orderMatcher.match(order, openOrders);

        if(!match.isPresent()){
            openOrderRepository.add(order);
        }else{
            final Order existingOrder = match.get();
            final boolean remove = openOrderRepository.remove(existingOrder);

            if(remove){
                executedOrderRepository.add(create(order, existingOrder));
            }else{
                this.addOrder(order);
            }
        }
    }

    private ExecutedOrder create(Order newOrder, Order existingOrder){
        Money bestUnitPrice = getBestPrice(newOrder, existingOrder);

        if(newOrder.isBuy()){
            return new ExecutedOrder(newOrder.getInstrument(),
                    newOrder.getQuantity(),
                    bestUnitPrice,
                    newOrder.getUser(),
                    existingOrder.getUser());
        }else{
            return new ExecutedOrder(newOrder.getInstrument(),
                    newOrder.getQuantity(),
                    bestUnitPrice,
                    existingOrder.getUser(),
                    newOrder.getUser());
        }
    }

    private Money getBestPrice(Order newOrder, Order existingOrder){
        if(newOrder.isSell() && newOrder.getUnitPrice().compareTo(existingOrder.getUnitPrice()) > 0){
            return existingOrder.getUnitPrice();
        }else if(newOrder.isBuy() && newOrder.getUnitPrice().compareTo(existingOrder.getUnitPrice()) < 0){
            return existingOrder.getUnitPrice();
        }else{
            return newOrder.getUnitPrice();
        }
    }

    public Map<Money,Long> calculateOpenInterest(Instrument instrument, Direction direction) {
        return openInterestCalculator.calculate(openOrderRepository.getAll(),instrument, direction);
    }

    public long calculateExecutedQuantity(Instrument instrument, User user) {
        return executedQuantityCalculator.calculate(executedOrderRepository.getAll(), instrument, user);
    }


    public Money calculateAverageExecutionPrice(Instrument instrument) {
        return averageExecutionPriceCalculator.calculate(executedOrderRepository.getAll(),instrument);
    }
}
