package de.sirati97.bex_proto.threading;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class SyncHelper implements AsyncHelper {
    private boolean updateThreadName;
    private Queue<SyncTask> queue = new LinkedList<>();

    public SyncHelper(boolean updateThreadName) {
        this.updateThreadName = updateThreadName;
    }

    public boolean yield() {
        if (queue.size() > 0) {
            SyncTask task = queue.poll();
            task.start();
            return true;
        }
        return false;
    }

    @Override
    public AsyncTask runAsync(Runnable runnable, String name) {
        SyncTask task = new SyncTask(name, runnable);
        return task;
    }

    @Override
    public AsyncHelperExecutorService createExecutorService(String name) {
        return new AsyncHelperExecutorService(this, name);
    }

    public class SyncTask implements AsyncTask {
        private String name;
        private boolean running=false;
        private Thread thread;
        private String oldName;
        private Runnable runnable;
        private boolean stopped = false;

        public SyncTask(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        @Override
        public void stop() {
            if (thread != null && running) {
                stopped = true;
                thread.interrupt();
            }
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
            if (thread != null && updateThreadName) {
                thread.setName(name);
            }
        }

        public void start() {
            this.thread = Thread.currentThread();
            this.running = true;
            this.oldName = thread.getName();
            if (updateThreadName) {
                thread.setName(name);
            }
            runnable.run();
            if (stopped) {
                Thread.interrupted();
            }

            this.running = false;
        }
    }
}
