package com.mtvi.casl.runner.jmeter;

/**
 * Thread names added to this list will be used when scanning JMeterThreads directly after JMeter is called.
 */
public enum JMeterThreads {

    /**
     * Standard JMeter Engine that is doing actual execution of test.
     */
    STANDARD_JMETER_ENGINE("StandardJMeterEngine", false),

    /**
     * Windows GUI Thread.
     */
    GUI_THREAD_WINDOWS("AWT-Windows", true),

    /**
     * Mac OS GUI Thread.
     */
    GUI_THREAD_MACOSX("AWT-AppKit", true),

    /**
     * Linux GUI Thread.
     */
    GUI_THREAD_LINUX("AWT-XAWT", true);

    /**
     * Thread name.
     */
    private final String threadName;

    /**
     * Flag that indicates whether thread is GUI or not.
     */
    private final boolean guiThread;

    /**
     * Private constructor.
     *
     * @param threadName - thread name.
     * @param guiThread  - flag for gui thread indication.
     */
    private JMeterThreads(String threadName, boolean guiThread) {
        if (threadName == null) {
            throw new IllegalArgumentException(String.format(
                    "Null or blank or wrong value has been passed in as required argument [threadName=%s]", threadName));
        }

        this.threadName = threadName;
        this.guiThread = guiThread;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public boolean isGUIThread() {
        return this.guiThread;
    }
}
