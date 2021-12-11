package processes;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import zmq.Publisher;
import zmq.Subscriber;

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
            this.received.add("1");
        }
    }


    @SneakyThrows
    @Override
    public void run() {
        if (received.size() == 1) {
            if (Integer.parseInt(received.get(0)) > current) {
                current = Integer.parseInt(received.get(0)) + 1;
                Thread.sleep(5000);
                if(toSend.size()==1){
                    toSend.set(0, String.valueOf(current));
                } else {
                    toSend.add(String.valueOf(current));
                }
            }
            Thread.sleep(5);
        }
    }
}
