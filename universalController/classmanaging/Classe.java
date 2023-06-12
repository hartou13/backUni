package universalController.classmanaging;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Classe {
    String className;
    ArrayList<Champ> listeChamp;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public ArrayList<Champ> getListeChamp() {
        return listeChamp;
    }
    public void setListeChamp(ArrayList<Champ> listeChamp) {
        this.listeChamp = listeChamp;
    }
    public static Classe translate(Class cl) throws Exception{
        Classe classe = new Classe();
        classe.setClassName(cl.getName());
        Field[] liFi=Util.listAllFields(cl);
        classe.setListeChamp(new ArrayList<>());
        for (int i = 0; i < liFi.length; i++) {
            classe.getListeChamp().add(Champ.translate(liFi[i]));
        }
        return classe;
    }
    @Override
    public String toString() {
        return "Classe [className=" + className + ", listeChamp=" + listeChamp + "]";
    }
    
    
}
