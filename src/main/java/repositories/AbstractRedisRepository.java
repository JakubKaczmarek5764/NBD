package repositories;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.util.Properties;

public class AbstractRedisRepository {
    private static JedisPooled jedisPool;

    public static JedisPooled initConnection() {
        if (jedisPool == null) {
            try {
                Properties props = new Properties();
                props.load(AbstractRedisRepository.class.getClassLoader().getResourceAsStream("redis.properties"));

                JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
                jedisPool = new JedisPooled(props.getProperty("redis.host"), Integer.parseInt(props.getProperty("redis.port")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jedisPool;
    }
}
