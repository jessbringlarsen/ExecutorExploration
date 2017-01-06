package dk.bringlarsen.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TransferQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Consumer {


    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void consume(TransferQueue<Integer> queue) {
        final Runnable pullQueue = () -> {
            try {
                System.out.println("Got: " + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(pullQueue, 0, 1, SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
