package kis.jmhbench;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import kis.jmhbench.MyBenchmark.Row;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *
 * @author naoki
 */
@State(Scope.Thread)
public class PebbleBench {
    PebbleEngine engine;
    HashMap ctx;
    
    @Setup
    public void setup() {
        engine = new PebbleEngine.Builder().build();
        ctx = new HashMap();
        ctx.put("description", "テンプレートのテストをします");
        ctx.put("data", Arrays.asList(
                new Row("最初", 123, "これ"),
                new Row("真ん中", 2345, "それ"),
                new Row("最後", 45678, "どれ")));        
    }
    
    @Benchmark
    public void test() throws PebbleException, IOException {
        PebbleTemplate template = engine.getTemplate("html/home.pebble");
        StringWriter sw = new StringWriter();
        template.evaluate(sw, ctx);
        sw.toString();
    }
    public static void main(String[] args) throws PebbleException, IOException, RunnerException {
        /*
        PebbleBench bench = new PebbleBench();
        bench.setup();
        PebbleTemplate template = bench.engine.getTemplate("html/home.pebble");
        StringWriter sw = new StringWriter();
        template.evaluate(sw, bench.ctx);
        System.out.println(sw);
         */
        Options opt = new OptionsBuilder()
                .include(PebbleBench.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
