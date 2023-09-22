package org.example;

import org.junit.jupiter.api.*;

import java.util.*;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@DisplayName("常用注解")
class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    @DisplayName("Ignore the test")
    void add() {
        int result = calculator.add(1, 2);
        assertEquals(result, 3);
    }

    @Test
    void groupedAssertions() {
        // 分组断言，执行分组中所有断言，分组中任何一个断言错误都会一起报告
        assertAll("person", () -> assertEquals("John", "John"), () -> assertEquals("Doe", "Doe"), () -> assertEquals("Doe2", "Doe2"));
    }

    @Test
    void dependentAssertions() {
        // 分组断言
        assertAll("properties", () -> {
            // 在代码块中，如果断言失败，后面的代码将不会运行
            String firstName = "John";
            assertNotNull(firstName);
            // 只有前一个断言通过才会运行
            assertAll("first name", () -> assertTrue(firstName.startsWith("J")),
                    () -> assertTrue(firstName.endsWith("n")));
        }, () -> {
            // 分组断言，不会受到first Name代码块的影响，所以即使上面的断言执行失败，这里的依旧会执行
            String lastName = "Doe";
            assertNotNull(lastName);
            // 只有前一个断言通过才会运行
            assertAll("last name", () -> assertTrue(lastName.startsWith("D")),
                    () -> assertTrue(lastName.endsWith("e")));
        });
    }

    @Test
    void exceptionTesting() {
        // 断言异常，抛出指定的异常，测试才会通过
        Throwable exception = assertThrows(ArithmeticException.class, () -> {
            calculator.divide(3, 0);
        });
        assertEquals("/ by zero", exception.getMessage());
    }

    @Test
    void timeoutNotExceeded() {
        // 断言超时
        assertTimeout(ofMinutes(2), () -> {
            // 完成任务小于2分钟时，测试通过。
        });
    }

    @Test
    void timeoutNotExceededWithResult() {
        // 断言成功并返回结果
        String actualResult = assertTimeout(ofMinutes(2), () -> {
            return "result";
        });
        assertEquals("result", actualResult);
    }

    @Test
    void timeoutExceeded() {
        // 断言超时，会在任务执行完毕后才返回，也就是1000毫秒后返回结果
        assertTimeout(ofMillis(10), () -> {
            // 执行任务花费时间1000毫秒
            Thread.sleep(1000);
        });
    }

    @Test
    void timeoutExceededWithPreemptiveTermination() {
        // 断言超时，如果在10毫秒内任务没有执行完毕，会立即返回断言失败，不会等到1000毫秒后
        assertTimeoutPreemptively(ofMillis(10), () -> {
            calculator.squareRoot(3);
        });
    }

    @Test
    public void testUsingJunitAssertThat() {
        // 字符串判断
        String s = "abcde";
        Assertions.assertTrue(s.startsWith("ab"));
        Assertions.assertTrue(s.endsWith("de"));
        Assertions.assertEquals(5, s.length());
        // 数字判断
        Integer i = 50;
        Assertions.assertTrue(i > 0);
        Assertions.assertTrue(i < 100);
        // 日期判断
        Date datel = new Date();
        Date date2 = new Date(datel.getTime() + 100);
        Date date3 = new Date(datel.getTime() - 100);
        Assertions.assertTrue(datel.before(date2));
        Assertions.assertTrue(datel.after(date3));
        // List判断
        List<String> list = Arrays.asList("a", "b", "c", "d");
        Assertions.assertEquals("a", list.get(0));
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals("d", list.get(list.size() - 1));
        // Map 判断
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        Set<String> set = map.keySet();
        Assertions.assertEquals(3, map.size());

        Assertions.assertTrue(set.containsAll(Arrays.asList("A", "B", "C")));
    }

    @Test
    public void testUsingAssertJ() {
        // 子符串判断
        String s = "abcde";
        assertThat(s).as("字符串判断，判断首尾及长度").startsWith("ab").endsWith("de").hasSize(5);
        // 数字判断
        Integer i = 150;
        assertThat(i).as("数字判断,数字大小比较").isGreaterThan(100).isLessThan(1000);
        // 日期判断
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 100);
        Date date3 = new Date(date1.getTime() - 100);
        assertThat(date1).as("日期判断：日期大小比较").isBefore(date2).isAfter(date3);
        // list比较
        List<String> list = Arrays.asList("a", "b", "c", "d");
        assertThat(list).as("list的首尾元素及长度").startsWith("a").endsWith("d").hasSize(4);
        // Map判断
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertThat(map).as("Map的长度及键值测试").hasSize(3).containsKeys("A", "B", "C");
    }

    @Test
//    @Disabled
    void assumeTrueTest() {
        //如果假设传入的值为True，那么就会执行后面测试，否则直接停止执行
        assumeTrue(false);
        System.out.println("This will not be implemented.");
    }

    @Test
    void assumeFalseTest() {
        //如果假设传入的值为false，那么就会执行后面测试，否则直接停止执行
        assumeFalse(true);
        System.out.println("This will not be implemented.");
    }

    @Test
    void assumingThatTest() {
        assumingThat(true,
                () -> {
                    System.out.println("This will not be implemented.");
                });

        //下面的输出将会执行
        System.out.println("This will be implemented.");
    }

    //自定义重复测试的显示名称
    @RepeatedTest(value = 10, name = "{displayName}-->{currentRepetition}/{totalRepetitions}")
    @DisplayName("repeatTest")
    void repeatedTest(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        //我们可以通过TestInfo在测试中获取测试的相关信息，比如输出自定义的测试名
        System.out.println(testInfo.getDisplayName());
        //输出当前重复次数
        System.out.println("currentRepetition:" + repetitionInfo.getCurrentRepetition());
    }
}