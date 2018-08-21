import org.junit.Test;
import org.messtin.jhttp.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Test server performance
 */
public class PerformanceTest {

    private static final String url = "http://localhost:8080/hello";

    @Test
    public void test() throws Exception {
        Core.init(HelloServlet.class);
        long startTime = System.currentTimeMillis();
        List<Callable<Void>> callables = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            callables.add(() -> {
                try {
                    Util.get(url);
                } catch (Exception e) {

                }
                return null;
            });
        }

        List<Future<Void>> futrues = Pool.submit(callables);
        for(Future<Void> f : futrues){
            f.get();
        }
        System.out.println("Cost: " + (System.currentTimeMillis() - startTime));

    }
}

