package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import java.util.Optional;

public interface IQueue<T> {
    T push(T value);

    Optional<T> pull();

    int getLength();
}
