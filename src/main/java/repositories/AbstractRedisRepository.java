package repositories;

import redis.clients.jedis.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AbstractRedisRepository {
    private JedisPooled jedisPool;
    public JedisPooled initConnection(){
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
