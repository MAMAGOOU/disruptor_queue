/**
 * 测试类
 * @author 19750
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(16);

        // 启动一个生产者线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i =0;
                // 在循环中不断的向队列放入数据
                while (true){
                    try {
                        queue.put(i++);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();

        // 启动一个消费者线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }
}
