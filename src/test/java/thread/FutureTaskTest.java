package thread;

import java.util.concurrent.*;

public class FutureTaskTest {
    public static void main(String[] args) {
        test();
    }
    public static void test() {
        //ExecutorService 方式：
        //ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i=0;i<10;i++) {
            String tasknum = "task" + i;
            Task task = new Task(tasknum);
            FutureTask<String> futureTask = new FutureTask<String>(task);
            System.out.println(tasknum + "子线程运行完毕");
            executor.submit(futureTask);
            executor.shutdown();

            //Thread 方式：
            /*Task task = new Task();
            FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
            Thread thread = new Thread(futureTask);
            thread.start();*/

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                System.out.println(tasknum + "运行结果" + futureTask.get(5000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

}
class Task implements Callable<String> {

    private String parameter;

    public Task(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String call() throws Exception {
        System.out.println(parameter+"线程在运行");
        Thread.sleep(3000);
        return parameter;
    }
}