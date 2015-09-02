package uk.co.exware.repository;

import uk.co.exware.model.Order;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryOpenOrderRepository implements OpenOrderRepository{

    private List<Order> orders = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void add(Order order) {
        orders.add(order);
    }

    @Override
    public boolean remove(Order order) {
        return orders.remove(order);
    }

    @Override
    public List<Order> getAll() {
        return new LinkedList<>(orders);
    }


}
