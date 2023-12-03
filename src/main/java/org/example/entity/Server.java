package org.example.entity;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Server {
    private final List<Integer> resultList;
    private final int n;

    public Server(int n) {
        this.resultList = new CopyOnWriteArrayList<>();
        this.n = n;
    }

    public void processRequest(int value) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt((901) + 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resultList.add(value);
    }

    public void checkData() {
        System.out.println("Result list size: " + resultList.size());
        System.out.println("Result list contains 1 to n: " + new HashSet<>(resultList).containsAll(generateList()));
        System.out.println("Result list size equals n: " + (resultList.size() == n));
    }

    public int getAccumulator() {
        return resultList.stream().mapToInt(Integer::intValue).sum();
    }

    private List<Integer> generateList() {
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }
}
