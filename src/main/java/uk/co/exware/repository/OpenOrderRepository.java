package uk.co.exware.repository;


import uk.co.exware.model.Order;

import java.util.List;

public interface OpenOrderRepository {

    void add(Order order);

    boolean remove(Order order);

    List<Order> getAll();

}
