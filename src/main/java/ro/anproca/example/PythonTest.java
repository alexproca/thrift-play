package ro.anproca.example;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import tutorial.NotificationService;

/**
 * Created by alex on 12.12.2014.
 */
public class PythonTest {


    public static void main(String[] args) throws TException {

        TTransport transport = new TFramedTransport(new TSocket("localhost", 12005));

        TProtocol protocol = new TBinaryProtocol(transport);

        NotificationService.Client callbackClient = new NotificationService.Client(protocol);

        transport.open();
        callbackClient.notify("Hello from java");
        transport.close();

    }

}
