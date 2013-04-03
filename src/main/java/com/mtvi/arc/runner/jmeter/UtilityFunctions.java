package com.mtvi.arc.runner.jmeter;


import com.mtvi.arc.runner.jmeter.threadhandling.JMeterThreads;

import java.util.ArrayList;
import java.util.List;

/**
 * JMeter Utility functions.
 */
public final class UtilityFunctions {

    /**
     * Private constructor for non-instantiable helper classes.
     */
    private UtilityFunctions() {
        //Hidden Constructor.
    }

    /**
     * Get a list of thread names to wait upon.
     *
     * @param ofTypeGUI look for GUI threads (false implies non-GUI threads)
     * @return the list of thread names to wait upon
     */
    public static List<String> getThreadNames(boolean ofTypeGUI) {
        List<String> threadNames = new ArrayList<String>();
        for (JMeterThreads thread : JMeterThreads.values()) {
            if (thread.isGUIThread() == ofTypeGUI) {
                threadNames.add(thread.getThreadName());
            }
        }
        return threadNames;
    }
}
