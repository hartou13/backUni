package universalController.classmanaging;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import gdao.inherit.DBModel;
import gdao.inherit.Hija;
import gdao.inherit.Madre;
import universalController.classmanaging.fd.*;
public class Util {
    public static Field[] listAllFields(Class cl) {
        ArrayList<Field> fi = new ArrayList<>();

        fi.addAll(Arrays.asList(cl.getDeclaredFields()));
        while (cl.getSuperclass() != DBModel.class && cl.getSuperclass() != Madre.class
                && cl.getSuperclass() != Hija.class && cl.getSuperclass() != Object.class) {
            cl = cl.getSuperclass();
            // system.out.println(cl);
            fi.addAll(Arrays.asList(cl.getDeclaredFields()));
        }

        Field[] res = new Field[fi.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = fi.get(i);
        }
        return res;
    }
    public static FieldDescriptor getFieldDescriptor(Field fi) {
        return (FieldDescriptor) fi.getAnnotation(FieldDescriptor.class);
    }
}
