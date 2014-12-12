package ro.anproca.example.handlers;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import tutorial.MultiplicationService;
import tutorial.NotificationService;
import tutorial.RegisteringClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 09.12.2014.
 */
public class MultiplicationHandler implements MultiplicationService.Iface {

    private static class Container {
        private final String clientName;
        private final int port;
        private final NotificationService.Iface client;
        private final TTransport transport;

        public String getClientName() {
            return clientName;
        }

        public int getPort() {
            return port;
        }

        public NotificationService.Iface getClient() {
            return client;
        }

        public TTransport getTransport() {
            return transport;
        }

        public Container(String clientName, int port, NotificationService.Iface client, TTransport transport) {
            this.clientName = clientName;
            this.port = port;
            this.client = client;
            this.transport = transport;
        }

        public static Container create(String clientName, int port, NotificationService.Iface client, TTransport transport) {
            return new Container(clientName, port, client, transport);
        }
    }

    private List<Container> clients;

    @Override
    public void registerClient(RegisteringClient registeringClient) throws TException {

        System.out.println("Client name: " + registeringClient.getName() + " Client port: " + registeringClient.getPort());

        TTransport transport = new TFramedTransport(new TSocket("localhost", registeringClient.getPort()));

        TProtocol protocol = new TBinaryProtocol(transport);

        NotificationService.Client callbackClient = new NotificationService.Client(protocol);

        getClients().add(Container.create(registeringClient.getName(), registeringClient.getPort(), callbackClient, transport));
    }

    @Override
    public int multiply(int n1, int n2) {
        int result = n1 * n2;

        for (Container handler : getClients()) {

            System.out.println("Notify client: " + handler.getClientName() + " on port: " + handler.getPort());

            try {
                handler.getTransport().open();
                handler.getClient().notify(handler.getClientName() + result);
                handler.getTransport().close();
            } catch (TException e) {
                getClients().remove(handler);
            }
        }

        return result;
    }

    public List<Container> getClients() {

        if (clients == null) {
            clients = new ArrayList<Container>();
        }
        return clients;
    }

}
