package example.micronaut;

import io.micronaut.configuration.lettuce.cache.RedisCache;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Singleton
public class RedisRepository {

	private RedisCache redisCache;

	@Inject
	public RedisRepository(RedisCache redisCache) {
		this.redisCache = redisCache;
	}

	public CompletableFuture<Optional<String>> getValue() {
		return redisCache.async().get("key", String.class);
	}
}
