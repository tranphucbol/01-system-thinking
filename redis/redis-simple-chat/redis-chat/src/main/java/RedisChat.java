import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RedisChat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your channel: ");
        String channel = scanner.nextLine();

        RedissonClient redissonClient = Redisson.create();
        RTopic<MyMessage> topic = redissonClient.getTopic(channel);

        topic.addListener(new MessageListener<MyMessage>() {
            public void onMessage(String s, MyMessage message) {
                System.out.println(message);
            }
        });

        RMapCache<String, String> map = redissonClient.getMapCache(channel);

        //load old messages
        for(String msg : map.readAllValues()) {
            System.out.println(msg);
        }

        while(true) {
            String m = scanner.nextLine();

            MyMessage message = new MyMessage(name, m);
            topic.publish(message);

            map.put(message.getKey(), message.toString(), 1, TimeUnit.DAYS);
        }
    }
}
