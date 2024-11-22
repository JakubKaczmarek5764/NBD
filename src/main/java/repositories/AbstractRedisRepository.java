package repositories;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AbstractRedisRepository {
    private JedisPool jedisPool;
    public JedisPool initConnection(){
        if (jedisPool == null) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("redis.properties"));

                JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
                jedisPool = new JedisPool(props.getProperty("redis.host"), Integer.parseInt(props.getProperty("redis.port")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jedisPool;
    }
}
