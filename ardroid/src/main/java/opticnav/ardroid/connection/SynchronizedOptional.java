package opticnav.ardroid.connection;

import android.util.Pair;

import java.util.NoSuchElementException;

/**
 * Much like Java 8's Optional<T>, but we don't have access to it.
 *
 * get() and set() are synchronized so that it's thread-safe.
 */
class SynchronizedOptional<E> {
    private boolean isPresent = false;
    private E value = null;

    public synchronized E get() {
        if (!this.isPresent) {
            throw new NoSuchElementException("Cannot get a non-present SynchronizedOptional value");
        }
        return this.value;
    }

    public synchronized Pair<Boolean, E> getIfPresent() {
        if (!this.isPresent) {
            return new Pair<Boolean, E>(false, null);
        } else {
            return new Pair<Boolean, E>(true, this.value);
        }
    }

    public synchronized void set(E value) {
        this.isPresent = true;
        this.value = value;
    }

    public synchronized void empty() {
        this.isPresent = false;
        this.value = null;
    }

    /** Perform get() and empty() as an atomic operation. */
    public synchronized E getAndEmpty() {
        if (!this.isPresent) {
            throw new NoSuchElementException("Cannot get a non-present SynchronizedOptional value");
        }
        E value = this.value;
        this.isPresent = false;
        this.value = null;
        return value;
    }

    public synchronized boolean isPresent() {
        return this.isPresent;
    }
}
