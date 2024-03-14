package pl.malcew.publicmentoringmalcew.model;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

public class WriterToAnswer implements Answer<String> {
    @Override
    public String answer(InvocationOnMock invocationOnMock) throws Throwable {
        Object[] args = invocationOnMock.getArguments();
        System.out.println("!!Answer called with: " );
        Arrays.stream(args).forEach(System.out::println);
        return (String) args[0];
    }
}
