package cn.my.chat.conf;

import java.util.List;
import java.util.Set;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import cn.my.chat.core.CacheManagerForwarding;
import lombok.Data;

/**
 * 缓存配置类<br>
 * dev环境下使用caffeine缓存<br>
 * pro环境下使用redisson缓存
 * 
 * @author WB
 *
 */

@Configuration
@EnableConfigurationProperties
@EnableCaching
public class CacheMangagerConfig {

	public static final String ONLINES = "onlines";

	@Bean
	@Profile("dev")
	public CacheManagerForwarding caffeineCacheManager() {

		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(Integer.MAX_VALUE)
				.removalListener((key, graph, cause) -> {
					if (RemovalCause.EXPLICIT.equals(cause)) {
						// TODO do something when user disconnected
						System.out.printf("Key %s was removed and value was [%s]%n", key, graph);
					} else if (RemovalCause.REPLACED.equals(cause)) {
						// TODO do something when user login again
						System.out.printf("Key %s was replaced and value was [%s]%n", key, graph);
					}
				});

		cacheManager.setCaffeine(caffeine);

		return new CacheManagerForwarding() {
			@Override
			public CacheManager delegate() {
				return cacheManager;
			}
			@Override
			public Set<Object> cacheKeys(String name) {
				return ((CaffeineCache)cacheManager.getCache(name)).getNativeCache().asMap().keySet();
			}
		};
	}
	
	

	@Bean
	@Profile("pro")
	public RedissonClient redisson(RedissonProperties props) {

		Config config = new Config();

		if (props.isSingle) {

			SingleServerConfig singleConfig = config.useSingleServer();
			singleConfig.setAddress(props.address);

		} else {
			ClusterServersConfig clusterConfig = config.useClusterServers();
			clusterConfig.setScanInterval(2000);
			clusterConfig.addNodeAddress(props.cluster.nodes.toArray(new String[]{}));
		}
		return Redisson.create(config);
	}

	@Bean(initMethod = "afterPropertiesSet")
	@Profile("pro")
	public CacheManagerForwarding redissonCacheManager(RedissonClient redisson) {

		RedissonSpringCacheManager cacheManager = new RedissonSpringCacheManager();
		cacheManager.setRedisson(redisson);

		return new CacheManagerForwarding() {
			@Override
			public CacheManager delegate() {
				return cacheManager;
			}
			@Override
			public Set<?> cacheKeys(String name) {
				return ((RedissonCache)cacheManager.getCache(name)).getNativeCache().keySet();
			}
		};
	}

	@Data
	@Component
	@Profile("pro")
	@ConfigurationProperties(prefix="spring.redisson")
	static class RedissonProperties {
		
		private boolean isSingle;
		private String address;
		
		private Cluster cluster;

		@Data
		public static class Cluster {
			private List<String> nodes;
		}
	}
}
