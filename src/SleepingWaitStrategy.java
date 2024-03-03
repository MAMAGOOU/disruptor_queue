import java.util.concurrent.locks.LockSupport;

/**
 * @author 19750
 * @version 1.0
 */
public final class SleepingWaitStrategy implements WaitStrategy {

    /**
     * 默认的自旋次数
     */
    private static final int DEFAULT_RETRIES = 200;

    /**
     * 默认的休眠时间
     */
    private static final long DEFAULT_SLEEP = 200;

    /**
     * 自旋次数
     */
    private int retries;

    /**
     * 睡眠时间
     */
    private final long sleepTimeNS;

    public SleepingWaitStrategy() {
        this(DEFAULT_RETRIES, DEFAULT_SLEEP);
    }

    public SleepingWaitStrategy(int retries, long sleepTimeNS) {
        this.retries = retries;
        this.sleepTimeNS = sleepTimeNS;
    }

    /**
     * 线程结束阻塞后，要重置retries的属性为200
     *
     * @param retries
     */
    @Override
    public void setRetries(int retries) {
        this.retries = retries;
    }

    /**
     * 阻塞策略
     *
     * @param a 生产者进度
     * @param b 消费者进度
     */
    @Override
    public void waitFor(Sequence a, long b) {
        int counter = retries;
        while (a.get() < b) {
            counter = applyWaitMethod(counter);
        }
    }


    private int applyWaitMethod(int counter) {
        if (counter > 100) {
            --counter;
        }
        //如果自旋次数小于100，大于0了，说明已经自旋了很多次了，但还是不能继续向下工作，这时候尝试让该线程让出CPU
        else if (counter > 0) {
            --counter;
            Thread.yield();
        } else {
            //走到这里意味着自旋次数到达200了，这时候就干脆让线程睡一会吧
            //睡的时间就是100纳秒，不能睡得太久，因为生产者可能随时发布新的数据
            LockSupport.parkNanos(sleepTimeNS);
        }

        //这里返回剩余的自旋次数，方便下一次循环的时候继续使用
        //这样，再多消费者情况下，只要外面的循环不结束，那么每个线程使用的都是自己剩余的自旋数
        return counter;
    }
}
