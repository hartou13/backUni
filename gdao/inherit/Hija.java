/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdao.inherit;

/**
 *
 * @author Hart
 *  prononcé "Hira"
 */
public abstract class Hija extends DBModel {
    Madre mere;

    public Madre getMere() {
        return mere;
    }

    public void setMere(Madre mere) {
        this.mere = mere;
    }
    public abstract void setIdMadre(String id);
    
}
