package benchmark;

import objects.Book;
import org.bson.types.ObjectId;
import org.openjdk.jmh.annotations.*;
import repositories.RedisLiteratureRepository;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class RedisBenchmarkWithCache {
    RedisLiteratureRepository redisLiteratureRepository;
    Book b;


    @Setup
    public void setup() {
        redisLiteratureRepository = new RedisLiteratureRepository();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 1);
        redisLiteratureRepository.create(b);

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS) // Results in milliseconds
    public void readWithCacheAvgTime() {
        redisLiteratureRepository.getById(b.getLiteratureId());
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void readWithCacheThroughput() {
        redisLiteratureRepository.getById(b.getLiteratureId());
    }


}
