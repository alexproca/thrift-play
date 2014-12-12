package ro.anproca.example;

import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import ro.anproca.example.handlers.MultiplicationHandler;
import tutorial.MultiplicationService;

/**
 * Created by alex on 09.12.2014.
 */
public class MultiplicationServer {

    private static MultiplicationHandler handler;
    private static MultiplicationService.Processor processor;

    public static void main(String[] args) {
        handler = new MultiplicationHandler();
        processor = new MultiplicationService.Processor(handler);

        final Runnable simple = new Runnable() {
            @Override
            public void run() {
                simple(processor);
            }
        };

        new Thread(simple).start();
    }

    private static void simple(MultiplicationService.Processor processor) {

        TNonblockingServerTransport serverTransport = null;
        try {
            serverTransport = new TNonblockingServerSocket(9090);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        THsHaServer server = new THsHaServer(new THsHaServer.Args(serverTransport).processor(processor));

        System.out.println("Starting server ...");
        server.serve();

    }
}
