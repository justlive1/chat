package cn.my.chat.conf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

/**
 * 缓存配置类
 * 
 * @author WB
 *
 */

@Configuration
@EnableCaching
public class CacheMangagerConfig {

	public static final String ONLINES = "onlines";
	
	@Bean
	@Primary
	public CacheManager caffeineCacheManager() {

		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
				.maximumSize(Integer.MAX_VALUE)
				.removalListener((key, graph, cause) -> {
					if (RemovalCause.EXPLICIT.equals(cause)) {
						// TODO do something when cache removed by user
						System.out.printf("Key %s was removed and value was [%s]%n", key, graph);
					}
				});

		cacheManager.setCaffeine(caffeine);

		return cacheManager;
	}
}
