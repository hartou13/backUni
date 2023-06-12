package requestHandler;

import gdao.inherit.DBModel;

public class Updatinator<T extends DBModel> {
    T old, brand;

    public T getOld() {
        return old;
    }

    public void setOld(T old) {
        this.old = old;
    }

    public T getBrand() {
        return brand;
    }

    public void setBrand(T brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Updatinator [old=" + old + ", brand=" + brand + "]";
    }
    
    
}
