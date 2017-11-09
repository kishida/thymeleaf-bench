package kis.jmhbench;

import java.util.Arrays;

import lombok.Value;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@State(Scope.Thread)
public class MyBenchmark {

    @Value
    static class Row {
        String name;
        int value;
        String description;
    }
    
    TemplateEngine engine;
    Context ctx;
    Context ctxen;

    @Setup
    public void setup() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("html/");
        resolver.setSuffix(".html");
        //resolver.setCacheable(false);
        
        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        
        ctx = new Context();
        ctx.setVariable("description", "テンプレートのテストをします");
        ctx.setVariable("data", Arrays.asList(
                new Row("最初", 123, "これ"),
                new Row("真ん中", 2345, "それ"),
                new Row("最後", 45678, "どれ")));
        ctxen = new Context();
        ctxen.setVariable("description", "Test a template");
        ctxen.setVariable("data", Arrays.asList(
                new Row("first", 123, "this"),
                new Row("middle", 2345, "that"),
                new Row("last", 45678, "whot")));        
        
    }
    
    @Benchmark
    public void test() {
        engine.process("home", ctx);
    }
    @Benchmark
    public void testen() {
        engine.process("homeen", ctxen);
    }
    public static void main(String[] args) throws RunnerException {
        if (false) {
            MyBenchmark b = new MyBenchmark();
            b.setup();
            System.out.println(b.engine.process("homeen", b.ctxen));

            System.exit(0);
        }
        Options opt = new OptionsBuilder()
                .forks(1)
                .include(MyBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
