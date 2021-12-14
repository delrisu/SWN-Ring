package zmq;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.List;

@Slf4j
public class Publisher implements Runnable {

    private final List<String> token;
    private final ZMQ.Socket socket;

    public Publisher(String port, ZContext context, List<String> token) {
        this.socket = context.createSocket(SocketType.PUB);
        this.token = token;
        socket.bind("tcp://*:" + port);
        log.info("Created publisher with port: " + port);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (token) {
                if (token.size() == 1) {
                    socket.send(token.get(0));
                }
                token.notifyAll();
            }
            Thread.sleep(200);
        }
        this.socket.close();
    }
}
