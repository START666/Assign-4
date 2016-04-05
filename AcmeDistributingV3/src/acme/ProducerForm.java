package acme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;


/**
 * Created by Xuhao Chen on 16/4/2.
 */
public class ProducerForm extends JFrame{

    private JPanel producer_panel_checked, producer_panel_unchecked, producer_panel_buttons;
    private JLabel producer_label_date, producer_label_time, producer_label_orderNum,
            producer_label_itemNum, producer_label_quantity, producer_label_address;
    private JTextField producer_field_date, producer_field_time,producer_field_orderNum,
            producer_field_itemNum, producer_field_quantity;
    private JTextArea producer_area_address;
    private JButton producer_button_ok, producer_button_quit;


    private boolean isQuit = false ;
    private Object lock;

    private boolean allValid = true;

//    private Queue<Order> orderQueue;
    private OrderBuffer orderBuffer;

    public ProducerForm(OrderBuffer orderBuffer, final Object lock){
        super("Acme Distributing V3");
        this.orderBuffer = orderBuffer;
        this.lock = lock;
        setLayout(new BorderLayout());

        initPanels();

        addComponentsToCheckedPanel();
        addComponentsToUncheckedPanel();
        addComponentsToButtonsPanel();

        add("North", producer_panel_checked);
        add("Center", producer_panel_unchecked);
        add("South", producer_panel_buttons);

        addToListener();

        pack();
        setPreferredSize(new Dimension(760,getHeight()));   //height: auto, width: 860
        pack();
        setVisible(true);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (lock){
                    setVisible(false);
                    lock.notifyAll();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

                synchronized (lock){
                    setVisible(false);
                    lock.notifyAll();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

    }

    private void initPanels(){
        producer_panel_checked = new JPanel();
        producer_panel_unchecked = new JPanel();
        producer_panel_buttons = new JPanel();

        producer_panel_checked.setLayout(new GridLayout(0,4));
        producer_panel_unchecked.setLayout(new FlowLayout(FlowLayout.LEFT));
        producer_panel_buttons.setLayout(new FlowLayout());
    }

    private void addComponentsToCheckedPanel(){

        producer_label_date = new JLabel("Date");
        producer_label_time = new JLabel("Time");
        producer_label_orderNum = new JLabel("Order #");
        producer_label_itemNum = new JLabel("Item #");
        producer_label_quantity = new JLabel("Quantity");

        producer_label_date.setForeground(Color.RED);
        producer_label_time.setForeground(Color.RED);
        producer_label_orderNum.setForeground(Color.RED);
        producer_label_itemNum.setForeground(Color.RED);
        producer_label_quantity.setForeground(Color.RED);

        producer_field_date = new JTextField();
        producer_field_time = new JTextField();
        producer_field_orderNum = new JTextField();
        producer_field_itemNum = new JTextField();
        producer_field_quantity = new JTextField();

        producer_panel_checked.add(producer_label_date);
        producer_panel_checked.add(producer_field_date);
        producer_panel_checked.add(producer_label_time);
        producer_panel_checked.add(producer_field_time);
        producer_panel_checked.add(producer_label_orderNum);
        producer_panel_checked.add(producer_field_orderNum);
        producer_panel_checked.add(producer_label_itemNum);
        producer_panel_checked.add(producer_field_itemNum);
        producer_panel_checked.add(producer_label_quantity);
        producer_panel_checked.add(producer_field_quantity);

    }

    private void addComponentsToUncheckedPanel(){
        producer_label_address = new JLabel("Address");
        producer_area_address = new JTextArea(5,50);

        producer_panel_unchecked.add(producer_label_address);
        producer_panel_unchecked.add(producer_area_address);
    }

    private void addComponentsToButtonsPanel(){

        producer_button_ok = new JButton("OK");
        producer_button_quit = new JButton("Quit");

        producer_button_ok.setEnabled(false);
        producer_panel_buttons.add(producer_button_ok);
        producer_panel_buttons.add(producer_button_quit);
    }

    private void addToListener(){
        producer_field_date.addFocusListener(new checkedFieldListener());
        producer_field_time.addFocusListener(new checkedFieldListener());
        producer_field_orderNum.addFocusListener(new checkedFieldListener());
        producer_field_itemNum.addFocusListener(new checkedFieldListener());
        producer_field_quantity.addFocusListener(new checkedFieldListener());

        producer_button_ok.addActionListener(new OkButtonListener());
        producer_button_quit.addActionListener(new QuitButtonListener());
    }

    private void addDataToQueue(){
        String date = producer_field_date.getText();
        String time = producer_field_time.getText();
        String orderNum = producer_field_orderNum.getText();
        String itemNum = producer_field_itemNum.getText();
        String quantity = producer_field_quantity.getText();
        String address = producer_area_address.getText();
        Order tmp;
        try{
            tmp = new Order(Order.makeDate(date),Order.makeTime(time),
                    Order.makeString(orderNum),Order.makeString(itemNum),
                    Order.makeInteger(quantity),address);
        }catch(ParseException e){
            System.err.println(e.getMessage());
            return;
        }
        orderBuffer.addOrder(tmp);
    }

    class OkButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addDataToQueue();
            producer_field_date.setText("");
            producer_field_time.setText("");
            producer_field_orderNum.setText("");
            producer_field_itemNum.setText("");
            producer_field_quantity.setText("");
            producer_area_address.setText("");

            producer_label_date.setForeground(Color.RED);
            producer_label_time.setForeground(Color.RED);
            producer_label_orderNum.setForeground(Color.RED);
            producer_label_itemNum.setForeground(Color.RED);
            producer_label_quantity.setForeground(Color.RED);

            producer_button_ok.setEnabled(false);

        }
    }

