package dk.bringlarsen.executor;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TransferQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Producer {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void produce(TransferQueue<Integer> queue) {
        // The Runnable is wraped in a try catch otherwise the scheduler will terminale and no additional
        // jobs will be executed!
        final Runnable beeper = () -> {
            try {
                boolean queueEmpty = queue.isEmpty();
                System.out.println("Queue is empty: " + queueEmpty);
                if (queueEmpty) {
                    fillQueue(queue);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(beeper, 0, 2, SECONDS);
    }

    private void fillQueue(TransferQueue<Integer> queue) {
        queue.addAll(Arrays.asList(1, 2, 3));
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
