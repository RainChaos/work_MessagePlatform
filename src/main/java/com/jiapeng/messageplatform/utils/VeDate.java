package com.jiapeng.messageplatform.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HZL on 2018/12/29.
 */
public class VeDate {
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyyMMddHHmmss
     *
     * @param strDate
     * @return
     */
    public static Date ymdhmsStrToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     * 将长时间格式字符串转换为时间 format
     *
     * @param strDate
     * @return
     */
    public static Date StrToDateByFormat(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        if (!StringUtils.isNotEmpty(strDate))
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param format
     * @return
     */
    public static String getLocalDate(String format) {
        Date dateDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 按照传入的转换格式转换时间
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate, String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //将当前月加1；
        calendar.add(Calendar.MONTH, 1);
        //在当前月的下一月基础上减去1毫秒
        calendar.add(Calendar.MILLISECOND, -1);
        //获得当前月最后一天
        return calendar.getTime();
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 获取一个时间的当天的0点
     *
     * @param date
     * @return
     */
    public static Date getDateStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一个时间的当天的最后一刻
     *
     * @param date
     * @return
     */
    public static Date getDateEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 给一个时间加上N天
     *
     * @param date
     * @return
     */
    public static Date getAddDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +day);//时间加day天
        date = calendar.getTime();
        return date;
    }

    /**
     * 给一个时间加上N小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date getAddHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, +hour);//时间+hour小时
        date = calendar.getTime();
        return date;
    }

    /**
     * 给一个时间加上N小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHourAndMin(Date date, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);//时间+hour小时
        calendar.add(Calendar.MINUTE, min);//+min
        date = calendar.getTime();
        return date;
    }

    /**
     * 判断两个时间是否相同（秒级别）
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isSameTimeOnSec(Date date1, Date date2) {
        long a = date1.getTime() / 1000;
        long b = date2.getTime() / 1000;
        int c = (int) (a - b);
        if (c == 0)
            return true;
        return false;
    }

    /**
     * 根据要计算的时间差类型计算并按照设定的格式返回两个时间之间的数组
     *
     * @param startDate
     * @param endDate
     * @param diffType  要区分的时间间隔类型:day、month、year
     * @param formatStr
     * @return
     */
    public static List<String> calcTowDateDiffArr(Date startDate, Date endDate, String diffType, String formatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int field = 0;
        switch (diffType) {
            case "day":
                field = Calendar.DATE;
                break;
            case "month":
                field = Calendar.MONTH;
                break;
            case "year":
                field = Calendar.YEAR;
                break;
            default:
                break;
        }
        List<String> list = new ArrayList<>();
        while (calendar.getTime().compareTo(endDate) < 1) {
            list.add(dateToStr(calendar.getTime(), formatStr));
            calendar.add(field, 1);
        }
        return list;
    }


    /**
     * 给时间加上N个时间单位后返回时间
     *
     * @param date
     * @param type day、month、year
     * @param n
     * @return
     */
    public static Date getNDate(Date date, String type, int n) {
        int field = 0;
        switch (type) {
            case "day":
                field = Calendar.DATE;
                break;
            case "month":
                field = Calendar.MONTH;
                break;
            case "year":
                field = Calendar.YEAR;
                break;
            default:
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, +n);//时间加day天
        date = calendar.getTime();
        return date;
    }


    //   private static Logger logger = LoggerFactory.getLogger(VeDate.class);

//    public static void main(String[] args) {
//        String dateStr = "2019-1-6";
////        Date date = strToDateLong(null);
//        System.out.println(dateStr);
//        logger.info("this a test!");
//    }

    //判断是否是否在n天之内
    public static boolean isLatestDay(Date startTime, Date endTime, int afterDay) {
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(startTime);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, afterDay - 1);  //设置为7天后
        Date afterNdays = calendar.getTime();   //得到7天前的时间
        if (afterNdays.getTime() < endTime.getTime()) {
            return false;
        } else {
            return true;
        }
    }

    //判断是否是否在n月之内
    public static boolean isLatestMonth(Date startTime, Date endTime, int afterMon) {
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(startTime);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, afterMon - 1);  //设置为7天后
        Date afterNMons = calendar.getTime();   //得到7天前的时间
        if (afterNMons.getTime() < endTime.getTime()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断某个时间是否超过
     *
     * @param configDate 系统配置的时间
     * @param actionDate 门禁出入时间
     * @return
     * @throws ParseException
     */
    public static boolean isLate(Date configDate, Date actionDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date configDate1 = df.parse(df.format(configDate));
        Date actionDate2 = df.parse(df.format(actionDate));

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(configDate1);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(actionDate2);
        if (beginTime.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] asd) {
        try {
            System.out.println(dateToStrLong(getLastDate(StrToDateByFormat("2019-07", "yyyy-MM"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
