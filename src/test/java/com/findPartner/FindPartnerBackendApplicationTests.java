package com.findPartner;

import com.findPartner.domain.entity.User;
import com.findPartner.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
class FindPartnerBackendApplicationTests {
    @Resource
    private UserService userService;

    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));


    /**
     * 并发批量插入用户
     */
    @Test
    void doConcurrencyInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        int batchSize = 10000;
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("布丁");
                user.setUserAccount("pudding");
                user.setAvatar("");
                user.setGender(1);
                user.setUserPassword("12345678");
                user.setPhone("18040523181");
                user.setEmail("code_make@163.com");
                user.setTags("[]");
                user.setUserStatus(0);
                user.setUserRole(0);
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 20 秒 10 万条
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    @Test
    void testThread() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("runAsync执行了");
            }
        });

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "supplyAsync");
        CompletableFuture.allOf(runAsync, supplyAsync).join();
    }

    @Test
    void testThread2() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("runAsync执行了");
            }
        });
        runAsync.get();
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "supplAsync执行了");
        System.out.println(supplyAsync.get());
        System.out.println(CompletableFuture.anyOf(runAsync, supplyAsync));
    }
}
