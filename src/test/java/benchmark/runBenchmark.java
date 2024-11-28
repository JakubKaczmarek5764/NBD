package benchmark;

import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class runBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt1 = new OptionsBuilder()
                .include(RedisBenchmarkNoCache.class.getSimpleName())
                .include(RedisBenchmarkWithCache.class.getSimpleName())
                .forks(1).warmupIterations(1)
                .measurementIterations(1)
                .measurementTime(TimeValue.milliseconds(500))
                .build();
        new org.openjdk.jmh.runner.Runner(opt1).run();

    }
}
