package gdao.login;

import gdao.genericdao.GenericDAO;
import gdao.inherit.DBModel;
import gdao.login.exception.IncorrectPasswordException;
import gdao.login.exception.NonExistantLoginException;
import gdao.login.token.Encrypte;
import gdao.login.token.Token;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hart
 * @param <T>
 * @param <E>
 */
public class Logable<T extends Logable, E extends Object > extends DBModel<T, E> {
    // 
    /**
     * transformer -na ny mdp zay vo antsoina ity
     * @param duration: duree en seconde du token qui sera alloué
     * @return token de l'utilisateur à renvoyer
     * @throws NonExistantLoginException
     * @throws IncorrectPasswordException
     * @throws Exception 
     */
    public Token log(long duration) throws NonExistantLoginException,IncorrectPasswordException, Exception{
        DBModel temp=this.getClass().getDeclaredConstructor().newInstance();
        Field id=getIdentifiant(this.getClass());
        Method get=GenericDAO.getter(id);
        Method set=GenericDAO.setter(id);
        set.invoke(temp, get.invoke(this));
        ArrayList<Logable> list=temp.list();
        if(list.isEmpty())
            throw new NonExistantLoginException("login "+get.invoke(this)+" inexistant");
        DBModel toAuth=list.get(0);
        Field pwd=getPassword(this.getClass());
        get=GenericDAO.getter(pwd);
        System.out.println(get);
        if(!get.invoke(this).equals(get.invoke(toAuth)))
            throw new IncorrectPasswordException("mot de passe incorrect");
        
        Token<E> tokn=new Token<>();
        Duration dt=Duration.ofSeconds(duration);
        Date add=new Date(new Date().getTime()+dt.getSeconds()*1000);
        tokn.setExpDate(add);
        tokn.setToken(Encrypte.SHA1(toAuth.getPkVal()+""+new Date()+""+duration));// a definir
        tokn.setIdOwner((E)toAuth.getPkVal());
        return tokn.generateToken();
    }
    
    public E log() throws NonExistantLoginException,IncorrectPasswordException, Exception{
        DBModel temp=this.getClass().getDeclaredConstructor().newInstance();
        Field id=getIdentifiant(this.getClass());
        System.out.println(id+" field identifiant");
        Method get=GenericDAO.getter(id);
        Method set=GenericDAO.setter(id);
        set.invoke(temp, get.invoke(this));
        ArrayList<Logable> list=temp.list();
        if(list.isEmpty())
            throw new NonExistantLoginException("login "+get.invoke(this)+" inexistant");
        DBModel toAuth=list.get(0);
        Field pwd=getPassword(this.getClass());
        get=GenericDAO.getter(pwd);
        if(!get.invoke(this).equals(get.invoke(toAuth)))
            throw new IncorrectPasswordException("mot de passe incorrect");
        
        return (E) toAuth.getPkVal();
    }
    
    private static Identifiant getIdentifiant(Field fi) {
        return (Identifiant) fi.getAnnotation(Identifiant.class);
    }
    private static Field getIdentifiant(Class cl){
        Field[] fi=GenericDAO.listAllFields(cl);
        for (Field field : fi) {
            if(getIdentifiant(field)!=null)
                return field;
        }
        return null;
    }
    private static Password getPassword(Field fi) {
        return (Password) fi.getAnnotation(Password.class);
    }
    private static Field getPassword(Class cl){
        Field[] fi=GenericDAO.listAllFields(cl);
        for (Field field : fi) {
            if(getPassword(field)!=null)
                return field;
        }
        return null;
    }
}
