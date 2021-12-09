package zmq;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.List;

@Slf4j
public class Subscriber implements Runnable {
    private final List<String> token;
    private ZMQ.Socket socket;

    public Subscriber(List<String> token, ZContext context, String host) {
        this.token = token;
        this.socket = context.createSocket(SocketType.SUB);

        socket.connect("tcp://" + host);
        socket.subscribe(ZMQ.SUBSCRIPTION_ALL);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String message = socket.recvStr();
            synchronized (token) {
                token.set(0, message);
                token.notify();
            }
            Thread.sleep(5);
        }
        this.socket.close();
    }
}
