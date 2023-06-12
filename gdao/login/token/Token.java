package gdao.login.token;

import gdao.genericdao.ColumnName;
import gdao.inherit.DBModel;
import gdao.login.Identifiant;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hart
 * @param <T>
 */
public class Token<T extends Object> extends DBModel<Token<T>, String> {
    @ColumnName
    Integer idToken;
    @ColumnName()
    String  token;
    @ColumnName
    Date expDate;
    @ColumnName()
    T idOwner;
    @ColumnName()
    Integer authLevel;
    
    public static boolean isTokenAvailable(String tokenIdentifier) throws Exception{
        Token tkn=new Token();
        tkn.setToken(tokenIdentifier);
        ArrayList<Token> listToken=tkn.list();
        if(listToken.isEmpty()){
            System.out.println("token inexistant");
            return false;
        }
        if(listToken.get(0).getExpDate().before(new Date())){
            System.out.println(listToken.get(0).getExpDate()+" "+new Date());
            System.out.println("token expire");
            return false;
        }
        
        return true;
    }
    public static boolean isTokenAvailable(String tokenIdentifier, Integer authRequired) throws Exception{
        Token tkn=new Token();
        tkn.setToken(tokenIdentifier);
        ArrayList<Token> listToken=tkn.list();
        if(listToken.isEmpty()){
            System.out.println("token inexistant");
            return false;
        }
        if(listToken.get(0).getExpDate().before(new Date())){
            System.out.println("token expire");
            return false;
        }
        if(listToken.get(0).getAuthLevel()<authRequired){
            System.out.println("user not allowed");
            return false;
        }
        
        return true;
    }
    
    
    
    public Token generateToken() throws Exception{
        this.save();
        this.setIdToken(this.list().get(0).getIdToken());
        return this;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }
    
    
  

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(T idOwner) {
        this.idOwner = idOwner;
    }

   

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
    public Integer getIdToken() {
        return idToken;
    }
    public void setIdToken(Integer idToken) {
        this.idToken = idToken;
    }
    
}
