package acme;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Xuhao Chen on 16/4/2.
 */

public class OrderBuffer {

    private Queue<Order> orderQueue;

    public OrderBuffer(){
        orderQueue = new LinkedList<Order>();
    }

    public synchronized void addOrder(Order aOrder){
        orderQueue.offer(aOrder);
        notify();
    }

    public synchronized Order pollOrder(){
        while(orderQueue.size()==0){
            try {
                System.out.println("pollOrder is waiting...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Order result = orderQueue.poll();
        orderQueue.offer(result);
        notify();
        return result;
    }

    public synchronized Order removeOrder(){
        while(orderQueue.size()==0){
            try {
                System.out.println("pollOrder is waiting...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Order result = orderQueue.poll();
        notify();
        return result;
    }

    public synchronized int getNumOrder(){
        return orderQueue.size();
    }

    public boolean isEmpty(){
        if (orderQueue.size()==0) return true;
        return false;
    }

}
