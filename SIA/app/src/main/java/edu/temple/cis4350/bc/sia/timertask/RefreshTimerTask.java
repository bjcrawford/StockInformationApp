package edu.temple.cis4350.bc.sia.timertask;


import java.util.TimerTask;

public class RefreshTimerTask extends TimerTask {

    private OnRefreshTimerTaskInteractionListener listener;

    public RefreshTimerTask(OnRefreshTimerTaskInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.OnRefreshTimerTaskInteraction();
    }

    public interface OnRefreshTimerTaskInteractionListener {
        public void OnRefreshTimerTaskInteraction();
    }

}
