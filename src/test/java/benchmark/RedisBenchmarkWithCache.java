package benchmark;

import mappers.MongoUniqueId;
import objects.Book;
import org.bson.types.ObjectId;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import repositories.RedisLiteratureRepository;

@State(Scope.Benchmark)
public class RedisBenchmarkWithCache {
    RedisLiteratureRepository redisLiteratureRepository;
    Book b;


    @Setup
    public void setup(){
        redisLiteratureRepository = new RedisLiteratureRepository();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 1);
        redisLiteratureRepository.create(b);

    }

    @Benchmark
    public void readWithCache(){
        redisLiteratureRepository.getById(b.getLiteratureId());
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(RedisBenchmarkWithCache.class.getSimpleName()).forks(1).build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
