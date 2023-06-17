package qc.query.ca.condition;

import java.util.ArrayList;
import qc.exception.ColumnNotPrecisedException;
import qc.exception.NoGivenQueryReferenceException;
import qc.exception.NoQueryTypeException;
import qc.query.Queriable;
import qc.query.ca.ColumnAffector;

/**
 *
 * @author Hart
 */
public class Condition extends Queriable{
    ConditionType conditionType;
    String column;
    Object element;

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }
    

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    
    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    
    

    public static Condition equals(String column, Object obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.EQUALS);
        cond.setElement(obj);
        return cond;
    }
    public static Condition like(String column, String obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.LIKE);
        cond.setElement("%"+obj+"%");
        return cond;
    }
    public static Condition inf(String column, Object obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.INF);
        cond.setElement(obj);
        return cond;
    }
    public static Condition sup(String column, Object obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.SUP);
        cond.setElement(obj);
        return cond;
    }
    public static Condition infEq(String column, Object obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.INFeQ);
        cond.setElement(obj);
        return cond;
    }
    public static Condition supEq(String column, Object obj){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.SUPeQ);
        cond.setElement(obj);
        return cond;
    }
    public static Condition between(String column, Object min, Object max){
        Condition cond=new Condition();
        cond.setColumn(column);
        cond.setConditionType(ConditionType.BETWEEN);
        ArrayList<Object> lido=new ArrayList<>();
        lido.add(min);
        lido.add(max);
        cond.setElement(lido);
        return cond;
    }

    @Override
    public String toString() {
        return "Condition{" + "conditionType=" + conditionType + ", column=" + column + ", element=" + element + '}';
    }
    public String getCondStart() throws Exception{
         String res="(";
        if(this.column==null)
            throw new ColumnNotPrecisedException("Column for condition "+this.toString()+" not given");
        else
            res+=this.getColumn()+" ";
        
        if(null==this.conditionType)
            res+=" = ";
        else switch (this.conditionType) {
            case EQUALS:
                res+=" = ";
                break;
            case INF:
                res+=" < ";
                break;
            case INFeQ:
                res+=" <= ";
                break;
            case SUP:
                res+=" > ";
                break;
            case SUPeQ:
                res+=" >= ";
                break;
            case LIKE:
                res+=" like ";
                break;
            case BETWEEN:
                res+=" between ";
                break;
            default:
                break;
        }
        return res;
        
    }
    
    @Override
    public String getQuery() throws Exception {
        String res=this.getCondStart();
        if(this.element==null)
            throw new NoGivenQueryReferenceException("element for column "+this.getColumn()+" not given");
        else if(this.conditionType==ConditionType.BETWEEN){
            ArrayList list=(ArrayList) this.getElement();
            res+=list.get(0)+" and "+list.get(1);
        }    
        else
            res+=" '"+this.getElement()+"' ";
        
        res+=")";
        return res;
    }

    @Override
    public String getPrepdQuery() throws Exception {
          String res=this.getCondStart();
        if(this.element==null)
            throw new NoGivenQueryReferenceException("element for column "+this.getColumn()+" not given");
        else if(this.conditionType==ConditionType.BETWEEN){
            res+="? and ?";
        }    
        else
            res+=" ? ";
        
        res+=")";
        return res;
    }

    @Override
    public ArrayList<Object> getPrepdQueryObject() throws Exception {
        ArrayList<Object> res=new ArrayList<>();
        if(this.getElement() instanceof ArrayList)
            res.addAll((ArrayList)this.getElement());
        else
            res.add(this.getElement());
        return res;
    }
    
}
