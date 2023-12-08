package org.example.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class Client implements Runnable {
    private final List<Integer> dataList;
    private final Server server;
    private final ExecutorService executorService;
    private final List<CompletableFuture<Void>> futures;

    public Client(List<Integer> dataList, Server server) {
        this.dataList = dataList;
        this.server = server;
        this.executorService = Executors.newFixedThreadPool(dataList.size());
        this.futures = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!dataList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(dataList.size());
            int value = dataList.remove(randomIndex);

            Request request = new Request(value);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(new Random().nextInt(401) + 100);
                    server.processRequest(request);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }, executorService);

            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException();
        }

        executorService.shutdown();
    }

}

