package acme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Xuhao Chen on 16/4/3.
 */
public class CustomerForm extends JFrame {

    private JPanel customer_panel_checked, customer_panel_unchecked, customer_panel_buttons;
    private JLabel customer_label_date, customer_label_time, customer_label_orderNum,
            customer_label_itemNum, customer_label_quantity, customer_label_address;
    private JTextField customer_field_date, customer_field_time, customer_field_orderNum,
            customer_field_itemNum, customer_field_quantity;
    private JTextArea customer_area_address;
    private JButton customer_button_ok, customer_button_quit;

    private boolean isQuit = false;
    private Object lock;

    private OrderBuffer orderBuffer;

    public CustomerForm(OrderBuffer orderBuffer,final Object lock){
        super("Acme Shipping Form");
        this.orderBuffer = orderBuffer;
        this.lock = lock;
        setLayout(new BorderLayout());

        initPanels();

        addComponentsToCheckedPanel();
        addComponentsToUncheckedPanel();
        addComponentsToButtonsPanel();

        add("North", customer_panel_checked);
        add("Center", customer_panel_unchecked);
        add("South", customer_panel_buttons);

        addToListener();

        pack();
        setPreferredSize(new Dimension(760,getHeight()));   //height: auto, width: 860
        pack();

        setVisible(true);

        Order tmp = orderBuffer.pollOrder();

        System.out.println("Order got!");
        System.out.println(tmp.getDateString());

        setText(tmp);

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

    private void setText(Order tmp){
        customer_field_date.setText(tmp.getDateString());
        customer_field_time.setText(tmp.getTimeString());
        customer_field_orderNum.setText(tmp.getOrderNum());
        customer_field_itemNum.setText(tmp.getItemNum());
        customer_field_quantity.setText(tmp.getQuantityString());
        customer_area_address.setText(tmp.getAddress());
    }

    private void initPanels(){
        customer_panel_checked = new JPanel();
        customer_panel_unchecked = new JPanel();
        customer_panel_buttons = new JPanel();

        customer_panel_checked.setLayout(new GridLayout(0,4));
        customer_panel_unchecked.setLayout(new FlowLayout(FlowLayout.LEFT));
        customer_panel_buttons.setLayout(new FlowLayout());
    }

    private void addToListener(){
        customer_button_ok.addActionListener(new CustomerForm.OkButtonListener());
        customer_button_quit.addActionListener(new CustomerForm.QuitButtonListener());
    }

    private void addComponentsToCheckedPanel(){

        customer_label_date = new JLabel("Date");
        customer_label_time = new JLabel("Time");
        customer_label_orderNum = new JLabel("Order #");
        customer_label_itemNum = new JLabel("Item #");
        customer_label_quantity = new JLabel("Quantity");

        customer_field_date = new JTextField();
        customer_field_time = new JTextField();
        customer_field_orderNum = new JTextField();
        customer_field_itemNum = new JTextField();
        customer_field_quantity = new JTextField();

        customer_field_date.setEditable(false);
        customer_field_time.setEditable(false);
        customer_field_orderNum.setEditable(false);
        customer_field_itemNum.setEditable(false);
        customer_field_quantity.setEditable(false);

        customer_panel_checked.add(customer_label_date);
        customer_panel_checked.add(customer_field_date);
        customer_panel_checked.add(customer_label_time);
        customer_panel_checked.add(customer_field_time);
        customer_panel_checked.add(customer_label_orderNum);
        customer_panel_checked.add(customer_field_orderNum);
        customer_panel_checked.add(customer_label_itemNum);
        customer_panel_checked.add(customer_field_itemNum);
        customer_panel_checked.add(customer_label_quantity);
        customer_panel_checked.add(customer_field_quantity);

    }

    private void addComponentsToUncheckedPanel(){
        customer_label_address = new JLabel("Address");
        customer_area_address = new JTextArea(5,50);

        customer_area_address.setEditable(false);

        customer_panel_unchecked.add(customer_label_address);
        customer_panel_unchecked.add(customer_area_address);
    }

    private void addComponentsToButtonsPanel(){

        customer_button_ok = new JButton("OK");
        customer_button_quit = new JButton("Quit");

        customer_panel_buttons.add(customer_button_ok);
        customer_panel_buttons.add(customer_button_quit);
    }

    private class OkButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Customer OK pressed.");

            Order tmp = orderBuffer.pollOrder();

            System.out.println("Order got!");
            System.out.println(tmp.getDateString());

            setText(tmp);


        }
    }

    private class QuitButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            isQuit = true;
            System.out.println("Customer Interrupted.");
            synchronized (this){
                notifyAll();
            }
            setVisible(false);
            dispose();
            Thread.currentThread().interrupt();

        }
    }

}
