package com.tacoma.uw.leebui99.yaker.fragment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Be Thao on 1/30/2016.
 */
public class Generator {

    public ArrayList<MyColor> myList;

    public Generator() {
        myList = new ArrayList<MyColor>();
    }

    public void add(String dis, int colorNum, String ans) {
        MyColor newColor = new MyColor(dis, colorNum, ans);
        myList.add(newColor);
    }

    public MyColor generate() {
        Random rd = new Random();
        int index = rd.nextInt(myList.size());
        return myList.get(index);
    }

    public class MyColor {
        public String distraction;
        public int color;
        public String answer;

        public MyColor(String dis, int colorNum, String ans) {
            distraction = dis;
            color = colorNum;
            answer = ans;
        }

        public boolean check(String word) {
            return answer.toLowerCase().equals(word.toLowerCase());
        }

    }
}
