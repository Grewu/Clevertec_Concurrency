package org.example.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientTest {

    private static final int DATA_SIZE = 100;

    private Server server;
    private Client client;

    @BeforeEach
    void setUp() {
        server = new Server(DATA_SIZE);
        client = new Client(generateList(), server);
    }

    @AfterEach
    void tearDown() {
        server.checkData();
    }

    @Test
    void testClientProcessing() {
        client.run();
        assertTrue(server.getAccumulator() > 0);
    }

    @Test
    void testServerResults() throws InterruptedException {
        List<Integer> dataList = generateList();
        CountDownLatch latch = new CountDownLatch(DATA_SIZE);

        dataList.forEach(value -> {
            server.processRequest(new Request(value));
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

        server.checkData();

        assertEquals(DATA_SIZE, server.getAccumulator());
    }

    private List<Integer> generateList() {
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= DATA_SIZE; i++) {
            list.add(i);
            server.processRequest(new Request(i));
        }
        return list;
    }
}
