package com.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******************************************************************************* 
 * pinyin4j is a plug-in, you can kind of Chinese characters into phonetic.Multi-tone character,Tone 
 * Detailed view http://pinyin4j.sourceforge.net/ 
 */
public class PinyinTools {

    protected final static Logger error_logger             = LoggerFactory.getLogger("error_log");

    public static String getPinyin(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese));  
    }  
  
    public static String getPinyinToUpperCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toUpperCase();  
    }  
  
    public static String getPinyinToLowerCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toLowerCase();  
    }  
  
    public static String getPinyinFirstToUpperCase(String chinese) {
        return getPinyin(chinese);  
    }  
  
    public static String getPinyinJianPin(String chinese) {
        return getPinyinConvertJianPin(getPinyin(chinese));  
    }  
    public static String getPinyinJianPinToLowerCase(String chinese) {  
    	return getPinyinConvertJianPin(getPinyin(chinese)).toLowerCase();  
    }  
  
    public static Set<String> makeStringByStringSet(String chinese) {
        char[] chars = chinese.toCharArray();  
        if (chinese != null && !chinese.trim().equalsIgnoreCase("")) {  
            char[] srcChar = chinese.toCharArray();  
            String[][] temp = new String[chinese.length()][];  
            for (int i = 0; i < srcChar.length; i++) {  
                char c = srcChar[i];  
  
                // 是中文或者a-z或者A-Z转换拼音  
                if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {  
  
                    try {  
                        temp[i] = PinyinHelper.toHanyuPinyinStringArray(  
                                chars[i], getDefaultOutputFormat());  
  
                    } catch (BadHanyuPinyinOutputFormatCombination e) {  
                        e.printStackTrace();  
                    }  
                } else if (((int) c >= 65 && (int) c <= 90)  
                        || ((int) c >= 97 && (int) c <= 122)) {  
                    temp[i] = new String[] { String.valueOf(srcChar[i]) };  
                } else {  
                    temp[i] = new String[] {c+""};  
                }  
            }  
            String[] pingyinArray = Exchange(temp);  
            Set<String> zhongWenPinYin = new HashSet<String>();  
            for (int i = 0; i < pingyinArray.length; i++) {  
                zhongWenPinYin.add(pingyinArray[i]);  
            }  
            return zhongWenPinYin;  
        }  
        return null;  
    }  
  
    public static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字  
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示  
        return format;  
    }  
  
    public static String[] Exchange(String[][] strJaggedArray) {
        String[][] temp = DoExchange(strJaggedArray);  
        return temp[0];  
    }  
  
    private static String[][] DoExchange(String[][] strJaggedArray) {
        int len = strJaggedArray.length;  
        if (len >= 2) {  
            int len1 = strJaggedArray[0].length;  
            int len2 = strJaggedArray[1].length;  
            int newlen = len1 * len2;  
            String[] temp = new String[newlen];  
            int Index = 0;  
            for (int i = 0; i < len1; i++) {  
                for (int j = 0; j < len2; j++) {  
                    temp[Index] = capitalize(strJaggedArray[0][i])  
                            + capitalize(strJaggedArray[1][j]);  
                    Index++;  
                }  
            }  
            String[][] newArray = new String[len - 1][];  
            for (int i = 2; i < len; i++) {  
                newArray[i - 1] = strJaggedArray[i];  
            }  
            newArray[0] = temp;  
            return DoExchange(newArray);  
        } else {  
            return strJaggedArray;  
        }  
    }  
  
    public static String capitalize(String s) {
        char ch[];  
        ch = s.toCharArray();  
        if (ch[0] >= 'a' && ch[0] <= 'z') {  
            ch[0] = (char) (ch[0] - 32);  
        }  
        String newString = new String(ch);  
        return newString;  
    }  
  
    public static String getPinyinZh_CN(Set<String> stringSet) {
        StringBuilder str = new StringBuilder();  
        int i = 0;  
        for (String s : stringSet) {  
            if (i == stringSet.size() - 1) {  
                str.append(s);  
            } else {  
                str.append(s + ",");  
            }  
            i++;  
        }  
        return str.toString();  
    }  
  
    public static String getPinyinConvertJianPin(String chinese) {
        String[] strArray = chinese.split(",");  
        String strChar = "";  
        for (String str : strArray) {  
            char arr[] = str.toCharArray(); // 将字符串转化成char型数组  
            for (int i = 0; i < arr.length; i++) {  
                if (arr[i] >= 65 && arr[i] < 91) { // 判断是否是大写字母  
                    strChar += new String(arr[i] + "");  
                }  
            }  
            strChar += ",";  
        }
        if(strChar.length()>0){
        	strChar = strChar.substring(0, strChar.length()-1);
        }
        return strChar;  
    }

    /**
     * Pinyin4j
     * 将汉字转换为全拼
     */
    public static String getPingYin(String src) {

        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                    t4 += Character.toString(t1[i]);
            }
            // System.out.println(t4);
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    // 返回中文的首字母
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    // 将字符串转移为ASCII码
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }




    /**
     * 按照指定长度切割字符串
     * @param inputString   需要切割的源字符串
     * @param length    指定的长度
     * @return
     */
    public static String getDivLines(String inputString, int length) {
        StringBuilder sb = new StringBuilder();
        int remainder = (inputString.length()) % length;
        // 一共要分割成几段
        int number = (int) Math.floor((inputString.length()) / length);
        for (int index = 0; index < number; index++) {
            String childStr = inputString.substring(index * length, (index + 1) * length);
            sb.append(getIndexMul(childStr));
            //System.out.println(childStr);
        }
        if (remainder > 0) {
            String cStr = inputString.substring(number * length, inputString.length());
            sb.append(getIndexMul(cStr));
        }
        return sb.toString();
    }
    public static List<String> matchStrByLength(String content,int length){
        List<String> strs = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\w{"+length+"}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            strs.add(matcher.group());
        }
        return strs;
    }

    /**
     * 汉字转拼音
     * 处理点:
     *        1.支持转多音
     *        2.长度超过指定长度，进行分割再进行多音转拼
     *        3.利用Pinyin4j进行全拼和全简拼转拼，不支持多音全拼和全简拼(避免包含多音字且长度过长字符串转拼导致内存溢出)
     */
    public static String getIndex(String str,int length){
        try {
            str = str.replaceAll(" ","");
            if(str.length()>length){
                return getDivLines(str,length).toString()+","+getIndexSin(str);
            }else{
                return getIndexMul(str);
            }
        } catch (Exception e) {
            error_logger.error("PinyinTools getIndex error : "+e);
        }
        return "";
    }
    /**
     * 单音全拼简拼
     */
    public static String getIndexSin(String str){
        String jp = getPinYinHeadChar(str);
        String qp = getPingYin(str);
        if(jp.equals(qp)){
            return jp;
        }else{
            return jp+","+qp;
        }
    }
    /**
     * 支持多音全拼简拼
     */
    public static String getIndexMul(String str){
        String jp = getPinyinJianPinToLowerCase(str);
        String qp = getPinyinToLowerCase(str);
        if(jp.equals(qp)){
            return jp;
        }else{
            return jp+","+qp;
        }
    }

    public static void main(String[] args) {
        Date s1 = new Date();
        String str1 = "阿卡丽考虑啦啦了121212啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯啦咯啦咯啦图咯gental235665啦咯啦咯啦咯掏空了啦咯啦咯啦咯啦掏空了啦咯啦咯啦咯啦太累了了来咯啦我了垃圾咯啦咯啦";
        String str = "重庆了";
        String strs = "咯啦咯";
        System.out.println(getIndexSin(strs));
        //System.out.println(getPingYin(str1));

        /*Regex.Matches("IBM发布基于人脑特性设计的全新计算架构和编程语言", @"\w{5}");
        System.out.println("大写输出：" + getPinyinToUpperCase(str));  
        System.out.println("首字母大写输出：" + getPinyinFirstToUpperCase(str));  
        System.out.println("简拼输出：" + getPinyinJianPin(str));  */
        //System.out.println("简拼输出：" + getPinyinJianPinToLowerCase(str));
        //System.out.println("小写输出：" + getPinyinToLowerCase(str));
        //System.out.println("输出：" + getIndex(str));
        System.out.println("-------"+(new Date().getTime()-s1.getTime())/1000.0D+"秒");
        //System.out.println("-------"+(new Date().getTime()-s1.getTime())+"毫秒");

    }  
}  