package uk.co.exware.repository;

import uk.co.exware.model.ExecutedOrder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryExecutedOrderRepository implements ExecutedOrderRepository {

    private List<ExecutedOrder> orders = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void add(ExecutedOrder order) {
        orders.add(order);
    }

    @Override
    public List<ExecutedOrder> getAll() {
        return new LinkedList<>(orders);
    }
}
