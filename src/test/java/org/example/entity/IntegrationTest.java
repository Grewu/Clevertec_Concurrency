package org.example.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    @Test
    void testClientServerInteraction() throws InterruptedException {
        int dataSize = 10;
        Server server = new Server(dataSize);
        List<Integer> dataList = generateTestData(dataSize);
        Client client = new Client(new ArrayList<>(dataList), server);

        CountDownLatch latch = new CountDownLatch(dataSize);

        Thread serverThread = new Thread(() -> {
            try {
                for (int i = 0; i < dataSize; i++) {
                    Request request = new Request(dataList.get(i));
                    server.processRequest(request);
                    latch.countDown();
                }
                server.checkData();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        Thread clientThread = new Thread(client);

        serverThread.start();
        clientThread.start();

        serverThread.join();
        clientThread.join();

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(server.getAccumulator(), dataSize);
    }

    private List<Integer> generateTestData(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }
        return list;
    }
}

