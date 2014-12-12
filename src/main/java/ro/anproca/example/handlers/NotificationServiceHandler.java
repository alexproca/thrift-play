package ro.anproca.example.handlers;

import org.apache.thrift.TException;
import tutorial.NotificationService;

/**
 * Created by alex on 09.12.2014.
 */
public class NotificationServiceHandler implements NotificationService.Iface {

    @Override
    public void notify(String value) throws TException {

        System.out.println("Notification received " + value);

    }
}
