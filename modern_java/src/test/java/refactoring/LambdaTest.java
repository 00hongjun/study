package refactoring;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class LambdaTest {

    @Test
    public void before() throws Exception {
        //given
        UnaryOperator<String> first =
                (String text) -> text.trim();

        UnaryOperator<String> second =
                (String text) -> text.toUpperCase();

        Function<String, String> pipeline =
                first.andThen(second);

        String result = pipeline.apply(" abcd   ");
    }
}