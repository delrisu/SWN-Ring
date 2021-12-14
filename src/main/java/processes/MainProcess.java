package processes;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MainProcess implements Runnable {

    private final List<String> received;
    private final List<String> toSend;
    private int current = 0;

    public MainProcess(List<String> received, List<String> toSend, boolean starter) {
        this.received = received;
        this.toSend = toSend;
        if (starter) {
            this.received.add("0");
        }
    }


    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            List<String> receivedCopy;
            synchronized (received) {
                receivedCopy = new ArrayList<>(received);
                received.notifyAll();
            }
            if (receivedCopy.size() == 1) {
                if (Integer.parseInt(received.get(0)) >= current) {
                    current = Integer.parseInt(received.get(0)) + 1;

                    log.info(Thread.currentThread().getId() + " Received token with higher number: " + (current - 1));

                    Thread.sleep(500);

                    synchronized (toSend) {
                        if (toSend.size() == 1) {
                            toSend.set(0, String.valueOf(current));
                        } else {
                            toSend.add(String.valueOf(current));
                        }
                        toSend.notifyAll();
                    }
                }
            }
            Thread.sleep(5);
        }
    }
}
