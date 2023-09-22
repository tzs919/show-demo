package org.example;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int substract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
//        if (b == 0) {
//            throw new ArithmeticException("not 0");
//        }
        return a / b;
    }

    public void squareRoot(int n) {
        for (; ; ) ; //错误：无限循环
    }
}