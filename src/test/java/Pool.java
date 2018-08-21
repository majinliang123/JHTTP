import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Pool {

    private static ExecutorService pool = Executors.newFixedThreadPool(13);

    public static void submit(Runnable runnable){
        pool.submit(runnable);
    }

    public static List<Future<Void>> submit(List<Callable<Void>> runnableList){
        List<Future<Void>> futures = null;
        try{
            futures = pool.invokeAll(runnableList);
        }catch (Exception e){}
        return futures;
    }
}
