package structures;

import lombok.SneakyThrows;
import org.zeromq.ZContext;
import processes.MainProcess;
import zmq.Publisher;
import zmq.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final List<String> received;
    private final List<String> toSend;

    private final Publisher publisher;
    private final Subscriber subscriber;
    private final MainProcess mainProcess;

    @SneakyThrows
    public Node(String subscriberHost, String publisherPort, boolean starter) {
        received = new ArrayList<>();
        toSend = new ArrayList<>();
        ZContext context = new ZContext();
        publisher = new Publisher(publisherPort, context, toSend);
        subscriber = new Subscriber(subscriberHost, context, received);
        mainProcess = new MainProcess(received, toSend, starter);

        new Thread(publisher).start();
        new Thread(subscriber).start();

        Thread.sleep(1000);
        new Thread(mainProcess).start();
    }
}
