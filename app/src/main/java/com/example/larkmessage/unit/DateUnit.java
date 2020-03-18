package com.example.larkmessage.unit;

import com.example.larkmessage.entity.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUnit {
    public GregorianCalendar aCalendar ;
    public String getSystemDate() throws ParseException
    {
        aCalendar= new GregorianCalendar();
        String aDate =  aCalendar.get(Calendar.DATE)+"-"+
                (aCalendar.get(Calendar.MONTH)+1)+"-"+aCalendar.get(Calendar.YEAR);

        return aDate;
    }
    public String getSystemTimeAndDate() throws ParseException
    {
        aCalendar= new GregorianCalendar();
        String aDate =  aCalendar.get(Calendar.DATE)+"-"+
                (aCalendar.get(Calendar.MONTH)+1)+"-"+aCalendar.get(Calendar.YEAR)+"."+
                aCalendar.get(Calendar.HOUR_OF_DAY)+":"+aCalendar.get(Calendar.MINUTE)+":"+aCalendar.get(Calendar.SECOND);
        return aDate;
    }
    public Date convertStringToDate(String aDate) throws ParseException
    {
        DateFormat aFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateValue = (Date)aFormater.parse(aDate);
        return dateValue;
    }
    public Date convertStringToDateAddTime(String aDate) throws ParseException
    {
        DateFormat aFormater = new SimpleDateFormat("dd-MM-yyyy.HH:mm:ss");
        Date dateValue = (Date)aFormater.parse(aDate);
        return dateValue;
    }
    /*final Comparator c =new Comparator<Message>() {
        @Override
        public int compare(MassageItem o1, MassageItem o2) {
            // if(MassageItem)
            try {
                if (u.convertStringToDateAddTime(o1.getTime()).before(u.convertStringToDateAddTime(o2.getTime()))) {
                    return 1;
                } else return -1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        }
    };*/

}

