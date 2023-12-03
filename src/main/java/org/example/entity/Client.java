package org.example.entity;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;


public class Client implements Runnable {
    private final List<Integer> dataList;
    private final Server server;

    public Client(List<Integer> dataList, Server server) {
        this.dataList = dataList;
        this.server = server;
    }

    @Override
    public void run() {
        while (!dataList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(dataList.size());
            int value = dataList.remove(randomIndex);

            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(401) + 100);
                    server.processRequest(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
