package gdao.inherit;

import gdao.genericdao.GenericDAO;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Hart
 * @param <T>
 * @param <E>
 */
public class DBModel<T extends DBModel, E extends Object> {
	public T getById(E o) throws Exception {

		this.setPkVal(o);
		try {
			return (T) this.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			// system.out.println("inexisant");
			return null;
		}
	}

	public T getById(E o, Connection con) throws Exception {
		this.setPkVal(o);
		try {
			return (T) this.list(con).get(0);
		} catch (IndexOutOfBoundsException e) {
			// system.out.println("inexisant");
			return null;
		}
	}

	public ArrayList<T> listAll() throws Exception {
		ArrayList<T> temp = GenericDAO.get(this.getClass().newInstance(), null);
		return temp;
	}

	public ArrayList<T> listAll(Connection con) throws Exception {
		ArrayList<T> temp = GenericDAO.get(this.getClass().newInstance(), con);
		return temp;
	}

	public ArrayList<T> list() throws Exception {
		ArrayList<T> temp = GenericDAO.get(this, null);
		return temp;
	}

	public ArrayList<T> list(Connection con) throws Exception {
		ArrayList<T> temp = GenericDAO.get(this, con);
		return temp;
	}

	public int delete() throws Exception {
		return GenericDAO.delete(this, null);
	}

	public int delete(Connection con) throws Exception {
		return GenericDAO.delete(this, con);
	}

	public int save() throws Exception {
		this.setPkVal(null);
		return GenericDAO.save(this, null);
	}
	public int saveList(List<T> list) throws Exception {
		Connection con=GenericDAO.getConPost();
		int res=0;
		for (T t : list) {
			t.setPkVal(null);
			GenericDAO.save(t, con);
		}
		con.close();
		return res;
	}

	public int save(Connection con) throws Exception {
		this.setPkVal(null);
		return GenericDAO.save(this, con);
	}

	public int update(T mods) throws Exception {
		return GenericDAO.update(mods, this, null);
	}

	public int update(T mods, Connection con) throws Exception {
		return GenericDAO.update(mods, this, con);
	}
	@JsonIgnore
	public ArrayList<String> getColumnsNames() {
		ArrayList<String> names = new ArrayList<String>();
		Field[] liFi = GenericDAO.listAllFields(this.getClass());
		for (int i = 0; i < liFi.length; i++) {
			names.add(liFi[i].getName());
		}
		return names;
	}

	public HashMap<String, Object> listColumnValues()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		HashMap<String, Object> names = new HashMap<String, Object>();
		Field[] liFi = GenericDAO.listAllFields(this.getClass());
		for (int i = 0; i < liFi.length; i++) {
			names.put(liFi[i].getName(), GenericDAO.getter(liFi[i]).invoke(this));
		}
		return names;
	}

	@JsonIgnore
	public E getPkVal() throws Exception {
		Field fi = GenericDAO.getPK(this.getClass());
		return (E) GenericDAO.getter(fi).invoke(this);
	}

	public void setPkVal(E o) throws Exception {
		Field fi = GenericDAO.getPK(this.getClass());
		System.out.println("pk: " + fi.getName());
		GenericDAO.setter(fi).invoke(this, o);
	}

	@JsonIgnore
	public Vector getPossibleFk() throws Exception {
		Vector v = new Vector();
		ArrayList li = this.listAll();
		li.forEach((el) -> {
			try {
				v.add((DBModel) el);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		});
		return v;
	}
}
