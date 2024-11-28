package repositories;

import com.mongodb.client.MongoCollection;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import mappers.LiteratureAdapter;
import mappers.MongoUniqueId;
import mappers.MongoUniqueIdAdapter;
import objects.Literature;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;
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

    public RedisLiteratureRepository(){
        this.mongoLiteratureRepository = new LiteratureRepository();
    }
    public RedisLiteratureRepository(LiteratureRepository mongoLiteratureRepository) {
        if (mongoLiteratureRepository == null){
            mongoLiteratureRepository = new LiteratureRepository();
        }
        this.mongoLiteratureRepository = mongoLiteratureRepository;
    }

    @Override
    public void create(Literature obj) {
        try {
            mongoLiteratureRepository.create(obj);
            createInCache(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInCache(Literature obj) {
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
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
                literatures.add(jsonb.fromJson(literatureJson, Literature.class));
                System.out.println("\n\n\n\n got from redis getall \n\n\n\n");
            }
            return literatures;
        } catch (JedisConnectionException | JsonbException e) { // to takie robocze chyba rozwiazanie Zaimplementuj metodę, która pobierze dane z bazy danych MongoDB w przypadku utraty połączenia z Redisem.
            System.out.println("\n\n\n\nerror message");
            System.out.println(e.getMessage());
            return mongoLiteratureRepository.getAll();
        }
    }

    @Override
    public Literature getById(MongoUniqueId id) {
        try {
            Object literatureObj = jedisPooled.jsonGet(hashPrefix + id.getId().toString());
            String literatureJson = jsonb.toJson(literatureObj);
            return jsonb.fromJson(literatureJson, Literature.class);
        } catch (JedisConnectionException | JsonbException e) { // to tez
            System.out.println("\n\n\ncoswcatch");
            Literature lit;
            lit = mongoLiteratureRepository.getById(id);
            if (lit != null) {
                createInCache(lit);
            }
            return lit;
        }
    }

    @Override
    public void delete(Literature obj) {
        try {
            mongoLiteratureRepository.delete(obj);
            deleteFromCache(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteFromCache(Literature obj){
        try {
            jedisPooled.del(hashPrefix + obj.getLiteratureId().getId().toString());
        } catch (JsonbException | JedisConnectionException e) {
            System.out.println("error deleting from cache");
            System.out.println(e.getMessage());
        }

    }
    @Override
    public void update(Literature obj) {
        try {
            mongoLiteratureRepository.update(obj);
            // ciekawe jak zrobic update
            String id = obj.getLiteratureId().getId().toString();
            String literatureJson = jsonb.toJson(obj);
            jedisPooled.jsonSet(hashPrefix + id, literatureJson);
            // chyba po prostu tak ^-^
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void emptyCollection() {
        try {
            mongoLiteratureRepository.emptyCollection();
            jedisPooled.del(hashPrefix + "*");
            clearCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        try {
            jedisPooled.flushAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MongoCollection<Literature> getLiteratureCollection() {
        try {
            return mongoLiteratureRepository.getLiteratureCollection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
