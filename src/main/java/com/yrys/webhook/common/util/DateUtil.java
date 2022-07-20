package com.yrys.webhook.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public final class DateUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static final String HONG_KONG_TIMEZONE = "Asia/Hong_Kong";
	
	public static final String IRN_VERSION_FORMAT = "yyyyMMdd";
	
	public static final String FILE_NAME_FORMAT = "yyyyMMdd";
	
	public static final String TIME_SHORT_FORMAT = "HH:mm";

	public static Date getCurrentDate() throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		final String currentDateStr = formatter.format(new Date());
		return formatter.parse(currentDateStr);
	}

	public static String toString(Date date) throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		// formatter.setTimeZone(TimeZone.getTimeZone(HONG_KONG_TIMEZONE));
		return formatter.format(date);
	}
}
