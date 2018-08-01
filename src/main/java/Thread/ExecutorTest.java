package Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorTest {
    public static void main(String[] args) {
//        new ThreadPoolExecutor();
        ExecutorService executor = Executors.newFixedThreadPool(3);

            for (int i=0;i<10;i++) {
                executor.submit(new CodingTask(i));
            }
            System.out.println("10 task successful");
        }

}

