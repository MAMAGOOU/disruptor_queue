import java.util.Spliterator;

/**
 * @author 19750
 * @version 1.0
 */
public class ArrayBlockingQueue<E> {
    final Object[] items;
    int count;
    /**
     * 读指针
     */
    int takeIndex;
    /**
     * 写指针
     */
    int putIndex;

    /**
     * 默认阻塞策略
     */
    WaitStrategy wait = new SleepingWaitStrategy();

    public ArrayBlockingQueue(int capacity) {
        this.items = new Object[capacity];
    }

    public ArrayBlockingQueue(int capacity, WaitStrategy waitStrategy) {
        this.items = new Object[capacity];
        this.wait = waitStrategy;
    }

    /**
     * 存放队列的方法，不限时阻塞
     */
    public void put(E e) throws InterruptedException {
        while (count == items.length) {
            System.out.println("生产者线程阻塞一下");
            wait.waitFor();
        }
        System.out.println("生产者线程继续运行");
        // 在这里线程可能阻塞结束或者没有阻塞，需要重置retries的属性
        wait.setRetries(200);
        enqueue(e);
    }

    /**
     * 取出数据的方法，不限时阻塞
     *
     * @return
     * @throws InterruptedException
     */
    public E take() throws InterruptedException {
        while (count == 0) {
            System.out.println("消费者线程阻塞一下");
            wait.waitFor();
        }
        System.out.println("消费者线程继续运行");
        wait.setRetries(200);
        return dequeue();
    }

    /**
     * 将数据从队列中取走，按顺序从前往后拿走
     *
     * @return
     */
    private E dequeue() {
        final Object[] items = this.items;
        E e = (E) items[takeIndex];
        items[takeIndex] = null;
        takeIndex++;
        if (takeIndex == items.length) {
            takeIndex = 0;
        }
        count--;
        return e;
    }


    /**
     * 将数据添加到数组中，按照从前往后的方式进行添加
     *
     * @param e
     */
    private void enqueue(E e) {
        final Object[] items = this.items;
        items[putIndex] = e;
        putIndex++;
        if (putIndex == items.length) {
            putIndex = 0;
        }
        count++;
    }

}
