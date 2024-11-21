package repositories;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;

public class AbstractRedisRepository {
    private JedisPool jedisPool;
    public JedisPool initConnection(){
        if (jedisPool == null) {
            jedisPool = new JedisPool("localhost", 6379);
        }
        return jedisPool;
    }
}
