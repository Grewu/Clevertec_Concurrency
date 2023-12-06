package org.example.entity;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Server {
    private final List<Integer> resultList;
    private final int dataSize;
    private final ExecutorService executorService;

    public Server(int dataSize) {
        this.resultList = new CopyOnWriteArrayList<>();
        this.dataSize = dataSize;
        this.executorService = Executors.newFixedThreadPool(dataSize);
    }

    public void processRequest(Request request) {
        executorService.submit(() -> {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(901) + 100);
                resultList.add(request.value());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void checkData() {
        executorService.shutdown();
        try {
           executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Result list size: " + resultList.size());
        System.out.println("Result list contains 1 to n: " + new HashSet<>(resultList).containsAll(generateList()));
        System.out.println("Result list size equals n: " + (resultList.size() == dataSize));
    }

    public int getAccumulator() {
        return resultList.stream().mapToInt(Integer::intValue).sum();
    }

    private List<Integer> generateList() {
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= dataSize; i++) {
            list.add(i);
        }
        return list;
    }

}