package com.XPTO.TECGRAF.util;

import java.time.LocalDateTime;

public class StringUtil {

	public static String nowInString() {
		
		LocalDateTime now = LocalDateTime.now();
		String date = now.toLocalDate().toString();
		String time = now.getHour() + "-" + now.getMinute() + "-" + now.getSecond();
		
		return date + "-" + time;
	}
}
