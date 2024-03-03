/**
 * 阻塞策略接口
 *
 * @author 19750
 */
public interface WaitStrategy {
    /**
     * 让线程等待
     */
    void waitFor(Sequence a, long b);

    void setRetries(int retries);

}


