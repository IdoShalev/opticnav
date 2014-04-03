package opticnav.ardd;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SettableFuture<V> implements Future<V> {
    private V value;
    private boolean isSet;
    private Object lock;
    
    public SettableFuture() {
        this.value = null;
        this.isSet = false;
        this.lock = new Object();
    }
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (this.lock) {
            while (!this.isSet) {
                this.lock.wait();
            }
        }
        return value;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException();
    }
    
    public void set(V value) {
        if (this.isSet) {
            throw new IllegalStateException("Future was already set");
        }
        this.value = value;
        synchronized (this.lock) {
            this.isSet = true;
            this.lock.notify();
        }
    }
}
