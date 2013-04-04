package com.mtvi.arc.runner.jmeter;


import java.util.ArrayList;
import java.util.List;

/**
 * JMeter Utility functions.
 */
public final class JMeterUtils {

    /**
     * Private constructor for non-instantiable helper classes.
     */
    private JMeterUtils() {
        //Hidden Constructor.
    }

    /**
     * Get a list of thread names to wait upon.
     *
     * @param guiThread look for GUI threads (false implies non-GUI threads)
     * @return the list of thread names to wait upon
     */
    public static List<String> getThreadNames(boolean guiThread) {
        List<String> threadNames = new ArrayList<String>();
        for (JMeterThreads thread : JMeterThreads.values()) {
            if (thread.isGUIThread() == guiThread) {
                threadNames.add(thread.getThreadName());
            }
        }
        return threadNames;
    }
}
