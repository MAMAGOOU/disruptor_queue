/**
 * 环形队列
 *
 * @author 19750
 * @version 1.0
 */
public final class RingBuffer<E> {
    private final long indexMask;
    /**
     * 环形数组
     */
    private final Object[] entries;

    /**
     * 数组的大小，必须是2的n次幂
     */
    protected final int bufferSize;

    RingBuffer(int bufferSize, EventFactory<E> eventFactory) {
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize不能小于1");
        }
        // 判断二进制中有多少个1，如果只有一个，说明是2的n次幂
        if (Integer.bitCount(bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize必须是2的n次幂");
        }

        this.bufferSize = bufferSize;
        this.indexMask = bufferSize - 1;
        this.entries = new Object[bufferSize];
        // 初始化数组，创建数组中的对象不会被回收
        // 但是对象中封装的对象会被垃圾回收
        // 一定程度上减少了垃圾回收
        fill(eventFactory);
    }

    /**
     * 初始化数组，创建数组中的每一个对象
     *
     * @param eventFactory
     */
    private void fill(EventFactory<E> eventFactory) {
        for (int i = 0; i < bufferSize; i++) {
            entries[i] = eventFactory.newInstance();
        }
    }

    /**
     * 发布生产者数据的核心方法
     *
     * @param translator
     * @param arg0
     * @param <A>
     */
    public <A> void publishEvent(EventTranslatorOneArg<E, A> translator, A arg0) {
        final long sequence = next();
        translateAndPublish(translator, sequence, arg0);
    }

    /**
     * 根据生产者的进度序号将数据放到数组对应的位置的方法
     * @param translator
     * @param sequence
     * @param arg0
     * @param <A>
     */
    private <A> void translateAndPublish(EventTranslatorOneArg<E, A> translator, long sequence, A arg0) {
        try {
            translator.translateTo(get(sequence), sequence, arg0);
        } finally {
            // 更新生产者进度，让消费者知道
        }
    }

    /**
     * 根据序号获得环形数组中对应的元素
     *
     * @param sequence
     * @return
     */
    public E get(long sequence) {
        return elementAt(sequence);
    }

    /**
     * 根据序号得到数组对应位置的元素
     *
     * @param sequence
     * @return
     */
    protected final E elementAt(long sequence) {
        return (E) entries[(int) (sequence & indexMask)];
    }

    /**
     * 获取生产者下一个可用的进度序号，比如现在是25，下一个存放的数据就是26，并将26给返回
     *
     * @return
     */
    public long next() {
        return 0;
    }

}
