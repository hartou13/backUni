package xcl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gdao.genericdao.GenericDAO;

public class Util {
    @JsonIgnore
	public static ArrayList<String> getColumnsNames(Object object) {
		ArrayList<String> names = new ArrayList<String>();
		Field[] liFi = GenericDAO.listAllFields(object.getClass());
		for (int i = 0; i < liFi.length; i++) {
			names.add(liFi[i].getName());
		}
		return names;
	}
    public static HashMap<String, Object> listColumnValues(Object object) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		HashMap<String, Object> names = new HashMap<String, Object>();
		Field[] liFi = GenericDAO.listAllFields(object.getClass());
		for (int i = 0; i < liFi.length; i++) {
			names.put(liFi[i].getName(), GenericDAO.getter(liFi[i]).invoke(object));
		}
		return names;
	}
}
