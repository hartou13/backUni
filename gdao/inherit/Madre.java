/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdao.inherit;

import gdao.genericdao.ColumnName;
import gdao.genericdao.exception.DatabaseConfException;
import java.sql.SQLException;
/**
 *
 * @author Hart
 */
public abstract class Madre extends DBModel {
    
    Hija[] fille;
    public abstract void setID(Object id);
    public abstract Object getID();
    public Hija[] getFille() {
        return fille;
    }

    public void setFille(Hija[] fille) {
        this.fille = fille;
    }
    public abstract void addFille();
    public void addFille(Hija newFille){
        if(newFille!=null){
             newFille.setMere(this);
            if(fille==null){
                fille=new Hija[1];
                fille[0]=newFille;
            }
            else{
                Hija[] newtab=new Hija[fille.length+1];
                System.arraycopy(fille, 0, newtab, 0, fille.length);
                newtab[newtab.length-1]=newFille;
                this.fille=newtab;
            }
        }
    }

    @Override
    public int save() throws Exception{
        try {
            super.save();
            Madre temp=(Madre) this.list().get(0);
            this.setID(temp.getID());
            for (Hija fille1 : fille) {
                fille1.setIdMadre((String)temp.getID());
                fille1.save();
            }
            return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    
}
