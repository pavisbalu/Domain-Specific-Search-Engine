package edu.bits.wilp.ir_assignment.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.OutputStream;

public class KryoSerDe {
    private final static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
    }

    public static void writeToFile(Object value, OutputStream outputStream) {
        Output output = new Output(outputStream);
        kryo.writeObject(output, value);
    }
}
