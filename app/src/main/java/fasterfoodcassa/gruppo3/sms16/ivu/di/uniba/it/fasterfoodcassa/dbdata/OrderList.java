package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata;

import java.util.ArrayList;
import java.util.List;

public class OrderList {
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        orders.add(order);
    }

    public List<Order> getOrders(){
        return orders;
    }
}
