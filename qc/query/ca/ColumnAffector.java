package qc.query.ca;

import java.util.ArrayList;

/**
 *
 * @author Hart
 */
public class ColumnAffector {
    String column;
    ArrayList<Object> elements;

    public ArrayList<Object> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Object> elements) {
        this.elements = elements;
    }
    
    public void addElements(Object elem){
        if(this.elements==null){
            elements=new ArrayList<>();
        }
        elements.add(elem);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    
}
