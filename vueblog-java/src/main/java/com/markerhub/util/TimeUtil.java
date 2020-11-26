package com.markerhub.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 描述: 时间工具类
 *
 * @author Yangjinming
 * @create 2018-03-28 上午11:28
 */

public class TimeUtil {
    private TimeUtil() {
    }

    ;

    /******yyyyMMddHHmmssSSS***/
    public static final String FORMAT_MILSECOND = "yyyyMMddHHmmssSSS";
    /**
     * yyyy-MM-dd HH:mm:ss
     **/
    public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     **/
    public static final String FORMAT_MIN = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd
     **/
    public static final String FORMAT_DAY = "yyyy-MM-dd";
    /**
     * HH:mm:ss
     **/
    public static final String FORMAT_TIME = "HH:mm:ss";
    /**
     * HH:mm:ss
     **/
    public static final String FORMAT_TIME2 = "HH-mm-ss";

    /**
     * 将日期转成特定格式的字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (null == date || StringUtilExt.isEmpty(format)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 将日期字符串转成Date
     *
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String format) throws ParseException {
        if (StringUtilExt.isEmpty(dateStr) || StringUtilExt.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(dateStr);
    }

    /**
     * todo:排除非工作日、假期
     * 获取两个时间之间为周x的日期
     *
     * @param startTime
     * @param endTime
     * @param week 1为周日 2为周一。。。6为周五 7为周六
     * @param interval 相隔，单位周
     * @return
     * @throws ParseException
     */
    public static List<Date> getDayByWeek(Date startTime, Date endTime, int week,int interval) throws ParseException {
        long times = endTime.getTime()-startTime.getTime();
        long bt = times/1000/60/60/24;
        List<Date> dateList = new ArrayList<>();
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MINUTE, 59);
        //判断如果周期时间大于起止时间，则不允许生成反馈
        if(bt<interval*7) {
            return dateList;
        }
        //判断当前周是否还没超过指定周几
        if(week!=1&&begin.get(Calendar.DAY_OF_WEEK)<=week) {//本周
            begin.set(Calendar.DAY_OF_WEEK, week);
        } else {//下周(周日也放在下周,这里的设置一周的范围是上一周日 至 本周六)
            begin.set(Calendar.DAY_OF_WEEK, week);
            begin.set(Calendar.DAY_OF_YEAR, begin.get(Calendar.DAY_OF_YEAR)+7);
        }
        if(begin.getTime().before(end.getTime())) {
            dateList.add(begin.getTime());
            //间隔时间处理
            begin.set(Calendar.DAY_OF_YEAR, begin.get(Calendar.DAY_OF_YEAR)+interval*7);
            while(begin.getTime().before(end.getTime())) {
                dateList.add(begin.getTime());
                begin.set(Calendar.DAY_OF_YEAR, begin.get(Calendar.DAY_OF_YEAR)+interval*7);
            }
        }
        return dateList;
    }

    public static List<Date> getDateByMonth(Date startTime, Date endTime, int day,int interval){
        long times = endTime.getTime()-startTime.getTime();
        long bt = times/1000/60/60/24;
        List<Date> dateList = new ArrayList<>();
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MINUTE, 59);
        //判断如果周期时间大于起止时间，则不允许生成反馈
        if(bt<interval*30) {
            return dateList;
        }
        //判断当前月是否还没超过指定日
        if(day>=begin.get(Calendar.DAY_OF_MONTH)) {//本月
            begin.set(Calendar.DAY_OF_MONTH, day);
        } else {//下月
            begin.set(Calendar.DAY_OF_MONTH, day);
            begin.set(Calendar.MONTH, begin.get(Calendar.MONTH)+1);
        }
        if(begin.getTime().before(end.getTime())) {
            dateList.add(begin.getTime());
            //间隔时间处理
            begin.set(Calendar.MONTH, begin.get(Calendar.MONTH)+interval);
            while(begin.getTime().before(end.getTime())) {
                dateList.add(begin.getTime());
                begin.set(Calendar.MONTH, begin.get(Calendar.MONTH)+interval);
            }
        }
        return dateList;
    }

    /**
     * 获取每年的特定的月日
     * @param startTime
     * @param endTime
     * @param day
     * @return
     */
    public static List<Date> getDateByYear(Date startTime, Date endTime, Date day,int interval){
        long times = endTime.getTime()-startTime.getTime();
        long bt = times/1000/60/60/24;
        List<Date> dateList = new ArrayList<>();
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MINUTE, 59);
        Calendar aimMD = Calendar.getInstance();
        aimMD.setTime(day);
        int m = aimMD.get(Calendar.MONTH);//月从0开始到11对应1月到12月
        int d = aimMD.get(Calendar.DAY_OF_MONTH);
        //判断如果周期时间大于起止时间，则不允许生成反馈
        if(bt<interval*365) {
            return dateList;
        }
        //判断当前月是否还没超过指定月日
        if(m>begin.get(Calendar.MONTH)||(m==begin.get(Calendar.MONTH)&&d>begin.get(Calendar.DAY_OF_MONTH))) {//本年
            begin.set(Calendar.MONTH, m);
            begin.set(Calendar.DAY_OF_MONTH, d);
        } else {//下年
            begin.set(Calendar.MONTH, m);
            begin.set(Calendar.DAY_OF_MONTH, d);
            begin.set(Calendar.YEAR, begin.get(Calendar.YEAR)+1);
        }
        if(begin.getTime().before(end.getTime())) {
            dateList.add(begin.getTime());
            //间隔时间处理
            begin.set(Calendar.YEAR, begin.get(Calendar.YEAR)+interval);
            while(begin.getTime().before(end.getTime())) {
                dateList.add(begin.getTime());
                begin.set(Calendar.YEAR, begin.get(Calendar.YEAR)+interval);
            }
        }
        return dateList;
    }

    /**
     * 获取排除非工作日的日期
     * @param startTime
     * @param endTime
     * @param days
     * @return
     */
    public static List<Date> getBetweenDates(Date startTime, Date endTime,List<String> days,int interval) throws Exception{
        //办理时限（天）<间隔日可以生成是因为间隔日没有用工作日
        long times = endTime.getTime()-startTime.getTime();
        long bt = times/1000/60/60/24;
        // 返回的日期集合
        List<Date> dayList = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(startTime);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endTime);
        tempEnd.add(Calendar.DATE,1);// 日期加1(包含结束)
        //判断如果周期时间大于起止时间，则不允许生成反馈
        if(bt<interval) {
            return dayList;
        }
        start:
        for (int i = 0; i < days.size(); i++) {
            String tempStartStr = DateUtil.format(tempStart.getTime(), "yyyy-MM-dd");
            //判断是否工作日
            while(!days.contains(tempStartStr)) {
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                tempStartStr = DateUtil.format(tempStart.getTime(), "yyyy-MM-dd");
                if(tempStart.getTime().after(DateUtil.parse(days.get(days.size()-1) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"))) {//超出工作日期范围
                    break start;
                }
            }
            //判断是否和工作日一致
            if(days.get(i).equals(tempStartStr)){
                dayList.add(tempStart.getTime());
                for(int j = 0; j< interval ; j++) {//间隔日期
                    tempStart.add(Calendar.DAY_OF_YEAR, 1);
                    tempStartStr = DateUtil.format(tempStart.getTime(), "yyyy-MM-dd");
                    while(!days.contains(tempStartStr)) {
                        tempStart.add(Calendar.DAY_OF_YEAR, 1);
                        tempStartStr = DateUtil.format(tempStart.getTime(), "yyyy-MM-dd");
                        if(tempStart.getTime().after(DateUtil.parse(days.get(days.size()-1) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"))) {//超出工作日期范围
                            break start;
                        }
                    }
                }
            }
        }
        return dayList;
    }


    public static Date utcDateToStr(String date){
        date = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date utcDate=null;
        try {
            utcDate= format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDate;
    }

    public static void main(String[] args) throws ParseException {
        String start="2020-07-09";
        String end="2030-11-23";
        String md="2020-07-10";
//        List<String> days = new ArrayList<>();
//        days.add("2020-01-01");
//        days.add("2020-02-01");
//        days.add("2020-03-01");
//        days.add("2020-04-01");
//        days.add("2020-05-01");
//        days.add("2020-06-01");
//        days.add("2020-07-01");
//        days.add("2020-08-01");
//        days.add("2020-09-01");
//        days.add("2020-10-01");
//        days.add("2020-11-01");
//        days.add("2020-12-01");
//        Calendar cal = Calendar.getInstance();
//        for(String day :days) {
//            cal.setTime(DateUtil.parse(day, "yyyy-MM-dd"));
//            System.out.println(cal.get(Calendar.MONTH));
//        }
        try {
            System.out.println("start:"+start);
            System.out.println("end:"+end);
            List<Date> list = getDateByYear(DateUtil.parse(start, "yyyy-MM-dd"), DateUtil.parse(end, "yyyy-MM-dd"), DateUtil.parse(md, "yyyy-MM-dd"), 2);

            for(Date d: list) {
                System.out.println(DateUtil.format(d, "yyyy-MM-dd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
