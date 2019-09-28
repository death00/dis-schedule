package top.death00.dis.schedule.manager;

/**
 * @author death00
 * @date 2019/9/26 17:29
 */
public interface IRedisManager {

	//region set

	/**
	 * 向set中添加元素
	 */
	boolean sadd(String key, String value);

	/**
	 * set中是否存在value
	 */
	boolean sismember(String key, String value);

	/**
	 * 移除set中的元素
	 */
	void srem(String key, String value);

	//region

	//region string

	/**
	 * 设置字符串的值（如果不存在的话）
	 */
	boolean setNx(String key, String value);

	//endregion
}
