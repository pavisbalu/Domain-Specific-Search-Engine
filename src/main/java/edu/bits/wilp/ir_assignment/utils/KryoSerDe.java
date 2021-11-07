package edu.bits.wilp.ir_assignment.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;

/**
 * Utility wrapper for working with <a href="https://github.com/EsotericSoftware/kryo">Kryo</a> Framework.
 */
public class KryoSerDe {
    private final static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
    }

    public static void writeToFile(Object value, String fileName) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(fileName));
        kryo.writeObject(output, value);
        output.close();
    }

    public static <T> T readFromFile(String fileName, Class<T> type) throws FileNotFoundException {
        Input input = new Input(new FileInputStream(fileName));
        Object object = kryo.readObject(input, type);
        return (T) object;
    }
}
