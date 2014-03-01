package opticnav.ardd;

public class BlockingValue<E> {
    private E value;
    private Object lock;
    private boolean isSet;
    
    public BlockingValue() {
        this.lock = new Object();
        this.isSet = false;
    }
    
    public void set(E value) {
        synchronized (this.lock) {
            if (this.isSet) {
                throw new IllegalStateException("BlockingValue cannot be set more than once");
            }
            this.isSet = true;
            this.value = value;
            this.lock.notifyAll();
        }
    }
    
    public E get() throws InterruptedException {
        synchronized (this.lock) {
            while (!this.isSet) {
                this.lock.wait();
            }
        }
        return this.value;
    }
}
