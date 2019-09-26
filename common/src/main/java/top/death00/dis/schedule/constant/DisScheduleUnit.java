package top.death00.dis.schedule.constant;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
 * 时间单位
 *
 * @author death00
 * @date 2019/9/24 18:39
 */
@Getter
public enum DisScheduleUnit {

	// 毫秒
	MILLISECONDS(TimeUnit.MILLISECONDS),

	// 秒
	SECONDS(TimeUnit.SECONDS),

	// 分钟
	MINUTES(TimeUnit.MINUTES);

	private TimeUnit unit;

	private DisScheduleUnit(TimeUnit unit) {
		Preconditions.checkNotNull(unit);
		this.unit = unit;
	}
}
