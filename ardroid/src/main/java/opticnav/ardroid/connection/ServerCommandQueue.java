package opticnav.ardroid.connection;

import android.os.Handler;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * ServerCommandQueue ensures that server commands are run in sequence. This is meant to prevent race conditions
 * created by executing commands in multiple threads simultaneously.
 */
public final class ServerCommandQueue implements Runnable {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ServerCommandQueue.class);
    private final Handler handler;
    private final FinishedEvent finishedEvent;
    private boolean finishing;

    public interface Command<E> {
        public abstract E background() throws IOException;
        public void ui(E result) throws Exception;
    }

    public interface FinishedEvent {
        public void finished();
    }

    private final BlockingQueue<Command<?>> commands;

    public ServerCommandQueue(Handler handler, final FinishedEvent finishedEvent) {
        this.handler = handler;
        this.commands = new LinkedBlockingDeque<Command<?>>();
        this.finishedEvent = finishedEvent;
        this.finishing = false;
    }

    @Override
    public void run() {
        try {
            while (!finishing && !Thread.currentThread().isInterrupted()) {
                final Command command = this.commands.take();
                LOG.debug("Running background command...: " + command);
                final Object result = command.background();
                LOG.debug("Finished background command: " + command);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            command.ui(result);
                        } catch (Exception e) {
                            // this is fatal. Make the application crash with it.
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            // do nothing - thread was just interrupted
            LOG.debug("Interrupted");
        } catch (EOFException e) {
            // connection ended, don't worry about it
            LOG.debug("Connection ended");
        } catch (IOException e) {
            LOG.catching(e);
        } finally {
            this.finishedEvent.finished();
        }
    }

    public void enqueue(Command command) {
        try {
            this.commands.put(command);
        } catch (InterruptedException e) {
            // re-interrupt the current thread
            Thread.currentThread().interrupt();
        }
    }

    public void finish() {
        enqueue(new Command<Void>() {
            @Override
            public Void background() throws IOException {
                finishing = true;
                return null;
            }

            @Override
            public void ui(Void result) {
            }
        });
    }
}
