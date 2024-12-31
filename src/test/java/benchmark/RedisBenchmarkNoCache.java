package benchmark;

import mappers.MongoUniqueId;
import objects.Book;
import org.bson.types.ObjectId;
import org.openjdk.jmh.annotations.*;
import repositories.LiteratureRepository;
import repositories.RedisLiteratureRepository;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime}) // Measures Throughput and Average Time
@State(Scope.Benchmark)
public class RedisBenchmarkNoCache {
    RedisLiteratureRepository redisLiteratureRepository;
    LiteratureRepository literatureRepository;
    Book b;


    @Setup
    public void setup() {
        literatureRepository = new LiteratureRepository();
        redisLiteratureRepository = new RedisLiteratureRepository();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 1);
        redisLiteratureRepository.create(b);
        redisLiteratureRepository.clearCache();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS) // Results in milliseconds
    public void readNoCacheAvgTime() {
        literatureRepository.getById(b.getLiteratureId());
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void readNoCacheThroughput() {
        literatureRepository.getById(b.getLiteratureId());
    }
}
