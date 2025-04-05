package code.challenge.commodity.rule.modular;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.GeneralRules;
import code.challenge.commodity.rule.modular.arithmetic.Minus;
import code.challenge.commodity.rule.modular.lit.Literal;
import code.challenge.currency.EUR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static code.challenge.commodity.rule.modular.arithmetic.BiOperation.*;
import static code.challenge.commodity.rule.modular.lit.CommodityValue.*;
public class ArithmeticRuleTest {
    Commodity commodity1 = null;
    Commodity commodity2 = null;

    @BeforeEach
    void setCommodity() {
        commodity1 = Commodity.of(
                "Test",
                new EUR(1),
                1,
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity1 creation failed")
        );

        commodity2 = Commodity.of(
                "Test",
                new EUR(1),
                -2,
                new ExpirationDate.ExpiresAt(LocalDate.of(1900, 1, 1)),
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity2 creation failed")
        );
    }

    @Test
    void biOperationOnlyForSameInputTypes() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> plus(Literal.of(1), Literal.of(1L)).apply(commodity1),
                "unequal input types on BiOperation"
        );
    }

    @Test
    void minusCorrect() {
        assertEquals(
                -10,
                new Minus<>(Literal.of((byte) 10)).apply(commodity1)
        );

        assertEquals(
                -10,
                new Minus<>(Literal.of((short) 10)).apply(commodity1)
        );

        assertEquals(
                -10,
                new Minus<>(Literal.of(10)).apply(commodity1)
        );

        assertEquals(
                -10L,
                new Minus<>(Literal.of(10L)).apply(commodity1)
        );

        assertEquals(
                -10.0f,
                new Minus<>(Literal.of(10.0f)).apply(commodity1)
        );

        assertEquals(
                -10.0d,
                new Minus<>(Literal.of(10.0d)).apply(commodity1)
        );

        assertEquals(
                new BigInteger("-1000000000000000"),
                new Minus<>(Literal.of(new BigInteger("1000000000000000"))).apply(commodity1)
        );

        assertEquals(
                new BigDecimal("-1000000000000000.0001"),
                new Minus<>(Literal.of(new BigDecimal("1000000000000000.0001"))).apply(commodity1)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> new Minus<>(Literal.of(new AtomicInteger(7))).apply(commodity1)
        );

        assertEquals(
                -1,
                new Minus<>(quality()).apply(commodity1)
        );
        assertEquals(
                2,
                new Minus<>(quality()).apply(commodity2)
        );
    }

    @Test
    void plusCorrect() {
        assertEquals(
                25,
                plus(
                        Literal.of((byte) 10),
                        Literal.of((byte) 15)
                ).apply(commodity1)
        );

        assertEquals(
                25,
                plus(
                        Literal.of((short) 10),
                        Literal.of((short) 15)
                ).apply(commodity1)
        );

        assertEquals(
                25,
                plus(
                        Literal.of(10),
                        Literal.of(15)
                ).apply(commodity1)
        );

        assertEquals(
                25L,
                plus(
                        Literal.of(10L),
                        Literal.of(15L)
                ).apply(commodity1)
        );

        assertEquals(
                25.0f,
                plus(
                        Literal.of(10.0f),
                        Literal.of(15.0f)
                ).apply(commodity1)
        );

        assertEquals(
                25.0d,
                plus(
                        Literal.of(10.0d),
                        Literal.of(15.0d)
                ).apply(commodity1)
        );

        assertEquals(
                new BigInteger("25000000000"),
                plus(
                        Literal.of(new BigInteger("10000000000")),
                        Literal.of(new BigInteger("15000000000"))
                ).apply(commodity1)
        );

        assertEquals(
                new BigDecimal("25000000000.00004"),
                plus(
                        Literal.of(new BigDecimal("10000000000.00002")),
                        Literal.of(new BigDecimal("15000000000.00002"))
                ).apply(commodity1)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> plus(Literal.of(new AtomicInteger(7)), Literal.of(new AtomicInteger(3))).apply(commodity1)
        );

        assertEquals(
                7,
                plus(
                        quality(),
                        Literal.of(6)
                ).apply(commodity1)
        );

        assertEquals(
                -4,
                plus(
                        quality(),
                        quality()
                ).apply(commodity2)
        );
    }

    @Test
    void multiplyCorrect() {
        assertEquals(
                10,
                multiply(
                        Literal.of((byte) 2),
                        Literal.of((byte) 5)
                ).apply(commodity1)
        );

        assertEquals(
                150,
                multiply(
                        Literal.of((short) 10),
                        Literal.of((short) 15)
                ).apply(commodity1)
        );

        assertEquals(
                150,
                multiply(
                        Literal.of(10),
                        Literal.of(15)
                ).apply(commodity1)
        );

        assertEquals(
                150L,
                multiply(
                        Literal.of(10L),
                        Literal.of(15L)
                ).apply(commodity1)
        );

        assertEquals(
                150.0f,
                multiply(
                        Literal.of(10.0f),
                        Literal.of(15.0f)
                ).apply(commodity1)
        );

        assertEquals(
                150.0d,
                multiply(
                        Literal.of(10.0d),
                        Literal.of(15.0d)
                ).apply(commodity1)
        );

        assertEquals(
                new BigInteger("150000000000000000000"),
                multiply(
                        Literal.of(new BigInteger("10000000000")),
                        Literal.of(new BigInteger("15000000000"))
                ).apply(commodity1)
        );

        assertEquals(
                new BigDecimal("150000000002500000000.01"),
                multiply(
                        Literal.of(new BigDecimal("10000000000.1")),
                        Literal.of(new BigDecimal("15000000000.1"))
                ).apply(commodity1)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> multiply(Literal.of(new AtomicInteger(7)), Literal.of(new AtomicInteger(3))).apply(commodity1)
        );

        assertEquals(
                6,
                multiply(
                        quality(),
                        Literal.of(6)
                ).apply(commodity1)
        );

        assertEquals(
                4,
                multiply(
                        quality(),
                        quality()
                ).apply(commodity2)
        );
    }

    @Test
    void moduloCorrect() {
        assertEquals(
                1,
                modulo(
                        Literal.of((byte) 5),
                        Literal.of((byte) 2)
                ).apply(commodity1)
        );

        assertEquals(
                5,
                modulo(
                        Literal.of((short) 15),
                        Literal.of((short) 10)
                ).apply(commodity1)
        );

        assertEquals(
                5,
                modulo(
                        Literal.of(15),
                        Literal.of(10)
                ).apply(commodity1)
        );

        assertEquals(
                5L,
                modulo(
                        Literal.of(15L),
                        Literal.of(10L)
                ).apply(commodity1)
        );

        assertEquals(
                5.0f,
                modulo(
                        Literal.of(15.0f),
                        Literal.of(10.0f)
                ).apply(commodity1)
        );

        assertEquals(
                5.0d,
                modulo(
                        Literal.of(15.0d),
                        Literal.of(10.0d)
                ).apply(commodity1)
        );

        assertEquals(
                new BigInteger("5000000000"),
                modulo(
                        Literal.of(new BigInteger("15000000000")),
                        Literal.of(new BigInteger("10000000000"))
                ).apply(commodity1)
        );

        assertEquals(
                new BigDecimal("0.0"),
                modulo(
                        Literal.of(new BigDecimal("15000000000.0")),
                        Literal.of(new BigDecimal("2.0"))
                ).apply(commodity1)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> modulo(Literal.of(new AtomicInteger(7)), Literal.of(new AtomicInteger(3))).apply(commodity1)
        );

        assertEquals(
                0,
                modulo(
                        Literal.of(6),
                        quality()
                ).apply(commodity1)
        );

        assertEquals(
                0,
                modulo(
                        quality(),
                        quality()
                ).apply(commodity2)
        );
    }
}