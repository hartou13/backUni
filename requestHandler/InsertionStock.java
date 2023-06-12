package requestHandler;

import gdao.inherit.DBModel;
import universalController.classmanaging.fd.FieldDescriptor;

public class InsertionStock extends DBModel<InsertionStock, Integer> {
    @FieldDescriptor(fk = true,fkClass ="model.mikolo.Produit", mainField = "nomProduit")
    Integer produitId;

    @FieldDescriptor()
    Integer nb;

    public Integer getProduitId() {
        return produitId;
    }

    public Integer getNb() {
        return nb;
    }

    
}
