package cn.my.chat.conf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

/**
 * 缓存配置类<br>
 * dev环境下使用caffeine缓存<br>
 * pro环境下则有spring boot自动创建redis缓存
 * 
 * @author WB
 *
 */

@Configuration
@EnableCaching
public class CacheMangagerConfig {

	public static final String ONLINES = "onlines";

	@Bean
	@Profile("dev")
	public CacheManager caffeineCacheManager() {

		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(Integer.MAX_VALUE)
				.removalListener((key, graph, cause) -> {
					if (RemovalCause.EXPLICIT.equals(cause)) {
						// TODO do something when user disconnected
						System.out.printf("Key %s was removed and value was [%s]%n", key, graph);
					}else if(RemovalCause.REPLACED.equals(cause)){
						// TODO do something when user login again
						System.out.printf("Key %s was replaced and value was [%s]%n", key, graph);
					}
				});

		cacheManager.setCaffeine(caffeine);

		return cacheManager;
	}
}
