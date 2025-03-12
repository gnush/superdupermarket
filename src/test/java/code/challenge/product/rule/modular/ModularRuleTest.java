package code.challenge.product.rule.modular;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
class ModularRuleTest {
    @Test
    @DisplayName("identity rule application")
    void identityRule() {
        assertEquals(0, new SimpleRule<Integer, Integer>(i -> i).apply(0));
    }

    @Test
    @DisplayName("invalidating rule application")
    void invalidatingRule() {
        assertEquals(false, new SimpleRule<Integer, Boolean>(_ -> false).apply(0));
    }

    @Test
    @DisplayName("compose id(_+1)")
    void ruleComposition1() {
        assertEquals(
                1,
                new ComposeRule<Integer, Integer, Integer>(
                        SimpleRule.identity(),
                        i -> i+1
                ).apply(0));
    }

    @Test
    @DisplayName("compose (_+3)*2")
    void ruleComposition2() {
        var doubling = new SimpleRule<Integer, Integer>(i -> i*2);
        Function<Integer, Integer> plus3 = i -> i+3;

        assertEquals(
                6,
                new ComposeRule<>(doubling, plus3).apply(0));
    }

    @Test
    @DisplayName("compose isEven(*2)")
    void ruleComposition3() {
        var isEven = new SimpleRule<Integer, Boolean>(i -> i%2==0);
        Function<Integer, Integer> doubling = i -> i*2;

        var rule = new ComposeRule<>(isEven, doubling);

        assertEquals(
                true,
                rule.apply(1));
        assertEquals(
                true,
                rule.apply(2));
    }

    @Test
    @DisplayName("id andThen +1")
    void ruleAndThen1() {
        assertEquals(
                1,
                new AndThenRule<Integer, Integer, Integer>(
                        SimpleRule.identity(),
                        i -> i+1
                ).apply(0));
    }

    @Test
    @DisplayName("*2 andThen +3")
    void ruleAndThen2() {
        var doubling = new SimpleRule<Integer, Integer>(i -> i*2);
        Function<Integer, Integer> plus3 = i -> i+3;

        assertEquals(
                3,
                new AndThenRule<>(doubling, plus3).apply(0));
    }

    @Test
    @DisplayName("*2 andThen isEven")
    void ruleAndThen3() {
        var doubling = new SimpleRule<Integer, Integer>(i -> i*2);
        Function<Integer, Boolean> isEven = i -> i%2==0;

        var rule = new AndThenRule<>(doubling, isEven);

        assertEquals(
                true,
                rule.apply(1));
        assertEquals(
                true,
                rule.apply(2));
    }

    @Test
    @DisplayName("(_*2) + (_+3)")
    void mergeRules1() {
        var doubling = new SimpleRule<Integer, Integer>(i -> i*2);
        var minus3 = new SimpleRule<Integer, Integer>(i -> i-3);

        var rule = new MergeRule<>(doubling, minus3, Integer::sum);

        assertEquals(
                9,
                rule.apply(4)
        );
    }

    @Test
    @DisplayName("isEven | >7")
    void mergeRules2() {
        var isEven = new SimpleRule<Integer, Boolean>(i -> i%2==0);
        var greater7 = new SimpleRule<Integer, Boolean>(i -> i>7);

        var rule = new MergeRule<>(isEven, greater7, Boolean::logicalOr);

        assertEquals(
                false,
                rule.apply(1)
        );

        assertEquals(
                true,
                rule.apply(4)
        );

        assertEquals(
                true,
                rule.apply(9)
        );
    }

    @Test
    @DisplayName("merge differently typed rules")
    void mergeRules3() {
        var isNonBlank = new SimpleRule<String, Boolean>(s -> !s.isBlank());
        var length = new SimpleRule<>(String::length);

        // String is empty    -> NaN
        // string length <= 3 -> -Inf
        // string length > 3  -> +Inf
        BiFunction<Boolean, Integer, Double> merge = (b, i) -> b ? (i > 3 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY) : Double.NaN;

        var rule = new MergeRule<>(isNonBlank, length, merge);

        assertEquals(
                Double.NaN,
                rule.apply("")
        );

        assertEquals(
                Double.NEGATIVE_INFINITY,
                rule.apply("NaN")
        );

        assertEquals(
                Double.POSITIVE_INFINITY,
                rule.apply("whoop")
        );
    }
}