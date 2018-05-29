﻿package ager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 公历农历转换工具类
 * 
 * @author 李海涛
 */
public class ChineseCalendar {
	
	//////////////////////////////////////////////公历转农历///////////////////////////////////////////////////
	
	private int year;
	private int month;
	private int day;
	private boolean leap;
	final static String chineseMonth[] = { "正", "二", "三", "四", "五", "六", "七",
			"八", "九", "十", "冬", "腊" };
	final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七",
		"八", "九", "十" };
	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日");
	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	// ====== 传回农历 y年的总天数
	final private int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	// ====== 传回农历 y年闰月的天数
	final private int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	final private int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ====== 传回农历 y年m月的总天数
	final private int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	// ====== 传回农历 y年的生肖
	/**
	 * 返回年属相
	 */
	public String getAnimalsYear() {
		final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇",
				"马", "羊", "猴", "鸡", "狗", "猪" };
		return Animals[(year - 4) % 12];
	}

	// ====== 传入 offset 传回干支, 0=甲子
	/**
	 * 返回年(甲子)
	 */
	public String getCyclical() {
		int num = year - 1900 + 36;
		return (cyclicalm(num));
	}

	// ====== 传入 月日的offset 传回干支, 0=甲子
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚",
				"辛", "壬", "癸" };
		final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午",
				"未", "申", "酉", "戌", "亥" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * 节气
	 */
	private String solar = null;

	/**
	 * 传入日期格式为:2012-01-01
	 * 
	 * @param dateStr
	 * @throws ParseException
	 */
	public ChineseCalendar(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			dateChange(cal);
			this.solar = getChineseTwentyFourDay(date);
		} catch (Exception e) {
			System.out.println("ChineseCalendar传入时间格式出错:" + e.toString());
		}

	}

	/**
	 * 传入日期Date
	 * 
	 * @param dateStr
	 * @throws ParseException
	 */
	public ChineseCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		dateChange(cal);
		this.solar = getChineseTwentyFourDay(date);

	}

	public String getChineseDay() {
		return getChinaDayString(day);
	}

	/**
	 * 返回农历
	 * 
	 * @return
	 */
	public String getChineseMouthDay() {
		return chineseMonth[month - 1] + "月" + getChinaDayString(day);
	}

