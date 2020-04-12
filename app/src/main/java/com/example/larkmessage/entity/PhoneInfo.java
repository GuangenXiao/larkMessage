package com.example.larkmessage.entity;

import android.content.Context;

import com.example.larkmessage.unit.DateUnit;
import com.example.larkmessage.unit.SystemUtil;

import java.text.ParseException;
import java.util.Locale;

public class PhoneInfo {
private Locale[] locales =null;
private String language=null;
private String model=null;
private String SystemVersion=null;
private  String DeviceBrand=null;
private  String IMEI=null;
private  String time=null;
public PhoneInfo()
{

}
public  void setAll(Context context)
{
    try {
        locales=SystemUtil.getSystemLanguageList();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    try {
        language = SystemUtil.getSystemLanguage();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    try {
        model=SystemUtil.getSystemModel();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    try {
        SystemVersion =SystemUtil.getSystemVersion();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    try {
        DeviceBrand =SystemUtil.getDeviceBrand();
    }
    catch (Exception e) {
        e.printStackTrace();
    }

    try {
        time = DateUnit.getSystemTimeAndDate();
    } catch (ParseException e) {
        e.printStackTrace();
    }
}

    public Locale[] getLocales() {
        return locales;
    }

    public void setLocales(Locale[] locales) {
        this.locales = locales;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemVersion() {
        return SystemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        SystemVersion = systemVersion;
    }

    public String getDeviceBrand() {
        return DeviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        DeviceBrand = deviceBrand;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
