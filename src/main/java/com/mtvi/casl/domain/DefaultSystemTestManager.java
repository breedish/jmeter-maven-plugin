package com.mtvi.casl.domain;

import com.google.common.collect.Sets;
import com.mtvi.casl.config.ExecutionConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * DefaultSystemTestManager.
 */
public class DefaultSystemTestManager implements SystemTestManager {

    /**
     * Tests Scanner.
     */
    private final SystemTestScanner systemTestScanner;

    /**
     * Default Constructor.
     */
    public DefaultSystemTestManager() {
        this.systemTestScanner = new ByExtensionSystemTestScanner();
    }

    @Override
    public Set<SystemTest> findSystemTests(ExecutionConfig config) throws TestExecutionException {
        Set<SystemTest> tests = Sets.newTreeSet(new Comparator<SystemTest>() {
            @Override
            public int compare(SystemTest test1, SystemTest test2) {
                return test1.getName().compareTo(test2.getName());
            }
        });

        for (File testSrc : this.systemTestScanner.scan(config.getSourcePath().getAbsolutePath())) {
            tests.add(new SystemTest(testSrc.getName(), testSrc));
        }

        return tests;
    }

    interface SystemTestScanner {

        Collection<File> scan(String srcPath) throws TestExecutionException;

    }

    class ByExtensionSystemTestScanner implements SystemTestScanner {

        /**
         * Default test extension.
         */
        private static final String JMETER_EXTENSION = "jmx";

        @Override
        public Collection<File> scan(String srcPath) throws TestExecutionException {
            File source = new File(srcPath);
            if (!source.exists()) {
                throw new TestExecutionException(String.format("Jmeter source path doesn't exist: [path=%s]", srcPath));
            }

            return FileUtils.listFiles(source, new SuffixFileFilter(JMETER_EXTENSION), TrueFileFilter.INSTANCE);
        }

    }



}
