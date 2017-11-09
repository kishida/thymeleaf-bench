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
    HashMap ctxen;

    PebbleTemplate template;
    PebbleTemplate templateen;
    
    @Setup
    public void setup() throws PebbleException {
        engine = new PebbleEngine.Builder().build();
        ctx = new HashMap();
        ctx.put("description", "テンプレートのテストをします");
        ctx.put("data", Arrays.asList(
                new Row("最初", 123, "これ"),
                new Row("真ん中", 2345, "それ"),
                new Row("最後", 45678, "どれ")));        
        ctxen = new HashMap();
        ctxen.put("description", "Test a template");
        ctxen.put("data", Arrays.asList(
                new Row("first", 123, "this"),
                new Row("middle", 2345, "that"),
                new Row("last", 45678, "whot")));        
        template = engine.getTemplate("html/home.pebble");
        templateen = engine.getTemplate("html/homeen.pebble");
    }
    
    @Benchmark
    public void test() throws PebbleException, IOException {
        StringWriter sw = new StringWriter();
        template.evaluate(sw, ctx);
        sw.toString();
    }

    @Benchmark
    public void testen() throws PebbleException, IOException {
        StringWriter sw = new StringWriter();
        template.evaluate(sw, ctxen);
        sw.toString();
    }
    public static void main(String[] args) throws PebbleException, IOException, RunnerException {
        if (false) {
            PebbleBench bench = new PebbleBench();
            bench.setup();
            PebbleTemplate template = bench.engine.getTemplate("html/homeen.pebble");
            StringWriter sw = new StringWriter();
            template.evaluate(sw, bench.ctxen);
            System.out.println(sw);
            System.exit(0);
        }
         
        Options opt = new OptionsBuilder()
                .include(PebbleBench.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
