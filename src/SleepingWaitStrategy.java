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
     * 让线程等待
     */
    @Override
    public void waitFor() {
        if (retries > 100) {
            retries--;
        }
        // 如果自旋次数<100，说明已经自旋很多次了，尝试让该线程退出cpu
        else if (retries > 0) {
            retries--;
            Thread.yield();
        } else {
            // 自选次数达到了200就干脆让线程睡一下
            // 但是不能够太久，因为生产者随时可能发布新的数据
            LockSupport.parkNanos(sleepTimeNS);
        }
    }
}
