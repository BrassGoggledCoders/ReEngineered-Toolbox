package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public abstract class SocketQueue<T> {
    private List<T> queuedList;
    private int queueSize;

    public SocketQueue(int initialSize) {
        this.queueSize = initialSize;
        queuedList = Lists.newArrayList();
    }

    public T offer(T value) {
        value = addToBack(value);
        while (anyRemainingValue(value) && queuedList.size() < queueSize) {
            value = addToBack(value);
        }
        return value;
    }

    protected void push(T value) {
        this.queuedList.add(value);
    }

    public Optional<T> peek() {
        return Optional.ofNullable(queuedList.isEmpty() ? null : queuedList.get(0));
    }

    public Optional<T> pull() {
        Optional<T> value;
        if (queuedList.isEmpty()) {
            value = Optional.empty();
        } else {
            value = Optional.of(queuedList.get(0));
            queuedList.remove(0);
        }

        return value;
    }

    public Optional<T> getEndOfQueue() {
        return Optional.ofNullable(queuedList.isEmpty() ? null : queuedList.get(queuedList.size() - 1));
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getLength() {
        return queuedList.size();
    }

    protected List<T> getBackingList() {
        return this.queuedList;
    }

    public abstract T simulateOffer(T value);

    protected abstract T addToBack(T value);

    protected abstract boolean anyRemainingValue(T value);
}
