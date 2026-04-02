package org.example.unit.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.trig.Csc;
import org.example.trig.Sin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.*;

class CscTest {

    private static final BigDecimal PRECISION = new BigDecimal("0.0000001");
    private static final double DELTA = 0.000001;
    private static final MathContext MC = new MathContext(25, RoundingMode.HALF_EVEN);
    private static final BigDecimal PI = new BigDecimal(Math.PI, MC);
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal HALF_PI = PI.divide(TWO, MC);
    private static final BigDecimal THREE = new BigDecimal("3");
    private Csc csc;

    @BeforeEach
    void init() {
        Sin sin = new Sin();
        csc = new Csc(sin);
    }

    // Проверяет значение csc(pi/2) = 1.
    @Test
    void shouldCalculateForPiHalf() {
        BigDecimal arg = HALF_PI;
        BigDecimal expected = ONE.setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет значение csc(-pi/2) = -1.
    @Test
    void shouldCalculateForNegativePiHalf() {
        BigDecimal arg = HALF_PI.negate();
        BigDecimal expected = ONE.negate().setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет значение csc(3pi/2) = -1.
    @Test
    void shouldCalculateForThreePiHalf() {
        BigDecimal threePiHalf = THREE.multiply(HALF_PI, MC);
        BigDecimal expected = ONE.negate().setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(threePiHalf, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет специальное значение csc(pi/6) = 2.
    @Test
    void shouldCalculateForPiSix() {
        BigDecimal arg = PI.divide(new BigDecimal("6"), MC);
        BigDecimal expected = new BigDecimal("2").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет специальное значение csc(pi/4).
    @Test
    void shouldCalculateForPiFour() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal expected = new BigDecimal("1.4142136").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет специальное значение csc(pi/3).
    @Test
    void shouldCalculateForPiThree() {
        BigDecimal arg = PI.divide(new BigDecimal("3"), MC);
        BigDecimal expected = new BigDecimal("1.1547005").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет точку разрыва csc(x) в нуле.
    @Test
    void shouldNotCalculateForZero() {
        assertThrows(ArithmeticException.class, () -> csc.calculate(ZERO, PRECISION));
    }

    // Проверяет точку разрыва csc(x) в pi.
    @Test
    void shouldNotCalculateForPi() {
        assertThrows(ArithmeticException.class, () -> csc.calculate(PI, PRECISION));
    }

    // Проверяет точку разрыва csc(x) в -pi.
    @Test
    void shouldNotCalculateForNegativePi() {
        assertThrows(ArithmeticException.class, () -> csc.calculate(PI.negate(), PRECISION));
    }

    // Проверяет точку разрыва csc(x) в 2pi.
    @Test
    void shouldNotCalculateForTwoPi() {
        BigDecimal twoPi = TWO.multiply(PI, MC);
        assertThrows(ArithmeticException.class, () -> csc.calculate(twoPi, PRECISION));
    }

    // Проверяет разрыв функции на большом кратном pi.
    @Test
    void shouldNotCalculateForLargeValue() {
        BigDecimal hundredPi = new BigDecimal("100").multiply(PI, MC);
        assertThrows(ArithmeticException.class, () -> csc.calculate(hundredPi, PRECISION));
    }

    // Проверяет нечетность функции на аргументе -pi/6.
    @Test
    void shouldCalculateForNegativePiSix() {
        BigDecimal arg = PI.divide(new BigDecimal("6"), MC).negate();
        BigDecimal expected = new BigDecimal("-2").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = csc.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет знак csc(x) справа от асимптоты в нуле.
    @Test
    void shouldCalculateNearAsymptoteFromRight() {
        BigDecimal epsilon = new BigDecimal("0.0001");
        BigDecimal arg = epsilon;
        BigDecimal result = csc.calculate(arg, PRECISION);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    // Проверяет знак csc(x) слева от асимптоты в pi.
    @Test
    void shouldCalculateNearAsymptoteFromLeft() {
        BigDecimal epsilon = new BigDecimal("0.0001");
        BigDecimal arg = PI.subtract(epsilon, MC);
        BigDecimal result = csc.calculate(arg, PRECISION);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    // Проверяет знак csc(x) около отрицательной асимптоты.
    @Test
    void shouldCalculateNearAsymptoteFromLeftNegative() {
        BigDecimal epsilon = new BigDecimal("0.0001");
        BigDecimal arg = PI.negate().add(epsilon, MC);
        BigDecimal result = csc.calculate(arg, PRECISION);
        assertTrue(result.compareTo(BigDecimal.ZERO) < 0);
    }

    // Проверяет нечетность функции на противоположных аргументах.
    @Test
    void shouldBeOddFunction() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal positive = csc.calculate(arg, PRECISION);
        BigDecimal negative = csc.calculate(arg.negate(), PRECISION);
        assertEquals(positive.doubleValue(), negative.negate().doubleValue(), DELTA);
    }

    // Проверяет периодичность csc(x) с периодом 2pi.
    @Test
    void shouldHavePeriodTwoPi() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal argPlusTwoPi = arg.add(TWO.multiply(PI, MC), MC);
        BigDecimal expected = csc.calculate(arg, PRECISION);
        BigDecimal actual = csc.calculate(argPlusTwoPi, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }
}
