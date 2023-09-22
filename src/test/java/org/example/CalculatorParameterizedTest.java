package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

@DisplayName("参数化测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorParameterizedTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @DisplayName("字符串值源")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    void simple(String value) {
        System.out.println(value);
    }

    @Order(2)
    @DisplayName("整数值源")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void simple2(int value) {
        System.out.println(value);
    }

    @Order(3)
    @DisplayName("枚举常量作为数据源")
    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, mode = EXCLUDE, names = {"DAYS", "HOURS"})
    void testWithEnumSource(TimeUnit timeUnit) {
        System.out.println(timeUnit.toString());
    }

    @Order(4)
    @DisplayName("工厂方法作为数据源")
    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithSimpleMethodSource(String argument) {
        System.out.println(argument);
    }

    //此类工厂方法必须返回流、可迭代、迭代器或参数数组
    static Stream<String> stringProvider() {
        return Stream.of("foo", "bar");
    }

    @Order(5)
    @DisplayName("工厂方法作为数据源（同名方法）")
    @ParameterizedTest
    @MethodSource
    void testWithSimpleMethodSourceHavingNoValue(String argument) {
        System.out.println(argument);
    }

    static Stream<String> testWithSimpleMethodSourceHavingNoValue() {
        return Stream.of("foo", "bar");
    }

    @Order(6)
    @DisplayName("工厂方法作为数据源（IntStream）")
    @ParameterizedTest
    @MethodSource
    void testWithRangeMethodSource(int argument) {
        System.out.println(argument);
    }

    static IntStream testWithRangeMethodSource() {
        return IntStream.range(0, 20).skip(10);
    }

    @Order(7)
    @DisplayName("工厂方法作为数据源（多个参数）")
    @ParameterizedTest
    @MethodSource
    void testWithMultiArgMethodSource(int input1, int input2, int expected) {
        int result = calculator.add(input1, input2);
        assertEquals(expected, result);
    }

    static Stream<Arguments> testWithMultiArgMethodSource() {
        return Stream.of(
                Arguments.of(-1, -2, -3),
                Arguments.of(0, 2, 2),
                Arguments.of(-1, 1, 0),
                Arguments.of(1, 2, 3)
        );
    }

    @Order(8)
    @DisplayName("参数列表表示为逗号分隔的值(@CsvSource)")
    @ParameterizedTest
    @CsvSource({"-1, -2, -3", "0, 2, 2", "-1, 1, 0", "1, 2, 3"})
    void testWithCsvSource(int input1, int input2, int expected) {
        int result = calculator.add(input1, input2);
        assertEquals(expected, result);
    }

    @Order(9)
    @DisplayName("参数来源于外部csv文件(@CsvFileSource)")
    @ParameterizedTest
    @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
//numLinesToSkip指跳过第几行
    void testWithCsvFileSource(String first, int second) {
        System.out.println(first + " " + second);
    }

    @Order(10)
    @DisplayName("自定义ArgumentsProvider")
    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        System.out.println(argument);
    }

}

class MyArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of("foo", "bar").map(Arguments::of);
    }
}