package acme;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Reference:
 *     1.java.text: From Java Library, used to get Date Format
 *     2.java.util: From Java Library, used to get Date Object
 * Created by Xuhao Chen on 16/2/28.
 * Edited by Xuhao Chen on 16/3/20.
 */

public class Order {
    public Date date;
    public Date time;
    public String orderNum;
    public String itemNum;
    public Number quantity;
    public String address;

    public enum Type {

        DATE(new SimpleDateFormat("MM/dd/yy")),
        TIME(new SimpleDateFormat("h:mm a")),
        CURRENCY(NumberFormat.getCurrencyInstance()),
        DECIMAL(NumberFormat.getInstance()),
        INTEGER(NumberFormat.getIntegerInstance()),
        PERCENT(NumberFormat.getPercentInstance()),
        STRING(null),  //no need to check String Type
        TEXT(null);    //no need to check Text Area

        private Format format;

        Type (Format format){
            this.format = format;
        }

        /**
         *
         * @return Format Object of specific Type
         */
        public Object getFormat(){
            return format;
        }

        public static String getAddressString(Order order){
            String address=order.getAddress();
            String result="";
            for(Character c : address.toCharArray()){
                if((int)c==10){
                    result += (char)92;
                    result += (char)110;
                }else{
                    result += c;
                }
            }
            result = result.trim();
            return result;
        }
    }


    public Order(Date date,Date time,String orderNum,
                 String itemNum,Number quantity,String address){
        this.date = date;
        this.time = time;
        this.orderNum = orderNum;
        this.itemNum = itemNum;
        this.quantity = quantity;
        this.address = address;
    }

    public Order(String date, String time, String orderNum,
                 String itemNum, String quantity,String address){
        try{
            this.date = makeDate(date);
            this.time = makeTime(time);
            this.orderNum = makeString(orderNum);
            this.itemNum = makeString(itemNum);
            this.quantity = makeInteger(quantity);
            this.address = makeString(address);

        }catch(ParseException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * All methods below will return a String in Format of each Type
     */

    public String getDateString(){
        return ((SimpleDateFormat) Type.DATE.getFormat()).format(date);
    }

    public String getTimeString(){
        return ((SimpleDateFormat) Type.TIME.getFormat()).format(time);
    }

    public String getOrderNum(){
        return orderNum;
    }

    public String getItemNum(){
        return itemNum;
    }

    public String getQuantityString(){
        return quantity.toString();
    }

    public String getAddress(){
        return address;
    }


    public static Date makeDate(String date) throws ParseException {
        try{
            return ((SimpleDateFormat) Type.DATE.getFormat()).parse(date);
        }catch(ParseException e){
            throw new ParseException("ParseException: Fail to make Date: Invalid format",0);
        }

    }

    public static Date makeTime(String time) throws ParseException {
        try{
            return ((SimpleDateFormat) Type.TIME.getFormat()).parse(time);
        }catch(ParseException e){
            throw new ParseException("ParseException: Fail to make Time: Invalid format",0);

        }

    }

    public static String makeString(String string) throws ParseException {
        if(string !=null && !string.equals("")) return string;
        else{
            throw new ParseException("ParseException: Fail to make String: input string is empty",0);
        }
    }

    public static Number makeInteger(String integer) throws ParseException {
        try{
            return ((NumberFormat) Type.INTEGER.getFormat()).parse(integer);
        }catch(ParseException e){
            throw new ParseException("ParseException: Fail to make Integer: Invalid format",0);
        }
    }

    public static boolean checkDateValid(String date){
        try{
            makeDate(date);
            return true;
        }catch(ParseException e){
            return false;
        }
    }

    public static boolean checkTimeValid(String time){
        try{
            makeTime(time);
            return  true;
        }catch(ParseException e){
            return false;
        }
    }

    public static boolean checkStringValid(String string){
        try{
            makeString(string);
            return true;
        }catch (ParseException e){
            return false;
        }
    }

    public static boolean checkInteger(String integer){
        try{
            makeInteger(integer);
            return true;
        }catch(ParseException e){
            return false;
        }
    }
}
