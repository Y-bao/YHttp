package appframe.utils.serialize;

import java.util.Random;

/**
 * Created by Y-bao on 2016/3/29 14:37.
 */
public class RandomStr {
    public static String getIntStr(int strLength) {
        Random rd = new Random();
        String n = "";
        int rdGet;

        do {
            rdGet = Math.abs(rd.nextInt()) % 10 + 48; //产生48到57的随机数(0-9的键位值)
            // rdGet=Math.abs(rd.nextInt())%26+97; //产生97到122的随机数(a-z的键位值)
            char num1 = (char) rdGet;
            String dd = Character.toString(num1);
            n += dd;
        } while (n.length() < strLength);
        return n;
    }

    public static String getStr(int strLength) {
        Random rd = new Random();
        String n = "";
        int rdGet;

        do {
            rdGet = Math.abs(rd.nextInt()) % 36;
            if (rdGet <= 10) {
                rdGet += 48;
            } else {
                rdGet -= 10;
                rdGet += 97;
            }
            //rdGet = Math.abs(rd.nextInt()) % 10 + 48 //产生48到57的随机数(0-9的键位值)
            // rdGet=Math.abs(rd.nextInt())%26+97; //产生97到122的随机数(a-z的键位值)
            char num1 = (char) rdGet;
            String dd = Character.toString(num1);
            n += dd;
        } while (n.length() < strLength);
        return n;
    }


    /**
     * 数字不足位数左补0
     *
     * @param str
     * @param strLength
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }
}
