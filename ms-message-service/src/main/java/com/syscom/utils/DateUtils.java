package com.syscom.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date convertLocalDateTimeToDate(LocalDateTime localDateTimeToConvert) {
		return java.util.Date.from(localDateTimeToConvert.atZone(ZoneId.systemDefault()).toInstant());
	}
}
