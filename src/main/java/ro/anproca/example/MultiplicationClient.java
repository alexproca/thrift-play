package ro.anproca.example;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import ro.anproca.example.handlers.NotificationServiceHandler;
import tutorial.MultiplicationService;
import tutorial.NotificationService;
import tutorial.RegisteringClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 09.12.2014.
 */
public class MultiplicationClient {

    public static void main(String[] args) throws TException, InterruptedException {
        TTransport transport;

        transport = new TFramedTransport(new TSocket("localhost", 9090));

        TProtocol protocol = new TBinaryProtocol(transport);
        MultiplicationService.Client client = new MultiplicationService.Client(protocol);
        transport.open();

        int port = Integer.parseInt(args[0]);
        String clientName = args[1];
        int n1 = Integer.parseInt(args[2]), n2 = Integer.parseInt(args[3]);

        addSomeHandlers(client, port, clientName);

        perform(client, n1, n2);

        transport.close();
    }

    private static void addSomeHandlers(MultiplicationService.Client client, final int port, final String clientName) throws TException {

        NotificationServiceHandler notificationHandler = new NotificationServiceHandler();
        final NotificationService.Processor processor = new NotificationService.Processor(notificationHandler);

        final Runnable simple = new Runnable() {
            @Override
            public void run() {

                TNonblockingServerSocket serverTransport = null;
                try {
                    serverTransport = new TNonblockingServerSocket(port);
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
                TServer server = new THsHaServer(new THsHaServer.Args(serverTransport).processor(processor));

                System.out.println("Starting simple client server on port " + port);
                server.serve();

            }
        };

        new Thread(simple).start();

        client.registerClient(new RegisteringClient(port, clientName));

    }

    private static void perform(MultiplicationService.Client client, int n1, int n2) throws TException, InterruptedException {

        for (int i = 0; i <= 10000; i++) {

            long time = -System.nanoTime();
            int product = client.multiply(n1, n2);
            time += System.nanoTime();
            System.out.println(n1 + "*" + n2 + "=" + product + " in " + time + " nano");
            System.out.println(n1 + "*" + n2 + "=" + product + " in " + time / 1000 + " micro");
            System.out.println(n1 + "*" + n2 + "=" + product + " in " + time / 1000000 + " mili");

            TimeUnit.SECONDS.sleep(1);
        }
    }

}
