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
    private final ZMQ.Socket socket;

    public Subscriber(String host, ZContext context, List<String> token) {
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
                if (token.size() != 0) {
                    if (!token.get(0).equals(message)) {
                        token.set(0, message);
                    }
                } else {
                    token.add(message);
                }
                token.notify();
            }
            Thread.sleep(5);
        }
        this.socket.close();
    }
}
