package top.death00.dis.schedule.manager;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

/**
 * @author death00
 * @date 2019/10/9 15:52
 */
public class RedissonManagerImpl implements IRedisManager {

	private final RedissonClient redissonClient;

	public RedissonManagerImpl(RedissonClient redissonClient) {
		Preconditions.checkNotNull(redissonClient);
		this.redissonClient = redissonClient;
	}

	@Override
	public boolean sadd(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		RSet<String> set = redissonClient.getSet(key);
		return set.add(value);
	}

	@Override
	public boolean sismember(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		RSet<String> set = redissonClient.getSet(key);
		return set.contains(value);
	}

	@Override
	public void srem(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		RSet<String> set = redissonClient.getSet(key);
		set.remove(value);
	}

	@Override
	public boolean setNx(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		RBucket<String> rBucket = redissonClient.getBucket(key);
		return rBucket.trySet(value);
	}
}
