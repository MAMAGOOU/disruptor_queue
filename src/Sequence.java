/**
 * 序号进度包装对象
 *
 * @author 19750
 * @version 1.0
 */
public class Sequence {
    protected volatile long value;
    /**
     * 初始化进度，不管是消费者还是生产者，初始化经度都是-1
     */
    static final long INITIAL_VALUE = -1L;

    public Sequence() {
        this(INITIAL_VALUE);
    }

    public Sequence(final long initialValue) {
        this.value = initialValue;
    }

    public long get() {
        return value;
    }

    public void set(long value) {
        this.value = value;
    }

}