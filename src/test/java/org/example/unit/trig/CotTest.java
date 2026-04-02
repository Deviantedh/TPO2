package org.example.unit.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.trig.Cos;
import org.example.trig.Cot;
import org.example.trig.Sin;
import org.example.trig.Tan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.*;

class CotTest {

    private static final BigDecimal PRECISION = new BigDecimal("0.0000001");
    private static final double DELTA = 0.000001;
    private static final MathContext MC = new MathContext(25, RoundingMode.HALF_EVEN);
    private static final BigDecimal PI = new BigDecimal(Math.PI, MC);
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal HALF_PI = PI.divide(TWO, MC);
    private static final BigDecimal THREE = new BigDecimal("3");
    private Cot cot;

    @BeforeEach
    void init() {
        Sin sin = new Sin();
        Cos cos = new Cos(sin);
        Tan tan = new Tan(sin, cos);
        cot = new Cot(tan);
    }

    // Проверяет специальное значение cot(pi/4) = 1.
    @Test
    void shouldCalculateForPiFour() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal expected = ONE.setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = cot.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет специальное значение cot(pi/6).
    @Test
    void shouldCalculateForPiSix() {
        BigDecimal arg = PI.divide(new BigDecimal("6"), MC);
        BigDecimal expected = new BigDecimal("1.7320508").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = cot.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет специальное значение cot(pi/3).
    @Test
    void shouldCalculateForPiThree() {
        BigDecimal arg = PI.divide(new BigDecimal("3"), MC);
        BigDecimal expected = new BigDecimal("0.5773503").setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = cot.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет точку разрыва cot(x) в нуле.
    @Test
    void shouldNotCalculateForZero() {
        assertThrows(ArithmeticException.class, () -> cot.calculate(ZERO, PRECISION));
    }

    // Проверяет точку разрыва cot(x) в pi.
    @Test
    void shouldNotCalculateForPi() {
        assertThrows(ArithmeticException.class, () -> cot.calculate(PI, PRECISION));
    }

    // Проверяет точку разрыва cot(x) в -pi.
    @Test
    void shouldNotCalculateForNegativePi() {
        assertThrows(ArithmeticException.class, () -> cot.calculate(PI.negate(), PRECISION));
    }

    // Проверяет точку разрыва cot(x) в 2pi.
    @Test
    void shouldNotCalculateForTwoPi() {
        BigDecimal twoPi = TWO.multiply(PI, MC);
        assertThrows(ArithmeticException.class, () -> cot.calculate(twoPi, PRECISION));
    }

    // Проверяет, что cot(pi/2) в реализации трактуется как недопустимое значение.
    @Test
    void shouldNotCalculateForPiHalf() {
        BigDecimal arg = HALF_PI;
        assertThrows(ArithmeticException.class, () -> cot.calculate(arg, PRECISION));
    }

    // Проверяет, что cot(-pi/2) в реализации трактуется как недопустимое значение.
    @Test
    void shouldNotCalculateForNegativePiHalf() {
        BigDecimal arg = HALF_PI.negate();
        assertThrows(ArithmeticException.class, () -> cot.calculate(arg, PRECISION));
    }

    // Проверяет, что cot(3pi/2) в реализации трактуется как недопустимое значение.
    @Test
    void shouldNotCalculateForThreePiHalf() {
        BigDecimal threePiHalf = THREE.multiply(HALF_PI, MC);
        assertThrows(ArithmeticException.class, () -> cot.calculate(threePiHalf, PRECISION));
    }

    // Проверяет нечетность функции на аргументе -pi/4.
    @Test
    void shouldCalculateForNegativePiFour() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC).negate();
        BigDecimal expected = ONE.negate().setScale(PRECISION.scale(), HALF_EVEN);
        BigDecimal actual = cot.calculate(arg, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }

    // Проверяет знак cot(x) справа от асимптоты в нуле.
    @Test
    void shouldCalculateNearAsymptoteFromRight() {
        BigDecimal epsilon = new BigDecimal("0.0001");
        BigDecimal arg = epsilon;
        BigDecimal result = cot.calculate(arg, PRECISION);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    // Проверяет знак cot(x) слева от асимптоты в нуле.
    @Test
    void shouldCalculateNearAsymptoteFromLeft() {
        BigDecimal epsilon = new BigDecimal("0.0001");
        BigDecimal arg = epsilon.negate();
        BigDecimal result = cot.calculate(arg, PRECISION);
        assertTrue(result.compareTo(BigDecimal.ZERO) < 0);
    }

    // Проверяет нечетность функции на паре противоположных аргументов.
    @Test
    void shouldBeOddFunction() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal positive = cot.calculate(arg, PRECISION);
        BigDecimal negative = cot.calculate(arg.negate(), PRECISION);
        assertEquals(positive.doubleValue(), negative.negate().doubleValue(), DELTA);
    }

    // Проверяет периодичность cot(x) с периодом pi.
    @Test
    void shouldHavePeriodPi() {
        BigDecimal arg = PI.divide(new BigDecimal("4"), MC);
        BigDecimal argPlusPi = arg.add(PI, MC);
        BigDecimal expected = cot.calculate(arg, PRECISION);
        BigDecimal actual = cot.calculate(argPlusPi, PRECISION);
        assertEquals(expected.doubleValue(), actual.doubleValue(), DELTA);
    }
}
