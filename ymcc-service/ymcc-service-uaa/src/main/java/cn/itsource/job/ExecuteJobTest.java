package cn.itsource.job;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExecuteJobTest {

    //@Scheduled(cron = "0/2 * * * * ?")
    //@Async
    public void configureTasks() {
        System.err.println(Thread.currentThread().getName()+"执行任务！！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
