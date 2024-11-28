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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static repositories.AbstractRedisRepository.initConnection;

public class RedisLiteratureRepository extends LiteratureRepository implements ILiteratureRepository {
    private final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new LiteratureAdapter(), new MongoUniqueIdAdapter()));
    private final static String hashPrefix = "literature:";
    private final JedisPooled jedisPooled = initConnection();
    private LiteratureRepository mongoLiteratureRepository;

    public RedisLiteratureRepository() {
        this.mongoLiteratureRepository = new LiteratureRepository();
    }

    public RedisLiteratureRepository(LiteratureRepository mongoLiteratureRepository) {
        if (mongoLiteratureRepository == null) {
            mongoLiteratureRepository = new LiteratureRepository();
        }
        this.mongoLiteratureRepository = mongoLiteratureRepository;
    }

    @Override
    public void create(Literature obj) {
        mongoLiteratureRepository.create(obj);
        createInCache(obj);
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
                Object literatureObj = jedisPooled.jsonGet(literatureId);
                String literatureJson = jsonb.toJson(literatureObj);
                literatures.add(jsonb.fromJson(literatureJson, Literature.class));
            }
            return literatures;
        } catch (JedisConnectionException | JsonbException e) {
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
            Literature lit;
            lit = mongoLiteratureRepository.getById(id);
            if (lit != null) {
                try {
                    createInCache(lit);
                } catch (Exception e1) {
                    return lit;
                }
            }
            return lit;
        }
    }

    @Override
    public void delete(Literature obj) {
        mongoLiteratureRepository.delete(obj);
        deleteFromCache(obj);
    }

    public void deleteFromCache(Literature obj) {
        try {
            jedisPooled.del(hashPrefix + obj.getLiteratureId().getId().toString());
        } catch (JsonbException | JedisConnectionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Literature obj) {
        mongoLiteratureRepository.update(obj);
        String id = obj.getLiteratureId().getId().toString();
        String literatureJson = jsonb.toJson(obj);
        jedisPooled.jsonSet(hashPrefix + id, literatureJson);
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
