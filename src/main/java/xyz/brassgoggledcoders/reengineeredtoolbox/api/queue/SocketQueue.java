package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public abstract class SocketQueue<T> implements INBTSerializable<NBTTagCompound> {
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

    protected void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getLength() {
        return queuedList.size();
    }

    protected List<T> getBackingList() {
        return this.queuedList;
    }

    protected abstract T addToBack(T value);

    protected abstract boolean anyRemainingValue(T value);

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setInteger("queueSize", this.getQueueSize());
        NBTTagList tagList = new NBTTagList();
        for (T value : this.getBackingList()) {
            tagList.appendTag(serializeValue(value));
        }
        nbtTagCompound.setTag("queueValues", tagList);
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.setQueueSize(nbt.getInteger("queueSize"));
        NBTTagList queueValueNBT = nbt.getTagList("fluidStacks", 9);
        for (int i = 0; i < queueValueNBT.tagCount(); i++) {
            this.getBackingList().add(deserializeValue(queueValueNBT.getCompoundTagAt(i)));
        }
    }

    public abstract NBTTagCompound serializeValue(T value);

    public abstract T deserializeValue(NBTTagCompound nbtTagCompound);
}
