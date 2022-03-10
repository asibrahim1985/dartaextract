package com.atos.bioscore.GetBioScoreBoot.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ApiUtils {

	private static final String UTC_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public String getUTCCurrentDateTime() {
		LocalDateTime  ldt = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
		return ldt.format(DateTimeFormatter.ofPattern(UTC_DATETIME_PATTERN));
	}
}
