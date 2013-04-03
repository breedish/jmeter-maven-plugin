package com.mtvi.arc.runner.jmeter;

import com.google.common.io.NullOutputStream;
import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.SystemTestResult;
import com.mtvi.arc.runner.Executor;
import org.apache.jmeter.JMeter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DefaultJmeterExecutor class.
 */
public class DefaultJmeterExecutor implements Executor {

    private static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");

    private static final String OS_NAME = System.getProperty("os.name");

    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);

    private static final String JAVA_CLASS_PATH = "java.class.path";// $NON-NLS-1$

    private final JmeterCLConfigConverter configConverter = new JmeterCLConfigConverter();

    private final ExecutionConfig config;

    private final JMeter executable;

    private final CountDownLatch latch = new CountDownLatch(1);

    private final ExecutorService service;

    private final PrintStream initialPrintStream = System.out;


    public DefaultJmeterExecutor(ExecutionConfig config) {
        this.config = config;
        this.executable = new JMeter();
        this.service = Executors.newFixedThreadPool(1);
        init(config);
        configureJmeterEnvironment();
    }

    private void init(ExecutionConfig config) {
        final List<URL> jars = new LinkedList<URL>();
        String initialClasspath = System.getProperty(JAVA_CLASS_PATH);
        /*
         * Does the system support UNC paths? If so, may need to fix them up
         * later
         */
        boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$

        // Add standard jar locations to initial classpath
        StringBuilder classpath = new StringBuilder();
        File[] libDirs = new File[] { new File(config.getExecutorHome() + File.separator + "lib"),
                new File(config.getExecutorHome() + File.separator + "lib" + File.separator + "ext"),
                new File(config.getExecutorHome() + File.separator + "lib" + File.separator + "junit"),
                new File(config.getExecutorHome() + File.separator + "bin" + File.separator)
        };

        for (int a = 0; a < libDirs.length; a++) {
            File[] libJars = libDirs[a].listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            if (libJars == null) {
                new Throwable("Could not access " + libDirs[a]).printStackTrace();
                continue;
            }
            Arrays.sort(libJars); // Bug 50708 Ensure predictable order of jars
            for (int i = 0; i < libJars.length; i++) {
                try {
                    String s = libJars[i].getPath();

                    // Fix path to allow the use of UNC URLs
                    if (usesUNC) {
                        if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "\\\\" + s;// $NON-NLS-1$
                        } else if (s.startsWith("//") && !s.startsWith("///")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "//" + s;// $NON-NLS-1$
                        }
                    } // usesUNC

                    jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                    classpath.append(CLASSPATH_SEPARATOR);
                    classpath.append(s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        // ClassFinder needs the classpath
        System.setProperty(JAVA_CLASS_PATH, initialClasspath + classpath.toString());
        Thread.currentThread().setContextClassLoader(new URLClassLoader(jars.toArray(new URL[jars.size()])));
    }

    private void configureJmeterEnvironment() {
        config.getLogsHome().mkdirs();


        //empty classpath, JMeter will automatically assemble and add all JARs in #libDir and #libExtDir and add them to the classpath. Otherwise all jars will be in the classpath twice.
        System.setProperty("java.class.path", "");
        //JMeter uses the system property "user.dir" to set its base working directory
        System.setProperty("user.dir", new File(config.getExecutorHome().getAbsolutePath(), "bin").getAbsolutePath());
        //Prevent JMeter from throwing some System.exit() calls
        System.setProperty("jmeterengine.remote.system.exit", "false");
        System.setProperty("jmeterengine.stopfail.system.exit", "false");
        System.setProperty("jmeter.home", config.getExecutorHome().getAbsolutePath());
    }


    @Override
    public SystemTestResult execute(SystemTest test) throws Exception {
        Date startDate = new Date();
        executable.start(configConverter.prepareConfig(config, test));
//        NewDriver.main(configConverter.prepareConfig(config, test));
        waitForTestToFinish(UtilityFunctions.getThreadNames(false));
        return new SystemTestResult(test, startDate, new Date(), new File(config.getLogsHome(), test.getName() + ".jtl"));
    }

    /**
     * Wait for one of the JMeterThreads in the list to stop.
     */
    protected void waitForTestToFinish(List<String> threadNames) throws InterruptedException {
        Thread waitThread = null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            for (String threadName : threadNames) {
                if (threadName.equals(thread.getName())) {
                    waitThread = thread;
                    break;
                }
            }
        }
        if (waitThread != null) {
            waitThread.join();
        }
    }

    @Override
    public void initExecutor() {
        if (!config.isShowOutput()) {
            System.setOut(new PrintStream(new NullOutputStream()));
        }

        service.submit(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                boolean showOutput = false;
                while(latch.getCount() > 0) {
                    try {
                        if (scanner.hasNext() && scanner.next().equalsIgnoreCase("o")) {
                            showOutput = !showOutput;
                        }

                        if (showOutput) {
                            System.setOut(initialPrintStream);
                        } else {
                            System.setOut(new PrintStream(new NullOutputStream()));
                        }

                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void finish() {
        System.setOut(initialPrintStream);
        latch.countDown();
        service.shutdownNow();
    }
}
