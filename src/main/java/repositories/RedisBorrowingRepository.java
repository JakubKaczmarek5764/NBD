package repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import mappers.MongoUniqueId;
import objects.Borrowing;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class RedisBorrowingRepository extends AbstractRedisRepository implements IBorrowingRepository {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final static String prefix = "borrowing:";
    private final JedisPool jedisPool = initConnection();
    @Override
    public void create(Borrowing obj) {
        String id = obj.getBorrowingId().getId().toString();
        String borrowingJson = jsonb.toJson(obj);
        try (Jedis jedis = jedisPool.getResource()) { // to mi intellij wygenerowal xp
            jedis.set(prefix + id, borrowingJson, SetParams.setParams().ex(10));
        }
    }

    @Override
    public List<Borrowing> getAll() {
        List<Borrowing> borrowings = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            String cursor = "0";
            do {
                ScanResult<String> result = jedis.scan(cursor, new ScanParams().match(prefix + "*"));
                cursor = result.getCursor();
                for (String borrowingJson : result.getResult()) {
                    borrowings.add(jsonb.fromJson(borrowingJson, Borrowing.class));
                }
            } while (!cursor.equals("0"));
        }
        return borrowings;
    }

    @Override
    public Borrowing getById(MongoUniqueId id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String borrowingJson = jedis.get(prefix + id.getId().toString());
            return jsonb.fromJson(borrowingJson, Borrowing.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Borrowing obj) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(prefix + obj.getBorrowingId().getId().toString());
        }
    }

    @Override
    public void update(Borrowing obj) {
        // ciekawe jak zrobic update

    }

    @Override
    public void emptyCollection() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(prefix + "*");
        }
    }
}
