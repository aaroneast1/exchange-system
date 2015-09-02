package uk.co.exware.repository;

import uk.co.exware.model.ExecutedOrder;

import java.util.List;

public interface ExecutedOrderRepository {

    void add(ExecutedOrder order);

    List<ExecutedOrder> getAll();
}
