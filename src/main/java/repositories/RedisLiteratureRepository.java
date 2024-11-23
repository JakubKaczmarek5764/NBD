package repositories;

import com.mongodb.client.MongoCollection;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import mappers.MongoUniqueId;
import objects.Literature;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class RedisLiteratureRepository extends AbstractRedisRepository implements ILiteratureRepository {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final static String hashPrefix = "literature:";
    private final JedisPooled jedisPool = initConnection();
    private LiteratureRepository mongoLiteratureRepository;

    public RedisLiteratureRepository(LiteratureRepository mongoLiteratureRepository) {
        this.mongoLiteratureRepository = mongoLiteratureRepository;
    }

    @Override
    public void create(Literature obj) {
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
        jedisPool.jsonSet(hashPrefix + id, literatureJson);
    }

    @Override
    public List<Literature> getAll() {
        List<Literature> literatures = new ArrayList<>();
        String cursor = "0";
        do {
            ScanResult<String> result = jedisPool.scan(cursor, new ScanParams().match(hashPrefix + "*"));
            cursor = result.getCursor();
            for (String literatureJson : result.getResult()) {
                literatures.add(jsonb.fromJson(literatureJson, Literature.class));
            }
        } while (!cursor.equals("0"));
        return literatures;
    }

    @Override
    public Literature getById(MongoUniqueId id) {
        String literatureJson = jedisPool.get(hashPrefix + id.getId().toString());
        return jsonb.fromJson(literatureJson, Literature.class);
    }

    @Override
    public void delete(Literature obj) {
            jedisPool.del(hashPrefix + obj.getLiteratureId().getId().toString());
    }

    @Override
    public void update(Literature obj) {
        // ciekawe jak zrobic update
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
        jedisPool.jsonSet(hashPrefix + id, literatureJson);
        // chyba po prostu tak ^-^
    }

    @Override
    public void emptyCollection() {
//            jedisPool.del(hashPrefix + "*");
        jedisPool.flushAll();
    }

    @Override
    public MongoCollection<Literature> getLiteratureCollection() {
        return null;
    }
}
