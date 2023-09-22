package org.example;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class MockitoUsageTest {
    //模拟接口
    @Test
    public void testMockitoInterface() {
        // 创建mock对象
        List mockedList = mock(List.class);

        // 使用模拟对象(而不是真实对象)
        mockedList.add("one");
        mockedList.clear();

        // 验证方法是否被调用
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    //模拟具体类
    @Test
    public void testMockitoConcreteClass() {
        // you can mock concrete classes, not only interfaces
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");

        // the following prints "first"
        System.out.println(mockedList.get(0));

        // the following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

//        verify(mockedList,atLeastOnce()).get(anyInt());
    }

    //可选参数
    @Test
    public void returnsSmartNullsTest() {
        List mock = mock(List.class, RETURNS_SMART_NULLS);

        //使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。另外控制台窗口会提示信息“SmartNull returned by this unstubbed method call on a mock:list.get(0);”
        System.out.println(mock.get(0));

        System.out.println(mock.toArray().length);
    }

    //多个thenReturn
    @Test
    public void when_thenReturn() {
        //mock一个Iterator类
        Iterator iterator = mock(Iterator.class);

        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        when(iterator.next()).thenReturn("hello").thenReturn("world");

        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();

        //验证结果
        assertEquals("hello world world", result);
//        verify(iterator,atLeastOnce()).next();
    }

    //模拟方法体抛出异常
    @Test
    public void when_thenThrow() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        //预设当流关闭时抛出异常
        doThrow(new IOException()).when(outputStream).close();
//        when(outputStream.close()).thenThrow()

//        outputStream.close();

        assertThrows(IOException.class, () -> {
            writer.close();
        });

    }

    @Test
    public void test_spy() throws IOException {
        List list = new LinkedList();
        List spy = spy(list);

        when(spy.size()).thenReturn(100);
//        when(spy.get(0)).thenReturn("foo");
//        doReturn("foo").when(spy).get(0);

        spy.add("one");
        spy.add("two");

        System.out.println(spy.get(0));
        System.out.println(spy.size());

        verify(spy).add("one");
        verify(spy).add("two");
    }

    @Test
    public void test_spy2() {
        // spy 允许我们部分 mock 一个类, 也可以使用 @Spy 创建
        List<String> data = spy(new ArrayList<>());

        // stubbing size 方法
        doReturn(10).when(data).size();

        // 调用真实的方法
        data.add("a");

        // 验证
        assertEquals("a", data.get(0));
        assertEquals(10, data.size());
    }

    @Test
    public void behaviorDrivenDevTest() {
        // BDD(Behavior Driven Development): Given When Then
        List<String> data = mock(List.class);

        // Given
        given(data.size()).willReturn(10);

        // When
        int actual = data.size();

        // Then
        assertThat(actual).isEqualTo(10);
    }
}

//
//    @Mock
//    private List list5;
//
//    public MyMockitoTest() {
//        initMocks(this);//import static org.mockito.MockitoAnnotations.initMocks;
//    }
//
//    //模拟接口
//    @Test
//    public void testMockitoInterface0() {
//
//        // 使用模拟对象(而不是真实对象)
//        list5.add("one");
//        list5.clear();
//
//        // 验证方法是否被调用
//        verify(list5).add("one");
//        verify(list5).clear();
//    }