package main;

import enumerator.tableenumerator.AbstractTableEnumerator;
import enumerator.tableenumerator.AggrHueristicTableEnumerator;
import enumerator.tableenumerator.CanonicalTableEnumerator;
import enumerator.tableenumerator.PlainTableEnumerator;
import org.junit.Test;
import sql.lang.ast.table.TableNode;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Created by clwang on 3/22/16.
 */
public class SynthesizerTest {

    public void test() {
        Synthesizer.Synthesize("data//StackOverflow//001", 2, new AggrHueristicTableEnumerator());
    }

    @Test
    public void synthesize() throws Exception {

        List<AbstractTableEnumerator> enumerators = new ArrayList<>();
        enumerators.add(new PlainTableEnumerator());
        enumerators.add(new CanonicalTableEnumerator());
        enumerators.add(new AggrHueristicTableEnumerator());

        // testing with time limit
        File dir = new File("data//StackOverflow");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File f : directoryListing) {
                if (f.isFile()) {
                    if (f.getName().endsWith("020")) continue;
                    System.out.println("\n[[~~~~~~~~]]");
                    for (AbstractTableEnumerator enumerator : enumerators) {
                        //Synthesizer.Synthesize(f.getPath(), enumerator);

                        final Duration timeout = Duration.ofMinutes(10);
                        ExecutorService executor = Executors.newSingleThreadExecutor();

                        final Future<List<TableNode>> handler = executor.submit(new Callable<List<TableNode>>() {
                            @Override
                            public List<TableNode> call() throws Exception {
                                return Synthesizer.Synthesize(f.getPath(), 2, enumerator);
                            }
                        });

                        try {
                            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                        } catch (TimeoutException e) {
                            handler.cancel(true);
                        }

                        executor.shutdownNow();
                    }
                }
            }
        }
    }

}