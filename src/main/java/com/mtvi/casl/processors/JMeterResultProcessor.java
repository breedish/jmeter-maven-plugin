package com.mtvi.casl.processors;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.domain.result.DetailedResult;
import com.mtvi.casl.domain.result.SystemTestResult;
import com.mtvi.casl.runner.ExecutionResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * JMeterResultProcessor class.
 */
public class JMeterResultProcessor implements ResultProcessor {

    private final JMeterTestResultParser parser;

    public JMeterResultProcessor() throws TestExecutionException {
        this.parser = new JAXBJMeterTestResultParser();
    }

    @Override
    public void process(ExecutionResult executionResult, ExecutionConfig config) throws TestExecutionException {
        for (SystemTestResult result : executionResult.getExecutionResults()) {
            result.bindResultExplanation(parseResult(result));
        }
    }

    private DetailedResult parseResult(SystemTestResult result) throws TestExecutionException {
        return parser.parse(result);
    }

    interface JMeterTestResultParser {

        DetailedResult parse(SystemTestResult systemTestResult) throws TestExecutionException;

    }

    static class JAXBJMeterTestResultParser implements JMeterTestResultParser {

        private final Unmarshaller unmarshaller;

        JAXBJMeterTestResultParser() throws TestExecutionException {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(DetailedResult.class);
                this.unmarshaller = jaxbContext.createUnmarshaller();
            } catch (Exception e) {
                throw new TestExecutionException("Error during configuration of results processor", e);
            }
        }

        @Override
        public DetailedResult parse(SystemTestResult systemTestResult) throws TestExecutionException {
            try {
                return (DetailedResult) this.unmarshaller.unmarshal(systemTestResult.getRawResult());
            } catch (JAXBException e) {
                throw new TestExecutionException(
                    String.format("Error during parsing results of \"%s\" system test",
                        systemTestResult.getTest().getName()), e);
            }
        }

    }

}
