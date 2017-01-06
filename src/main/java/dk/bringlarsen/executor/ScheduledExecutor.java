package dk.bringlarsen.executor;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ScheduledExecutor {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final TransferQueue<Integer> queue = new LinkedTransferQueue<>();
    private final Consumer consumer = new Consumer();
    private final Producer producer = new Producer();

    public void process() {
        scheduleProcessor();
        scheduleShutdown();
    }

    private void scheduleProcessor() {
        final Runnable process = () -> {
            try {
                producer.produce(queue);
                consumer.consume(queue);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(process, 0, 5, SECONDS);
    }

    private void scheduleShutdown() {
        final Runnable cancel = () -> {
            producer.shutdown();
            consumer.shutdown();
            scheduler.shutdown();
        };
        scheduler.scheduleAtFixedRate(cancel, 10, 1, SECONDS);
    }

    public static void main(String[] args) {
        new ScheduledExecutor().process();
    }
}
