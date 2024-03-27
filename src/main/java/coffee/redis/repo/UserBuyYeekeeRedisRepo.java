package coffee.redis.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserBuyYeekeeRedisRepo {

	@SuppressWarnings("rawtypes")
	private HashOperations hashOperations;
	private final String key = "UserBuyYeekee:";
	private StringRedisTemplate redisTemplate;

	public UserBuyYeekeeRedisRepo(@Qualifier(value = "redisUserBuyYeekeeTemplate") StringRedisTemplate redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();
		this.redisTemplate = redisTemplate;
	}

	public boolean hasUsername(String username, int round) {
		String keySave = key + username + String.valueOf(round);
		Map<String, String> value = hashOperations.entries(keySave);
		return value.size() > 0;
	}

	public void setUser(String username, int round) {
		String keySave = key + username + String.valueOf(round);
		Map<String, String> value = new HashMap<String, String>();
		value.put(keySave, username);
		redisTemplate.opsForHash().putAll(keySave, value);
		redisTemplate.expire(keySave, 10, TimeUnit.SECONDS);
	}

}
