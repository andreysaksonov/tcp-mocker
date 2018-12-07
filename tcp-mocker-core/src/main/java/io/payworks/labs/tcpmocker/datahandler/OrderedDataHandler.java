package io.payworks.labs.tcpmocker.datahandler;

import io.payworks.labs.tcpmocker.order.Ordered;

import java.util.Optional;

public class OrderedDataHandler implements DataHandler, Ordered, Comparable<OrderedDataHandler> {

    private final int order;
    private final DataHandler delegate;

    public OrderedDataHandler(final int order,
                              final DataHandler delegate) {
        this.order = order;
        this.delegate = delegate;
    }

    public OrderedDataHandler(final DataHandler delegate) {
        this(DEFAULT_ORDER, delegate);
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        return delegate.handle(data);
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public int compareTo(final OrderedDataHandler that) {
        return Integer.compare(this.order, that.order);
    }
}
