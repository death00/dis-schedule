package top.death00.dis.schedule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import top.death00.dis.schedule.constant.DisScheduleUnit;

/**
 * 在方法执行之前，决定当前是否需要执行定时调度任务
 *
 * @author death00
 * @date 2019/9/24 18:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisSchedule {

	/**
	 * 定时调度任务的名称(默认是方法名)
	 */
	String name() default "";

	/**
	 * 任务的间隔时间
	 */
	int duration();

	/**
	 * duration的时间单位(默认：分钟)
	 */
	DisScheduleUnit unit() default DisScheduleUnit.MINUTES;
}