    class QuitButtonListener implements ActionListener{


        @Override
        public void actionPerformed(ActionEvent e) {

            isQuit = true;
            System.out.println("Producer Interrupted");
            synchronized (this){
                notifyAll();
            }
            Thread.currentThread().interrupt();
            setVisible(false);
            dispose();
            Thread.currentThread().interrupt();
        }

    }


    private boolean dateValid=false,timeValid=false,
            orderNumValid=false,itemNumValid=false,quantityValid = false;
    class checkedFieldListener implements FocusListener {


        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField focused = (JTextField) e.getSource();
            String text = focused.getText();
            if(focused == producer_field_date){
                if(!Order.checkDateValid(text)){
                    allValid=false;
                    dateValid=false;
                    producer_label_date.setForeground(Color.RED);
                }else{
                    dateValid=true;
                    producer_label_date.setForeground(Color.BLACK);
                }
            }else if(focused == producer_field_time){
                if(!Order.checkTimeValid(text)){
                    allValid=false;
                    timeValid=false;
                    producer_label_time.setForeground(Color.RED);
                }else{
                    timeValid = true;
                    producer_label_time.setForeground(Color.BLACK);
                }
            }else if(focused == producer_field_quantity){
                if(!Order.checkInteger(text)){
                    allValid=false;
                    quantityValid=false;
                    producer_label_quantity.setForeground(Color.RED);
                }else{
                    quantityValid = true;
                    producer_label_quantity.setForeground(Color.BLACK);
                }
            }else if(focused == producer_field_orderNum || focused == producer_field_itemNum){
                if(!Order.checkStringValid(text)){
                    allValid=false;
                    if(focused == producer_field_orderNum){
                        orderNumValid=false;
                        producer_label_orderNum.setForeground(Color.RED);
                    }else{
                        itemNumValid=false;
                        producer_label_itemNum.setForeground(Color.RED);
                    }
                }else{
                    if(focused == producer_field_orderNum){
                        orderNumValid = true;
                        producer_label_orderNum.setForeground(Color.BLACK);
                    }else{
                        itemNumValid=true;
                        producer_label_itemNum.setForeground(Color.BLACK);
                    }
                }
            }else{
                System.err.println("Unknown JTextField");
                return;
            }
            allValid = dateValid && timeValid
                    && orderNumValid && itemNumValid && quantityValid;
            if(!allValid) producer_button_ok.setEnabled(false);
            else producer_button_ok.setEnabled(true);
        }
    }


}