//	/**
//	 * 显示详细信息
//	 */
//	public String toString() {
//		return getCyclical() + "年" + (leap ? "闰" : "")
//				+ chineseNumber[month - 1] + "月" + getChinaDayString(day)
//				+ " 【 " + getAnimalsYear() + "年】";
//	}
	
	/**
	 * 显示详细信息
	 */
	public String toString() {
		return getCyclical() + "年" + "(" + getYear() + ")" + (leap ? "闰" : "")
				+ chineseMonth[month - 1] + "月" + getChinaDayString(day);
	}
	
	/**
	 * 返回农历节日
	 */
	public String getFestival() {
		String festival = "";
		String dateStr = (leap ? "闰" : "") + chineseMonth[month - 1] + "月" + getChinaDayString(day);
		if ("正月初一".equals(dateStr)) {
			festival = "春节";
		} else if ("正月十五".equals(dateStr)) {
			festival = "元宵节";
		} else if ("五月初五".equals(dateStr)) {
			festival = "端午节";
		} else if ("七月初七".equals(dateStr)) {
			festival = "七夕";
		} else if ("七月十五".equals(dateStr)) {
			festival = "中元节";
		} else if ("八月十五".equals(dateStr)) {
			festival = "中秋节";
		} else if ("九月初九".equals(dateStr)) {
			festival = "重阳节";
		} else if ("腊月初八".equals(dateStr)) {
			festival = "腊八节";
		} else if ("腊月卅十".equals(dateStr)) {
			festival = "除夕";
		}
		return festival;
	}

	/**
	 * 返回节气
	 * 
	 * @return
	 */
	public String getSolartem() {
		return solar;
	}

	/**
	 * 获得节气
	 * 
	 * @param date
	 * @return
	 */
	public static String getSolartem(Date date) {
		return getChineseTwentyFourDay(date);
	}

	/**
	 * 获得气节 日期格式:2012-01-01
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getSolartem(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date date = sdf.parse(dateStr);
			return getChineseTwentyFourDay(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
	 * dayCyl5:与1900年1月31日相差的天数,再加40 ?
	 * 
	 * @param cal
	 * @return
	 */
	private void dateChange(Calendar cal) {
		int leapMonth = 0;
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse("1900年1月31日");
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
		}

		// 求出和1900年1月31日相差的天数
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);

		// 用offset减去每农历年的天数
		// 计算当天是农历第几天
		// i最终结果是农历的年份
		// offset是当年的第几天
		int iYear, daysOfYear = 0;
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
		}
		// 农历年份
		year = iYear;
		leapMonth = leapMonth(iYear); // 闰哪个月,1-12
		leap = false;

		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);

			offset -= daysOfMonth;
			// 解除闰月
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
		}
		// offset为0时，并且刚才计算的月份是闰月，要校正
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
			}
		}
		// offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
		}
		month = iMonth;
		day = offset + 1;
	}

	// 农历显示格式
	private String getChinaDayString(int day) {
		String chineseTen[] = { "初", "十", "廿", "卅" };
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 10)
			return "初十";
		else
			return chineseTen[day / 10] + chineseNumber[n];
	}

	// 节气计算
	/**
	 * 日期格式
	 */
	private static String formatData(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 计算节气
	 * 
	 * @param date1
	 * @return
	 */
	private static String getChineseTwentyFourDay(Date date1) {

		String[] SolarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
				"清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
				"白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };
		int[] sTermInfo = new int[] { 0, 21208, 42467, 63836, 85337, 107014,
				128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989,
				308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224,
				483532, 504758 };
		// Date baseDateAndTime = new Date(1900, 1, 6, 2, 5, 0); //#1/6/1900
		// 2:05:00 AM#
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 6, 2, 5, 0);// 月份从0开始
		final Date baseDateAndTime = cal.getTime();
		double num;
		int y;
		String tempStr = "";
		// 获取年份
		y = Integer.parseInt(formatData(date1, "yyyy"));

		for (int i = 1; i <= 24; i++) {
			num = 525948.76 * (y - 1900) + sTermInfo[i - 1];
			Calendar calendarMin = Calendar.getInstance();
			calendarMin.setTime(baseDateAndTime);
			// 添加分钟
			calendarMin.add(Calendar.MINUTE, (int) Math.ceil(num) + 5);
			// 对传递的天数进行处理
			Calendar calendarDate1 = Calendar.getInstance();
			calendarDate1.setTime(date1);

			// 对时间进行格式化字符串比较
			if (formatData(calendarMin.getTime(), "yyyy-MM-dd").trim().equals(
					formatData(calendarDate1.getTime(), "yyyy-MM-dd").trim())) {
				tempStr = SolarTerm[i - 1];
				break;
			}
		}
		return tempStr;
	}
	
	/**
	 * 获取年份
	 * 
	 * @return
	 */
	public int getYear() {
		return this.year;
	}
	
	
	
	
	
	//////////////////////////////////////////农历转公历///////////////////////////////////////////
	
	/**
     * 支持转换的最小农历年份
     */
    public static final int MIN_YEAR = 1900;
    /**
     * 支持转换的最大农历年份
     */
    public static final int MAX_YEAR = 2099;
    /**
     * 公历每月前的天数
     */
    private static final int DAYS_BEFORE_MONTH[] = { 0, 31, 59, 90, 120, 151, 181,
                    212, 243, 273, 304, 334, 365 };
    /**
     * 用来表示1900年到2099年间农历年份的相关信息，共24位bit的16进制表示，其中：
     * 1. 前4位表示该年闰哪个月；
     * 2. 5-17位表示农历年份13个月的大小月分布，0表示小，1表示大；
     * 3. 最后7位表示农历年首（正月初一）对应的公历日期。
     *
     * 以2014年的数据0x955ABF为例说明：
     *       1001 0101 0101 1010 1011 1111
     *       闰九月                                  农历正月初一对应公历1月31号       
     */
    private static final int LUNAR_INFO[] = {
        0x84B6BF,/*1900*/
        0x04AE53,0x0A5748,0x5526BD,0x0D2650,0x0D9544,0x46AAB9,0x056A4D,0x09AD42,0x24AEB6,0x04AE4A,/*1901-1910*/
        0x6A4DBE,0x0A4D52,0x0D2546,0x5D52BA,0x0B544E,0x0D6A43,0x296D37,0x095B4B,0x749BC1,0x049754,/*1911-1920*/
        0x0A4B48,0x5B25BC,0x06A550,0x06D445,0x4ADAB8,0x02B64D,0x095742,0x2497B7,0x04974A,0x664B3E,/*1921-1930*/
        0x0D4A51,0x0EA546,0x56D4BA,0x05AD4E,0x02B644,0x393738,0x092E4B,0x7C96BF,0x0C9553,0x0D4A48,/*1931-1940*/
        0x6DA53B,0x0B554F,0x056A45,0x4AADB9,0x025D4D,0x092D42,0x2C95B6,0x0A954A,0x7B4ABD,0x06CA51,/*1941-1950*/
        0x0B5546,0x555ABB,0x04DA4E,0x0A5B43,0x352BB8,0x052B4C,0x8A953F,0x0E9552,0x06AA48,0x6AD53C,/*1951-1960*/
        0x0AB54F,0x04B645,0x4A5739,0x0A574D,0x052642,0x3E9335,0x0D9549,0x75AABE,0x056A51,0x096D46,/*1961-1970*/
        0x54AEBB,0x04AD4F,0x0A4D43,0x4D26B7,0x0D254B,0x8D52BF,0x0B5452,0x0B6A47,0x696D3C,0x095B50,/*1971-1980*/
        0x049B45,0x4A4BB9,0x0A4B4D,0xAB25C2,0x06A554,0x06D449,0x6ADA3D,0x0AB651,0x095746,0x5497BB,/*1981-1990*/
        0x04974F,0x064B44,0x36A537,0x0EA54A,0x86B2BF,0x05AC53,0x0AB647,0x5936BC,0x092E50,0x0C9645,/*1991-2000*/
        0x4D4AB8,0x0D4A4C,0x0DA541,0x25AAB6,0x056A49,0x7AADBD,0x025D52,0x092D47,0x5C95BA,0x0A954E,/*2001-2010*/
        0x0B4A43,0x4B5537,0x0AD54A,0x955ABF,0x04BA53,0x0A5B48,0x652BBC,0x052B50,0x0A9345,0x474AB9,/*2011-2020*/
        0x06AA4C,0x0AD541,0x24DAB6,0x04B64A,0x6a573D,0x0A4E51,0x0D2646,0x5E933A,0x0D534D,0x05AA43,/*2021-2030*/
        0x36B537,0x096D4B,0xB4AEBF,0x04AD53,0x0A4D48,0x6D25BC,0x0D254F,0x0D5244,0x5DAA38,0x0B5A4C,/*2031-2040*/
        0x056D41,0x24ADB6,0x049B4A,0x7A4BBE,0x0A4B51,0x0AA546,0x5B52BA,0x06D24E,0x0ADA42,0x355B37,/*2041-2050*/
        0x09374B,0x8497C1,0x049753,0x064B48,0x66A53C,0x0EA54F,0x06AA44,0x4AB638,0x0AAE4C,0x092E42,/*2051-2060*/
        0x3C9735,0x0C9649,0x7D4ABD,0x0D4A51,0x0DA545,0x55AABA,0x056A4E,0x0A6D43,0x452EB7,0x052D4B,/*2061-2070*/
        0x8A95BF,0x0A9553,0x0B4A47,0x6B553B,0x0AD54F,0x055A45,0x4A5D38,0x0A5B4C,0x052B42,0x3A93B6,/*2071-2080*/
        0x069349,0x7729BD,0x06AA51,0x0AD546,0x54DABA,0x04B64E,0x0A5743,0x452738,0x0D264A,0x8E933E,/*2081-2090*/
        0x0D5252,0x0DAA47,0x66B53B,0x056D4F,0x04AE45,0x4A4EB9,0x0A4D4C,0x0D1541,0x2D92B5          /*2091-2099*/
    };
	
	
	/**
     * 将农历日期转换为公历日期
     * @param year               农历年份
     * @param month              农历月
     * @param monthDay           农历日
     * @param isLeapMonth        该月是否是闰月
     * @return 返回农历日期对应的公历日期，year0, month1, day2.
     */
    public static final int[] lunarToSolar(int year, int month, int monthDay,
                    boolean isLeapMonth) {
            int dayOffset;
            int leapMonth;
            int i;
            
            if (year < MIN_YEAR || year > MAX_YEAR || month < 1 || month > 12
                            || monthDay < 1 || monthDay > 30) {
                    throw new IllegalArgumentException(
                                    "Illegal lunar date, must be like that:\n\t" +
                                    "year : 1900~2099\n\t" +
                                    "month : 1~12\n\t" +
                                    "day : 1~30");
            }
            
            dayOffset = (LUNAR_INFO[year - MIN_YEAR] & 0x001F) - 1;

            if (((LUNAR_INFO[year - MIN_YEAR] & 0x0060) >> 5) == 2)
                    dayOffset += 31;

            for (i = 1; i < month; i++) {
                    if ((LUNAR_INFO[year - MIN_YEAR] & (0x80000 >> (i - 1))) == 0)
                            dayOffset += 29;
                    else
                            dayOffset += 30;
            }

            dayOffset += monthDay;
            leapMonth = (LUNAR_INFO[year - MIN_YEAR] & 0xf00000) >> 20;

            // 这一年有闰月
            if (leapMonth != 0) {
                    if (month > leapMonth || (month == leapMonth && isLeapMonth)) {
                            if ((LUNAR_INFO[year - MIN_YEAR] & (0x80000 >> (month - 1))) == 0)
                                    dayOffset += 29;
                            else
                                    dayOffset += 30;
                    }
            }

            if (dayOffset > 366 || (year % 4 != 0 && dayOffset > 365)) {
                    year += 1;
                    if (year % 4 == 1)
                            dayOffset -= 366;
                    else
                            dayOffset -= 365;
            }
            
            int[] solarInfo = new int[3];
            for (i = 1; i < 13; i++) {
                    int iPos = DAYS_BEFORE_MONTH[i];
                    if (year % 4 == 0 && i > 2) {
                            iPos += 1;
                    }

                    if (year % 4 == 0 && i == 2 && iPos + 1 == dayOffset) {
                            solarInfo[1] = i;
                            solarInfo[2] = dayOffset - 31;
                            break;
                    }

                    if (iPos >= dayOffset) {
                            solarInfo[1] = i;
                            iPos = DAYS_BEFORE_MONTH[i - 1];
                            if (year % 4 == 0 && i > 2) {
                                    iPos += 1;
                            }
                            if (dayOffset > iPos)
                                    solarInfo[2] = dayOffset - iPos;
                            else if (dayOffset == iPos) {
                                    if (year % 4 == 0 && i == 2)
                                            solarInfo[2] = DAYS_BEFORE_MONTH[i] - DAYS_BEFORE_MONTH[i - 1] + 1;
                                    else
                                            solarInfo[2] = DAYS_BEFORE_MONTH[i] - DAYS_BEFORE_MONTH[i - 1];

                            } else
                                    solarInfo[2] = dayOffset;
                            break;
                    }
            }
            solarInfo[0] = year;

            return solarInfo;
    }

}