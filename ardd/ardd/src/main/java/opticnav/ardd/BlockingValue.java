package opticnav.ardd;

/**
 * A synchronization primitive used to get a value set at a later time.
 * If the value is requested and not set, the thread will block until it is set.
 * 
 * @author Danny Spencer
 *
 * @param <E> The type of the value to get() and set()
 */
public class BlockingValue<E> {
    private E value;
    private Object lock;
    private boolean isSet;
    
    /**
     * Construct the BlockingValue. No value is set.
     */
    public BlockingValue() {
        this.lock = new Object();
        this.isSet = false;
    }
    
    /**
     * Set a value. This can only be called once.
     * 
     * @param value The value to set
     * @throws IllegalStateException Thrown if set() was already called before
     */
    public void set(E value) throws IllegalStateException {
        synchronized (this.lock) {
            if (this.isSet) {
                throw new IllegalStateException("BlockingValue cannot be set more than once");
            }
            this.isSet = true;
            this.value = value;
            this.lock.notifyAll();
        }
    }
    
    /**
     * Get the value set by set(). If the value hasn't been set, the thread will block until a value is set.
     * 
     * @return The value set by set()
     * @throws InterruptedException Thrown if the thread is interrupted
     */
    public E get() throws InterruptedException {
        synchronized (this.lock) {
            while (!this.isSet) {
                this.lock.wait();
            }
        }
        return this.value;
    }
}
