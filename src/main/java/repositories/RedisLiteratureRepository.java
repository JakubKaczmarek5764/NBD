package repositories;

import com.mongodb.client.MongoCollection;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import mappers.LiteratureAdapter;
import mappers.MongoUniqueId;
import mappers.MongoUniqueIdAdapter;
import objects.Literature;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisLiteratureRepository extends AbstractRedisRepository implements ILiteratureRepository {
    private final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new LiteratureAdapter(), new MongoUniqueIdAdapter()));
    private final static String hashPrefix = "literature:";
    private final JedisPooled jedisPooled = initConnection();
    private LiteratureRepository mongoLiteratureRepository;

    public RedisLiteratureRepository(LiteratureRepository mongoLiteratureRepository) {
        this.mongoLiteratureRepository = mongoLiteratureRepository;
    }

    @Override
    public void create(Literature obj) {
        mongoLiteratureRepository.create(obj);
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
        System.out.println("\n\n\n costam");
        System.out.println(literatureJson);
        System.out.println("\n\n\n");

        jedisPooled.jsonSet(hashPrefix + id, literatureJson);
        jedisPooled.expire(hashPrefix + id, 3600);
    }

    @Override
    public List<Literature> getAll() {
        try {
            List<Literature> literatures = new ArrayList<>();

            Set<String> keys = jedisPooled.keys(hashPrefix + "*");

            for (String literatureId : keys) {
                System.out.println(literatureId);
                Object literatureObj = jedisPooled.jsonGet(literatureId);
                String literatureJson = jsonb.toJson(literatureObj);
                System.out.println(literatureJson);
                System.out.println("tutaj?");
                literatures.add(jsonb.fromJson(literatureJson, Literature.class));
                System.out.println("\n\n\n\n got from redis getall \n\n\n\n");
            }
            return literatures;
        } catch (Exception e) { // to takie robocze chyba rozwiazanie Zaimplementuj metodę, która pobierze dane z bazy danych MongoDB w przypadku utraty połączenia z Redisem.
            System.out.println("\n\n\n\nerror message");

            System.out.println(e.getMessage());
            return mongoLiteratureRepository.getAll();
        }
    }

    @Override
    public Literature getById(MongoUniqueId id) {
        try {
            String literatureJson = jedisPooled.get(hashPrefix + id.getId().toString());
            System.out.println("\n\n\n\n got from redis \n\n\n\n");
            System.out.println(literatureJson);
            return jsonb.fromJson(literatureJson, Literature.class);
        } catch (Exception e) { // to tez
            return mongoLiteratureRepository.getById(id);
        }

    }

    @Override
    public void delete(Literature obj) {
        mongoLiteratureRepository.delete(obj);
        jedisPooled.del(hashPrefix + obj.getLiteratureId().getId().toString());
    }

    @Override
    public void update(Literature obj) {
        mongoLiteratureRepository.update(obj);
        // ciekawe jak zrobic update
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
        jedisPooled.jsonSet(hashPrefix + id, literatureJson);
        // chyba po prostu tak ^-^
    }

    @Override
    public void emptyCollection() {
        mongoLiteratureRepository.emptyCollection();
//            jedisPool.del(hashPrefix + "*");
        jedisPooled.flushAll();
    }

    public void clearCache() {
        jedisPooled.flushAll();
    }

    @Override
    public MongoCollection<Literature> getLiteratureCollection() {
        return mongoLiteratureRepository.getLiteratureCollection();
    }
}
