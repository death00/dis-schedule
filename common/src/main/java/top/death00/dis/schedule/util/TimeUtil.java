package top.death00.dis.schedule.util;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author death00
 * @date 2019/9/24 18:51
 */
public class TimeUtil {

	/**
	 * 线程安全的详细时间日期formatter
	 */
	private static final DateTimeFormatter SPECIFIC_DATE_TIME_FORMATTER =
		DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
			.withZone(DateTimeZone.getDefault());

	/**
	 * 线程安全的日期formatter
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormat.forPattern("yyyy-MM-dd")
			.withZone(DateTimeZone.getDefault());

	/**
	 * 获得当前日期date
	 */
	public static Date getCurDate() {
		return new Date();
	}

	/**
	 * 根据date，取millis的开始时间，再减去millis
	 */
	public static Date getMillisDate(Date date, int millis) {
		Preconditions.checkNotNull(date);
		Preconditions.checkArgument(millis > 0);

		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.minusMillis(dateTime.getMillisOfDay() % millis)
			.minusMillis(millis);

		return dateTime.toDate();
	}

	/**
	 * 根据时间戳，获得"yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public static String specialFormatToDateStr(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(SPECIFIC_DATE_TIME_FORMATTER);
	}

	/**
	 * 根据date， 获得"yyyy-MM-dd"格式的字符串
	 */
	public static String formatToDateStr(Date date) {
		Preconditions.checkNotNull(date);

		DateTime dateTime = new DateTime(date);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}
}
