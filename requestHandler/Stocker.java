package requestHandler;

import model.Stock;
import model.Unite;

public class Stocker {
    Integer produit, nombre, unite;
    public int save() throws Exception{
        Unite un=new Unite();
        un.setId(unite);
        un=un.list().get(0);
        Stock stock=new Stock();
        stock.setEntree(this.getNombre()*un.getValeur());
        stock.setProduitId(produit);
        return stock.save();
    }
    public Integer getProduit() {
        return produit;
    }

    public void setProduit(Integer produit) {
        this.produit = produit;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }
    
}
