/**
 * @author banbao
 * @comment modified from example code
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.io.*;

public class BankerTellerDemo {
    static class Teller {
        private static int cnt = 1000;
        public final int ID = ++cnt;
        public Teller() {}
        @Override
        public String toString() {
            return "Teller " + ID;
        }
    }
    static class Consumer {
        public final String name;
        public final int time;
        public Consumer(String name, int time) {
            this.name = name;
            this.time = time;
        }
        @Override
        public String toString() {
            return name + "[" + time + "]";
        }
    }
    private volatile boolean closeBank = false;
    private ArrayBlockingQueue<Teller> tellers;
    private LinkedBlockingQueue<Consumer> consumers
        = new LinkedBlockingQueue<>();
    public BankerTellerDemo(int nTeller) {
        // initial the tellers
        tellers = new ArrayBlockingQueue<>(nTeller);
        for(int i = 0;i < nTeller; ++i) {
            try {
                tellers.put(new Teller());
            } catch(InterruptedException e) {
                System.out.println("Interrupted During tellers.put()");
            }
        }
        // get input
        Thread inputThread = new Thread(new Runnable(){
            @Override
            public void run() {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(System.in));
                while(!Thread.interrupted()) {
                    String line = null;
                    try {
                        line = br.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    if(Thread.interrupted()) break; // interrupted
                    if(line == null) continue;
                    String[] what = line.split(" ");
                    if(what.length != 2) continue;
                    if(!what[1].matches("[\\d]*")) continue;
                    Consumer consumer = new Consumer(what[0], Integer.parseInt(what[1]));
                    try {
                        consumers.put(consumer);
                    } catch(InterruptedException e) {
                        System.out.println("Interrupted during consumers.put()");
                    }
                    System.out.println(consumer + " is in the List!");
                }
                try {
                     br.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
                System.out.println("No more work will be aceepted!");
            }
        });
        inputThread.start();
        // do work
        Thread workThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!closeBank || consumers.size() != 0) {
                    Consumer consumer = null;
                    Teller teller = null;
                    // no problems
                    while(teller == null) {
                        try{
                            teller = tellers.take();
                        } catch (InterruptedException e){
                            System.out.println("Interrupted While Getting Teller!");
                        }
                    }
                    try{
                        consumer = consumers.take();
                    } catch (InterruptedException e){
                        System.out.println("Interrupted While Getting Consumer!");
                    }
                    // restart
                    if(consumer == null) {
                        try {
                            tellers.put(new Teller());
                        } catch(InterruptedException e) {
                            System.out.println("Interrupted While Restart!");
                        }
                        continue;
                    }
                    // for anonymous class
                    Consumer consumerA = consumer;
                    Teller tellerA = teller;
                    // new Thread to do work
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            System.out.println(
                                tellerA + " is already for " + consumerA);
                            try{
                                TimeUnit.SECONDS.sleep(consumerA.time);
                            } catch (InterruptedException e){
                                System.out.println("Work:Sleep Interrupted!");
                            }
                            System.out.println(consumerA + " left " + tellerA);
                            try {
                                tellers.put(tellerA);
                            } catch(InterruptedException e) {
                                System.out.println(tellerA + " is not in work!");
                            }
                        }
                    }).start();
                }
                System.out.println("Bank Close!");
            }
        });
        workThread.start();
        try{
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e){
            System.out.println("main:Sleep Interrupted");
        }
        closeBank = true;
        inputThread.interrupt();
        workThread.interrupt();
    }
    public static void main(String...args) {
        new BankerTellerDemo(3);
    }
}


/* Output
*/