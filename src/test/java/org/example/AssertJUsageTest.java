package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AssertJUsageTest {
    @Test
    public void testAssertJUsage1() {
        String name = "taozhaosheng";
        assertThat(name).as("name：%s", name).startsWith("tao").endsWith("eng");
    }

    @Test
    public void testAssertJUsage2() {
        List<String> names = Arrays.asList("zhangsan", "lisi", "wangwu");
        assertThat(names).as("names：%s", names).hasSize(names.size()).contains("lisi").doesNotContain("taozs");
    }

    @Test
    // exception assertion, standard style ...
    public void testAssertJUsage3() {
        assertThatThrownBy(() -> {
            throw new Exception("boom!");
        }).hasMessage("boom!");
    }

    @Test
    // ... or BDD style
    public void testAssertJUsage4() {
        Throwable thrown = catchThrowable(() -> {
            throw new Exception("boom!");
        });
        assertThat(thrown).hasMessageContaining("boom");
    }
}
