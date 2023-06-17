package gdao.genericdao;

import gdao.confreader.reader.Reader;
import gdao.genericdao.exception.DatabaseConfException;
import gdao.inherit.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.Arrays;
import java.util.Date;

import javax.print.attribute.standard.Fidelity;

import org.postgresql.util.PGInterval;

public class GenericDAO {

    public static String getMainClassPath() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = "";
        Enumeration<URL> resources = classLoader.getResources(path);
        String tempPath = "";
        boolean isApacheTomcatWebProject = false;
        // system.out.println("eto");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            // system.out.println("------------ main " +resource);
            tempPath = resource.getFile();
            if (!tempPath.startsWith("jar") && tempPath.endsWith("classes/")) {
                isApacheTomcatWebProject = true;
                break;
            }
        }
        // //system.out.println(tempPath.subSequence(1,tempPath.indexOf("build")));
        // system.out.println(tempPath);
        return isApacheTomcatWebProject ? (tempPath.subSequence(1, tempPath.indexOf("build")).toString()) : "";
    }

    public static void check(String file) throws IOException, DatabaseConfException {
        File f = new File(file);
        if (!f.exists()) {
            // system.out.println("Checker le fichier " + f.getAbsolutePath() + " avant de
            // continuer");

            f.createNewFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("port=5432\n");
                bw.write("database=<dbname>\n");
                bw.write("mdp=root\n");
                bw.write("user=postgres\n");
                bw.write("driver=org.postgresql.Driver\n");
                bw.write("url=jdbc:postgresql://localhost:\n");
            }
            throw new DatabaseConfException("fichier créé, pensez à le modifier");
        }
    }

    public static Field getPK(Class cl) {
        Field[] liFi = listAllFields(cl);
        for (Field liFi1 : liFi) {
            if (getColumnName(liFi1).pk()) {
                return liFi1;
            }
        }
        // system.out.println("tsisy primary key---");
        return null;
    }

    public static Method setter(Field f) {
        // system.out.println(f.getName());
        try {
            Class clTemp = f.getDeclaringClass();
            Class cFi = f.getType();
            Method set = clTemp.getMethod("set" + UCFirst(f.getName()), cFi);
            return set;
        } catch (NoSuchMethodException ex) {
            // system.out.println("No such method " + "set" + UCFirst(f.getName()));
            return null;
        }

    }

    public static Method getter(Field f) {
        try {
            Class clTemp = f.getDeclaringClass();
            Method set = clTemp.getMethod("get" + UCFirst(f.getName()));
            return set;
        } catch (NoSuchMethodException ex) {
            // system.out.println("No such method " + "get" + UCFirst(f.getName()));
            return null;
        }
    }

    public static Connection getConPost()
            throws IOException, ClassNotFoundException, SQLException, DatabaseConfException {
        String filename ="databaseManager.hart.conf";
        check(filename);
        // system.out.println(filename);
        HashMap<String, String> conf = new Reader().read(filename);
        Class.forName(conf.get("driver"));
        Connection con;
        String tempURL = conf.get("url")+":"+ conf.get("port") + "/" + conf.get("database");
        // system.out.println(tempURL);
        con = DriverManager.getConnection(tempURL, conf.get("user"), conf.get("mdp"));
        return con;
    }

    public static TableName getTableName(Class cl) {
        return (TableName) cl.getAnnotation(TableName.class);
    }

    public static VueName getVueName(Class cl) {
        return (VueName) cl.getAnnotation(VueName.class);
    }

    public static ColumnName getColumnName(Field fi) {
        ColumnName temp = fi.getAnnotation(ColumnName.class);
        if (temp != null) {
            // system.out.println("Champ " + fi.getName() + " column name");
            return temp;
        }
        return null;
    }

    public static String UCFirst(String name) {
        char[] charLi = name.toCharArray();
        String res = "" + charLi[0];
        res = res.toUpperCase();
        for (int i = 1; i < charLi.length; i++) {
            res += charLi[i];
        }
        return res;
    }

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

    public static String checkTableName(Object o, String sql) {
        if (getTableName(o.getClass()) != null) {
            System.out.println("nom de table altéré");
            sql += getTableName(o.getClass()).value();
        } else {
            sql += o.getClass().getSimpleName();
        }
        // TableName tn=getTableName(o.getClass());

        return sql;
    }
    public static String checkSelectTableName(Object o, String sql) {
        if (getVueName(o.getClass()) != null) {
            System.out.println("nom de vue altéré");
            sql += getVueName(o.getClass()).value();
        } 
        else if (getTableName(o.getClass()) != null) {
            System.out.println("nom de table altéré");
            sql += getTableName(o.getClass()).value();
        } else {
            sql += o.getClass().getSimpleName();
        }
        // TableName tn=getTableName(o.getClass());

        return sql;
    }
    public static String checkTableName(Class cl, String sql) {
        if (getTableName(cl) != null) {
            System.out.println("nom de table altéré");
            sql += getTableName(cl).value();
        } else {
            sql += cl.getSimpleName();
        }
        // TableName tn=getTableName(o.getClass());

        return sql;
    }

    public static void updateList(ArrayList<String> listChamp, ArrayList listObj, Object o) throws Exception {
        // system.out.println(o);
        Field[] liFi = listAllFields(o.getClass());
        for (int i = 0; i < liFi.length; i++) {
            try {
                Method get = getter(liFi[i]);
                if(get==null)
                    throw new Exception("fonction get "+liFi[i].getName()+" absente");
                Object tempo = get.invoke(o);
                if (tempo != null) {
                    ColumnName cn = getColumnName(liFi[i]);
                    if (cn != null) {
                        if (cn.value().length() == 0) {
                            listChamp.add(liFi[i].getName());
                        } else {
                            listChamp.add(cn.value());
                        }
                        // listChamp.add(liFi[i]);
                        if (cn.fk()) {
                            // system.out.println("fk");
                            Class clTemp = tempo.getClass();
                            Field[] liMeth = listAllFields(clTemp);
                            for (int j = 0; j < liMeth.length; j++) {
                                ColumnName tempCN = getColumnName(liMeth[j]);
                                if (tempCN.pk()) {
                                    Method get2 = getter(liMeth[j]);
                                    listObj.add(get2.invoke(tempo));
                                }
                            }
                        } else if (tempo instanceof java.time.Duration) {
                            Duration duration = (Duration) tempo;
                            duration.getSeconds();
                            listObj.add(new PGInterval(0, 0, 0, 0, 0, duration.getSeconds()));
                        } else {
                            listObj.add(tempo);
                        }
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }
    }

    public static void listGetable(ArrayList<String> champSelect, ArrayList<Field> fiSetable, Object o) {
        Field[] liFi = listAllFields(o.getClass());
        for (int i = 0; i < liFi.length; i++) {
            try {
                Method get = getter(liFi[i]);
                Object tempo = get.invoke(o);

                ColumnName cn = getColumnName(liFi[i]);
                if (cn != null) {
                    if (cn.value().length() == 0) {
                        champSelect.add(liFi[i].getName());
                    } else {
                        champSelect.add(cn.value());
                    }
                    // champSelect.add(liFi[i]);
                    fiSetable.add(liFi[i]);
                }

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Connection initCO(Boolean initCO, Connection con) throws DatabaseConfException {
        if (con == null) {
            initCO = true;
            try {
                con = GenericDAO.getConPost();
            } catch (Exception ex) {
                System.gc();
                ex.printStackTrace();
                // system.out.println("tsy nety fangalana connection");
                throw new DatabaseConfException(ex.getMessage());
            }
        }
        return con;
    }

    public static int save(Object o, Connection con) throws Exception {
        Boolean initCO = false;
        con = initCO(initCO, con);
        ArrayList<String> listChamp = new ArrayList<>();
        ArrayList listObj = new ArrayList();
        String sql = "INSERT INTO ";
        sql = checkTableName(o, sql);
        updateList(listChamp, listObj, o);
        String col = " (";
        String val = " values (";
        for (int i = 0; i < listChamp.size(); i++) {
            col += listChamp.get(i);
            val += "?";
            if (i != listChamp.size() - 1) {
                col += ",";
                val += ",";
            }
        }
        col += ") ";
        val += ")";
        sql += col + val;
        System.out.println(sql);
        int res = 0;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            setObjectInStatement(listObj, stmt);
            System.out.println(stmt);
            res = stmt.executeUpdate();
        }
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }

    public static void setObjectInStatement(ArrayList listObj, PreparedStatement stmt) throws SQLException {
        for (int i = 1; i < listObj.size() + 1; i++) {
            if (listObj.get(i - 1) instanceof java.util.Date) {
                Date dTemp = (Date) listObj.get(i - 1);
                stmt.setDate(i, new java.sql.Date(dTemp.getTime()));
                stmt.setTimestamp(i, new java.sql.Timestamp(dTemp.getTime()));
            } else {
                stmt.setObject(i, listObj.get(i - 1));
            }
            // stmt.setString(i, listObj.get(i-1).toString());
        }
    }

    public static int delete(Object o, Connection con) throws Exception {
        Boolean initCO = false;

        con = initCO(initCO, con);
        ArrayList<String> listChamp = new ArrayList<>();
        ArrayList listObj = new ArrayList();
        String sql = "DELETE FROM ";
        sql = checkTableName(o, sql);
        sql += " where 1=1 ";
        updateList(listChamp, listObj, o);
        // String cond="";
        for (int i = 0; i < listChamp.size(); i++) {
            sql += " and " + listChamp.get(i) + "=? ";
        }
        // system.out.println(sql);
        PreparedStatement stmt = con.prepareStatement(sql);
        setObjectInStatement(listObj, stmt);
        System.out.println(stmt);

        int res = stmt.executeUpdate();
        stmt.close();
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }

    public static int update(Object taloha, Object vaovao, Connection con) throws Exception, DatabaseConfException {
        Boolean initCO = false;
        con = initCO(initCO, con);
        ArrayList<String> listChamp = new ArrayList<>();
        ArrayList<String> listChamp2 = new ArrayList<>();
        ArrayList listObj = new ArrayList();
        ArrayList listObj2 = new ArrayList();
        String sql = "UPDATE ";
        sql = checkTableName(taloha, sql);
        sql += " set ";
        updateList(listChamp, listObj, taloha);
        updateList(listChamp2, listObj2, vaovao);
        for (int i = 0; i < listChamp.size(); i++) {
            if (i != 0) {
                sql += ",";
            }
            sql += listChamp.get(i) + "=? ";
            // system.out.println(i);
        }
        sql += "where 1=1 ";
        for (int i = 0; i < listChamp2.size(); i++) {
            sql += " and " + listChamp2.get(i) + "=? ";
            // system.out.println(i);
        }
        // system.out.println(sql);
        listObj.addAll(listObj2);
        PreparedStatement stmt = con.prepareStatement(sql);
        setObjectInStatement(listObj, stmt);
        System.out.println(stmt);
        int res = stmt.executeUpdate();
        stmt.close();
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }

    public static Object getById(Object o, Connection con) throws Exception {
        Boolean initCO = false;
        con = initCO(initCO, con);
        Class cl = o.getClass();
        Object temp = cl.newInstance();
        Field[] liFi = listAllFields(cl);
        for (int i = 0; i < liFi.length; i++) {
            ColumnName cn = getColumnName(liFi[i]);
            if (cn.pk()) {
                Method get = o.getClass().getMethod("get" + UCFirst(liFi[i].getName()));
                Method set = o.getClass().getMethod("set" + UCFirst(liFi[i].getName()), liFi[i].getType());
                set.invoke(temp, get.invoke(o));
                break;
            }
        }
        if (initCO) {
            con.close();
            System.gc();
        }
        return GenericDAO.get(temp, null).get(0);

    }

    public static ResultSet exeQuery(String Query, Statement stmt) throws Exception {
        ResultSet res = stmt.executeQuery(Query);
        return res;
    }

    public static Field getFieldOfString(Class cl, String str) {
        Field[] lifi = GenericDAO.listAllFields(cl);
        for (Field lifi1 : lifi) {
            if (lifi1.getName().equalsIgnoreCase(str)) {
                return lifi1;
            }
        }
        // system.out.println("Tsisy " + str);
        return null;
    }

    public static Object[] select(String what, String where, Object o, Connection con) throws Exception {
        Class cl = o.getClass();
        Field[] liFi = cl.getDeclaredFields();
        String[] nomChamps = new String[liFi.length];
        for (int i = 0; i < liFi.length; i++) {
            String ess = liFi[i].getName();
            nomChamps[i] = UCFirst(ess);
        }

        String query = "SELECT " + what + " FROM " + where;
        int isacond = 0;
        for (int i = 0; i < nomChamps.length; i++) {
            try {
                Method temp = cl.getMethod("get" + nomChamps[i]);
                if (temp.invoke(o) != null) {
                    isacond++;
                    if (isacond == 1) {
                        query += " where";
                    } else {
                        query += " and";
                    }
                    if (temp.invoke(o) instanceof String) {
                        String st = (String) temp.invoke(o);
                        char[] ch = st.toCharArray();
                        if (ch[0] == '%') {
                            query += " " + liFi[i].getName() + " like '%" + temp.invoke(o) + "%'";
                        }
                    }
                    query += " " + liFi[i].getName() + " = '" + temp.invoke(o) + "'";
                }
            } catch (NoSuchMethodException ex) {
                // system.out.println(ex.getMessage());

            } catch (IllegalAccessException | IllegalArgumentException | SecurityException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // system.out.println(query);
        Statement stmt = con.createStatement();
        ResultSet RS = exeQuery(query, stmt);
        Vector v = new Vector();
        // v.getClass().getSuperclass(
        ResultSetMetaData MD = RS.getMetaData();
        int nbCol = MD.getColumnCount();
        String[] nomcol = new String[nbCol];
        for (int i = 0; i < nbCol; i++) {
            nomcol[i] = MD.getColumnName(i + 1);
        }
        // int i=0;
        Field[] toFill = new Field[nomcol.length];
        // system.out.println("nombre colonne: " + nomcol.length);
        for (int i = 0; i < nomcol.length; i++) {
            toFill[i] = getFieldOfString(cl, nomcol[i]);
        }
        for (int i = 0; i < toFill.length; i++) {
            String ess = toFill[i].getName();
            char[] ar = ess.toCharArray();
            String Firstchar = String.valueOf(ar[0]);
            Firstchar = Firstchar.toUpperCase();
            String nomChamp = Firstchar;
            for (int j = 1; j < ar.length; j++) {
                nomChamp += String.valueOf(ar[j]);
            }
            nomChamps[i] = nomChamp;
        }
        while (RS.next()) {
            Object otemp = cl.newInstance();
            for (int i = 0; i < toFill.length; i++) {
                Method meth = cl.getMethod("set" + nomChamps[i], toFill[i].getType());
                if (liFi[i].getType().getSimpleName().equalsIgnoreCase("Integer")) {
                    meth.invoke(otemp, RS.getInt(i + 1));
                } else if (liFi[i].getType().getSimpleName().equalsIgnoreCase("Double")) {
                    meth.invoke(otemp, RS.getDouble(i + 1));
                } else if (liFi[i].getType().getSimpleName().equalsIgnoreCase("Date")) {
                    meth.invoke(otemp, new Date(RS.getString(i + 1).replace("-", "/")));
                } else if (liFi[i].getType().getSimpleName().equalsIgnoreCase("Boolean")) {
                    meth.invoke(otemp, RS.getBoolean(i + 1));
                } else {
                    meth.invoke(otemp, liFi[i].getType().cast(RS.getString(i + 1)));
                }
            }
            v.add(otemp);
            // Method set=cl.getMethod("set"+nomChamps[i], )
        }
        // //system.out.println(v.size()+"size");
        Object[] tabres = new Object[v.size()];
        for (int i = 0; i < tabres.length; i++) {
            tabres[i] = v.elementAt(i);
        }
        stmt.close();
        RS.close();
        return tabres;
    }

    public static int exec(String query, Connection con) throws DatabaseConfException, SQLException {
        Boolean initCO = false;
        con = initCO(initCO, con);
        Statement stmt = con.createStatement();
        int res = stmt.executeUpdate(query);
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }

    public static ArrayList get(Object o, Connection con) throws Exception {
        Boolean initCO = false;
        con = initCO(initCO, con);
        Class cl = o.getClass();
        ArrayList<String> listChamp = new ArrayList<>();
        ArrayList listObj = new ArrayList();
        String sql = "SELECT * FROM ";
        sql = checkSelectTableName(o, sql);
        sql += " where 1=1 ";
        Field[] liFi = listAllFields(o.getClass());
        updateList(listChamp, listObj, o);
        for (int i = 0; i < listChamp.size(); i++) {
            sql += " and " + listChamp.get(i) + "=? ";
        }
        // system.out.println(sql);
        PreparedStatement stmt = con.prepareStatement(sql);
        setObjectInStatement(listObj, stmt);
        System.out.println(stmt);
        ResultSet RS = stmt.executeQuery();
        ArrayList res = new ArrayList();
        ArrayList<String> champSelect = new ArrayList();
        ArrayList<Field> fiSetable = new ArrayList<>();
        listGetable(champSelect, fiSetable, o);
        try {
            while (RS.next()) {
                Object otemp = cl.getDeclaredConstructor().newInstance();
                for (int i = 0; i < champSelect.size(); i++) {
                    Method set = setter(fiSetable.get(i));
                    ColumnName cn = getColumnName(fiSetable.get(i));
                    if (RS.getObject(champSelect.get(i)) != null) {
                        if (cn.fk()) {// if foreign key
                            // system.out.println("fk");
                            Object tempOb = liFi[i].getType().newInstance();
                            Field[] liFi2 = listAllFields(tempOb.getClass());
                            for (Field liFi21 : liFi2) {
                                ColumnName cn2 = getColumnName(liFi21);
                                if (cn2 != null && cn2.pk()) {
                                    Method set2 = tempOb.getClass().getMethod("set" + UCFirst(liFi21.getName()),
                                            liFi21.getType()); // set id
                                    set2.invoke(tempOb, RS.getString(champSelect.get(i)));
                                    set.invoke(otemp, liFi[i].getType().cast(GenericDAO.getById(tempOb, con)));
                                    break;
                                }
                            }
                        } else if (fiSetable.get(i).getType().getSimpleName().equalsIgnoreCase("Date")) {
                            java.sql.Timestamp temp = RS.getTimestamp(champSelect.get(i));
                            set.invoke(otemp, new Date(temp.getTime()));
                        } else if (fiSetable.get(i).getType().getSimpleName().equalsIgnoreCase("Duration")) {
                            PGInterval pgInterval = (PGInterval) RS.getObject(champSelect.get(i));
                            Duration duration = Duration.ofSeconds((long) pgInterval.getSeconds(),
                                    pgInterval.getMicroSeconds() * 1000);
                            set.invoke(otemp, duration);
                        } else {
                            System.out.println(fiSetable.get(i).getType().getName()+" "+RS.getObject(champSelect.get(i)).getClass().getName());
                            set.invoke(otemp, RS.getObject(champSelect.get(i)));
                        }
                    }
                }
                res.add(otemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }
    public static ArrayList executeQuery(String query,ArrayList listObj,Object o, Connection con) throws SQLException, DatabaseConfException {
        Boolean initCO = false;
        con = initCO(initCO, con);
        Class cl = o.getClass();
        Field[] liFi=GenericDAO.listAllFields(cl);
        PreparedStatement stmt = con.prepareStatement(query);
        setObjectInStatement(listObj, stmt);
        System.out.println(stmt);
        ResultSet RS = stmt.executeQuery();
        ArrayList res = new ArrayList();
        ArrayList<String> champSelect = new ArrayList();
        ArrayList<Field> fiSetable = new ArrayList<>();
        listGetable(champSelect, fiSetable, o);
        try {
            while (RS.next()) {
                Object otemp = cl.getDeclaredConstructor().newInstance();
                for (int i = 0; i < champSelect.size(); i++) {
                    Method set = setter(fiSetable.get(i));
                    ColumnName cn = getColumnName(fiSetable.get(i));
                    if (RS.getObject(champSelect.get(i)) != null) {
                        if (cn.fk()) {// if foreign key
                            // system.out.println("fk");
                            Object tempOb = liFi[i].getType().newInstance();
                            Field[] liFi2 = listAllFields(tempOb.getClass());
                            for (Field liFi21 : liFi2) {
                                ColumnName cn2 = getColumnName(liFi21);
                                if (cn2 != null && cn2.pk()) {
                                    Method set2 = tempOb.getClass().getMethod("set" + UCFirst(liFi21.getName()),
                                            liFi21.getType()); // set id
                                    set2.invoke(tempOb, RS.getString(champSelect.get(i)));
                                    set.invoke(otemp, liFi[i].getType().cast(GenericDAO.getById(tempOb, con)));
                                    break;
                                }
                            }
                        } else if (fiSetable.get(i).getType().getSimpleName().equalsIgnoreCase("Date")) {
                            java.sql.Timestamp temp = RS.getTimestamp(champSelect.get(i));
                            set.invoke(otemp, new Date(temp.getTime()));
                        } else if (fiSetable.get(i).getType().getSimpleName().equalsIgnoreCase("Duration")) {
                            PGInterval pgInterval = (PGInterval) RS.getObject(champSelect.get(i));
                            Duration duration = Duration.ofSeconds((long) pgInterval.getSeconds(),
                                    pgInterval.getMicroSeconds() * 1000);
                            set.invoke(otemp, duration);
                        } else {
                            // system.out.println(fiSetable.get(i).getType().getName()+"
                            // "+RS.getObject(champSelect.get(i)).getClass().getName());
                            set.invoke(otemp, RS.getObject(champSelect.get(i)));
                        }
                    }
                }
                res.add(otemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (initCO) {
            con.close();
            System.gc();
        }
        return res;
    }
}
