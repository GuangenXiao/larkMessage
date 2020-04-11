package com.example.larkmessage.unit;

import com.example.larkmessage.R;

import java.util.ArrayList;
import java.util.HashMap;

public class IconDB {

   private HashMap<Integer,Integer> iconHash;
   private ArrayList<Integer> numList;
   private Integer defaultKey=7;
public IconDB()
{
iconHash = new HashMap<Integer,Integer>();
    iconHash.put(1, R.drawable.nn1);
    iconHash.put(2, R.drawable.nn2);
    iconHash.put(3, R.drawable.nn3);
    iconHash.put(4, R.drawable.nn4);
    iconHash.put(5, R.drawable.nn5);
    iconHash.put(6, R.drawable.nn6);
    iconHash.put(7, R.drawable.nn7);
    iconHash.put(8, R.drawable.nn8);
    iconHash.put(9, R.drawable.nn9);
    iconHash.put(10, R.drawable.nn10);
    iconHash.put(11, R.drawable.nn11);
    iconHash.put(12, R.drawable.nn12);
    iconHash.put(13, R.drawable.nn13);
    iconHash.put(14, R.drawable.nn14);
    iconHash.put(15, R.drawable.nn15);
    iconHash.put(16, R.drawable.nn16);
    iconHash.put(17, R.drawable.nn17);
    iconHash.put(18, R.drawable.nn18);
    iconHash.put(19, R.drawable.nn19);
    iconHash.put(20, R.drawable.nn20);
    iconHash.put(21, R.drawable.nn21);
    iconHash.put(22, R.drawable.nn22);
    iconHash.put(23, R.drawable.nn23);
    iconHash.put(24, R.drawable.nn24);
    iconHash.put(25, R.drawable.nn25);
    iconHash.put(26, R.drawable.nn26);
    iconHash.put(27, R.drawable.nn27);
    iconHash.put(28, R.drawable.nn28);
    iconHash.put(29, R.drawable.nn29);
    iconHash.put(30, R.drawable.nn30);
    iconHash.put(31, R.drawable.nn31);
    iconHash.put(32, R.drawable.nn32);
    iconHash.put(33, R.drawable.nn33);
    iconHash.put(34, R.drawable.nn34);

    numList = new ArrayList<Integer>();
    for(int i=1;i<=iconHash.size();i++ ) {
    numList.add(i);
    }
}

    public  HashMap<Integer, Integer> getIconHash() {
        return iconHash;
    }

    public void setIconHash(HashMap<Integer, Integer> iconHash) {
        this.iconHash = iconHash;
    }

    public Integer getIconID(Integer key)
    {
        return iconHash.get(key);
    }
    public Boolean containValue(Integer value)
    {
        return iconHash.containsValue(value);
    }
    public Boolean containKey(Integer key)
    {

        return iconHash.containsKey(key);
    }
    public Integer getDefaultIcon()
    {
        return  iconHash.get(defaultKey);
    }
    public Integer getDefaultKey()
    {
        return  defaultKey;
    }
    public ArrayList<Integer> getList()
    {
        ArrayList<Integer> list;
        list= numList;
        return list;

    }
}
