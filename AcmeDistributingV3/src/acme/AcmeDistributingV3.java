package acme;

import BasicIO.ASCIIOutputFile;

import javax.swing.*;

/**
 * Created by Xuhao Chen on 16/3/23.
 */
public class AcmeDistributingV3{

    public static OrderBuffer orderBuffer = new OrderBuffer();
    public Thread producerThread;
    public Thread customerThread;

    private static ProducerForm pForm;
    private static CustomerForm cForm;

    private Object lock = new Object();

    public static void main(String[] args) {
        new AcmeDistributingV3();
    }
    public AcmeDistributingV3() {
        producerThread = new Thread(new ProducerThread());
        customerThread = new Thread(new CustomerThread());
        producerThread.start();
        customerThread.start();

        try {
            producerThread.join();
            customerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ASCIIOutputFile output;

        while (true){
            try{
                output = new ASCIIOutputFile();
                break;
            }catch(NullPointerException e1){
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure want to close without saving?",
                        "Close without saving",JOptionPane.YES_NO_OPTION);

                if(confirm==0) System.exit(-1);
            }
        }

        while(!orderBuffer.isEmpty()){
            Order order = orderBuffer.removeOrder();
            output.writeString(order.getDateString());
            output.writeString(order.getTimeString());
            output.writeString(order.getOrderNum());
            output.writeString(order.getItemNum());
            output.writeString(order.getQuantityString());
            output.writeString(Order.Type.getAddressString(order));
            output.newLine();

        }

        System.exit(0);

    }

    public class CustomerThread implements Runnable{

        @Override
        public void run() {

            cForm = new CustomerForm(orderBuffer,lock);
            while(cForm.isVisible()){
                synchronized (lock){
                    try{
                        lock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public class ProducerThread implements Runnable{

        @Override
        public void run() {
            pForm = new ProducerForm(orderBuffer,lock);

            while(pForm.isVisible()){
                synchronized (lock){
                    try{
                        lock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}




