package com.markerhub.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar c = Calendar.getInstance();

	public static String getBZFirst() {
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 1);//
		return sdf.format(c.getTime());
	}

	public static String getBZLeast() {
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 7);// 本周最后一天
		return sdf.format(c.getTime());
	}

	public static String getBYFirst() {
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);// 本月第一天
		return sdf.format(c.getTime());
	}

	public static String getBYLeast() {
		c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);// 本月最后一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(c.getTime());
	}

	public static String getBNFirst() {
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, 1);// 本年第一天
		return sdf.format(c.getTime());
	}

	public static String getBNLeast() {
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, 1);// 本年最后一天
		c.set(Calendar.YEAR, 1);
		c.add(Calendar.DAY_OF_YEAR, -1);
		return sdf.format(c.getTime());
	}

	public static int getSpecTime(String destTime) throws ParseException {
		Date dest = sdf.parse(destTime);
		double between = (new Date().getTime() - dest.getTime()) / 1000;// 除以1000是为了转换成秒
		return Integer.parseInt((between / (24 * 3600) + "").substring(0, 1));
	}

	public static String getCurrentDate() throws ParseException {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		Date now = new Date();
		String date = fm.format(now);
		c.setTime(now);
		int week = c.get(Calendar.DAY_OF_WEEK);
		return date + " 星期" + tranNum2Chi(week);
	}

	public static String getCurrentYear() {
		Calendar cld = Calendar.getInstance();
		return String.valueOf(cld.get(cld.YEAR));
	}

	public static String getCurrentDate(String pattern) throws ParseException {
		SimpleDateFormat fm = new SimpleDateFormat(pattern);
		Date now = new Date();
		String date = fm.format(now);
		return date;
	}

	public static String tranNum2Chi(Object num) {
		int day = Integer.parseInt(num.toString());
		switch (day) {
		case 1:
			return "日";
		case 2:
			return "一";
		case 3:
			return "二";
		case 4:
			return "三";
		case 5:
			return "四";
		case 6:
			return "五";
		case 7:
			return "六";
		}
		return null;
	}

	/**
	 * 默认的日期时间格式
	 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将日期转化为指定格式的字符串
	 *
	 * @param date
	 *            日期时间对象,若为空则返回""
	 * @param pattern
	 *            日期格式
	 * @return str
	 */
	public static String format(Date date, String pattern) {
		String str = "";
		if (null != date) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			str = formatter.format(date);
		}
		return str;
	}

	/**
	 * 将字符串转化为指定格式的日期
	 *
	 * @param dateStr
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return date 日期
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date parse(String dateStr, String pattern)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = format.parse(dateStr);
		return date;
	}

    /**
     * 转成Date
     * @param pars
     * @return
     */
    public static Date convertStringtoDate(String pars)
    {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mydate=null;//new Date(100,1,1);
        try {
            mydate = dateFormat1.parse(pars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  mydate;
    }


	/**
	 * 计算2个时间之间的差值，单位为"秒"
	 *
	 * @param begin
	 *            起始时间
	 * @param end
	 *            结束时间
	 * @return 时间之差
	 */
	public static long diffSeconds(Date begin, Date end) {
		if (null != begin && null != end) {
			return (end.getTime() - begin.getTime()) / 1000;
		} else if (null == begin) {
			return (end.getTime()) / 1000;
		} else if (null == end) {
			return (begin.getTime()) / 1000;
		} else {
			return 0;
		}

	}

	/**
	 * 将毫秒数转换为：x天x小时x分钟
	 *
	 * @param millis
	 * @return
	 */
	public static String getDateTimeByMillis(long millis) {
		StringBuffer re = new StringBuffer();
		long dayMili = 24 * 60 * 60 * 1000;
		long day = millis / dayMili;
		if (day > 0)
			re.append(day).append("天");
		long hourMili = millis % dayMili;
		if (hourMili > 0) {
			long hour = hourMili / (60 * 60 * 1000);
			re.append(hour).append("小时");
			long minuteMini = hourMili % (60 * 60 * 1000);
			if (minuteMini > 0) {
				long minute = minuteMini / (60 * 1000);
				re.append(minute).append("分钟");
			}
		} else {
			re.append(0).append("小时");
		}
		return re.toString();
	}

	public static void main(String[] args) throws ParseException {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DAY_OF_MONTH, -1);
		cl.add(Calendar.HOUR_OF_DAY, -2);
		cl.add(Calendar.MINUTE, -45);
		Date now = new Date();
		long bf = now.getTime() - cl.getTimeInMillis();
		System.out.println(getDateTimeByMillis(bf));

		String dateStr = "2014-05-20 00:00:01";
		Date date = parse(dateStr, "yyyy-MM-dd HH:mm:ss");
		System.out.println(format(getEndWorkDate(date, 15),
				"yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 在当前的时间增加月数
	 *
	 * @param n
	 *            要增加的月数
	 * @return
	 */
	public static Date addMonth(Date d, int n) throws ParseException {

		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.MONTH, n);// 增加月数

		return cd.getTime();
	}

	/**
	 * 获取workDateCount个工作日后的日期，这里的工作日指星期一至星期五
	 *
	 * @param startDate
	 *            开始日期
	 * @param workDateCount
	 *            工作日天数
	 * @return N个工作日后的日期
	 */
	public static Date getEndWorkDate(Date startDate, int workDateCount) {
		Calendar stc = Calendar.getInstance();
		stc.setTime(startDate);
		int notWorkDateCount = 0;
		for (int i = 0; i < workDateCount; i++) {
			stc.add(Calendar.DAY_OF_MONTH, 1);
			int sdw = stc.get(Calendar.DAY_OF_WEEK);
			if (sdw == 1 || sdw == 7) {
				notWorkDateCount++;
			}
		}
		if (notWorkDateCount > 0) {
			return getEndWorkDate(stc.getTime(), notWorkDateCount);
		} else {
			return stc.getTime();
		}
	}

	/**
	 * 获取当前日期的前一天23时59分59秒
	 *
	 * @return 前一天23时59分59秒
	 */
	public static String getDayBefore() {
		Calendar calendar = Calendar.getInstance();
		// 将小时至0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		calendar.set(Calendar.MINUTE, 0);
		// 将秒至0
		calendar.set(Calendar.SECOND, 0);
		// 将毫秒至0
		calendar.set(Calendar.MILLISECOND, 0);
		// 获得当前月第一天
		Date sdate = calendar.getTime();
		// 将当前月加1；
		calendar.add(Calendar.MILLISECOND, -1);
		// 获得当前月最后一天
		Date edate = calendar.getTime();

		String dateStr = DateUtil.format(edate, "yyyy年MM月dd日HH:mm:ss");
		return dateStr;
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return
	 */
	public static String getDateTimeNow() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		String timeNow = format1.format(currentTime);
		return timeNow;
	}

	/**
	 * 计算指定日期（yyyy-MM-dd）下当前时分秒的时间
	 * @param caculaTime
	 * @return
	 * @throws ParseException
	 */
	public static Date caculateTime(String caculaTime) throws ParseException {
		Date now = new Date();
		String nowDayStr = DateUtil.format(now, "HH:mm:ss");
		String caculaTimeStr = caculaTime + " " + nowDayStr;
		Date resultTime = DateUtil.parse(caculaTimeStr, "yyyy-MM-dd HH:mm:ss");
		return resultTime;//得到精确到分秒的处理时间
	}
}
