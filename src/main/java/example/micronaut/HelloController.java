package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Controller
public class HelloController {

	private RedisRepository redisRepository;

	@Inject
	public HelloController(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Get("/hello")
	public CompletableFuture<String> getHello() {
		return redisRepository.getValue()
				.thenApply(value -> value.orElse("missing"));
	}
}
