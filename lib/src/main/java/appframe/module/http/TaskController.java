package appframe.module.http;

/**
 * Created by ybao on 16/2/2.
 */
public class TaskController {
    private final Object lock = new Object();
    private boolean cencl = false;
    private boolean pause = false;

    public void setPause(boolean pause) {
        synchronized (lock) {
            this.pause = pause;
        }
    }

    public void setCencl() {
        synchronized (lock) {
            this.cencl = true;
        }
    }

    public boolean isCencl() {
        synchronized (lock) {
            return cencl;
        }
    }

    public boolean isPause() {
        synchronized (lock) {
            return pause;
        }
    }
}
