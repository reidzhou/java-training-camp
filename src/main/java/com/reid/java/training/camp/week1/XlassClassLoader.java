package com.reid.java.training.camp.week1;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

public class XlassClassLoader extends ClassLoader {

    private static final String SUFFIX = ".xlass";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (name.endsWith(SUFFIX)) {
            try {
                clazz = findClass(new XlassFile(name));
            } catch (IOException e) {

            }
        } else {
            clazz = super.findClass(name);
        }
        return clazz;
    }

    private Class<?> findClass(XlassFile xlassFile) {
        byte[] bytes = xlassFile.getBytes();

        // 字节取反
        for (int i = 0, size = bytes.length; i < size; i++) {
            bytes[i] = (byte) ~bytes[i];
        }

        return defineClass(xlassFile.getClazzName(), bytes, 0, bytes.length);
    }

    public static class XlassFile {

        private String path;

        private String clazzName;

        private byte[] bytes;

        public XlassFile(String path) throws IOException {
            this.path = path;
            this.clazzName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf("."));
            this.bytes = Files.readAllBytes(Path.of(path));
        }

        public String getPath() {
            return path;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String getClazzName() {
            return clazzName;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        XlassClassLoader classLoader = new XlassClassLoader();
        Class<?> clazz = classLoader.loadClass(new File("").getAbsolutePath() + "\\src\\main\\java\\com\\reid\\java\\training\\camp\\week1\\Hello.xlass");

        Method method = clazz.getMethod("hello");
        method.invoke(clazz.getDeclaredConstructor().newInstance());
    }
}
