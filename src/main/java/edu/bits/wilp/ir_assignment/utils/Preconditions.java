package edu.bits.wilp.ir_assignment.utils;

public class Preconditions {
    public static void isNotNull(Object value) {
        if(value == null) {
            throw new RuntimeException("Input value is null");
        }
    }
}
