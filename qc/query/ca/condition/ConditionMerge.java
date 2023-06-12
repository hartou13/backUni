package qc.query.ca.condition;

import java.util.ArrayList;
import qc.query.Queriable;

/**
 *
 * @author Hart
 */
public class ConditionMerge extends Queriable{
    ConditionMerge c1, c2;
    Condition mainCondition;
    MergeType type;
    
    public ConditionMerge(Condition condition) {
        this.mainCondition=condition;
    }
    public ConditionMerge(ConditionMerge c1) {
        this.c1 = c1;
    }
    
    public ConditionMerge add(Condition c, MergeType type){
        return new ConditionMerge(this, new ConditionMerge(c), type);
    }
    
    public ConditionMerge add(ConditionMerge c, MergeType type){
        if(this.c1==null)
            return new ConditionMerge(this, c, type);
        else if (this.c2==null){
              this.setC2(c);
              this.setType(type);
              return this;
        }
        return new ConditionMerge(this, c, type);
    }

    public ConditionMerge(ConditionMerge c1, ConditionMerge c2, MergeType type) {
        this.c1 = c1;
        this.c2 = c2;
        this.type=type;
    }
    public ConditionMerge(Condition c1, Condition c2, MergeType type) {
        this.c1 = new ConditionMerge(c1);
        this.c2 =new ConditionMerge(c2);
        this.type=type;
    }

    public Condition getMainCondition() {
        return mainCondition;
    }

    public void setMainCondition(Condition mainCondition) {
        this.mainCondition = mainCondition;
    }

    public MergeType getType() {
        return type;
    }

    public void setType(MergeType type) {
        this.type = type;
    }
    

    public ConditionMerge getC1() {
        return c1;
    }

    public void setC1(ConditionMerge c1) {
        this.c1 = c1;
    }

    public ConditionMerge getC2() {
        return c2;
    }

    public void setC2(ConditionMerge c2) {
        this.c2 = c2;
    }
    public String getConditionQuery(){
        return "";
    }

    @Override
    public String getQuery() throws Exception {
        String res="";
         res+="(";
        if(c1==null){
            System.out.println("no query 1");
            if(this.mainCondition==null)
                System.out.println("no main condition");
            else{
                res+=this.mainCondition.getQuery();
            }
        }
        else
            res+=c1.getQuery();
        if(c2==null){
            System.out.println("no query 2");
        }
        else
            if(this.type==null)
                System.out.println("no type");
            else{
                if(this.type==MergeType.AND)
                    res+=" and ";
                else if(this.type==MergeType.OR)
                    res+=" or ";
                res+=c2.getQuery();
            }   
       
        
        
        res+=")";
        
        return res;
    }

    @Override
    public String getPrepdQuery() throws Exception {
        String res="";
         res+="(";
        if(c1==null){
            System.out.println("no query 1");
            if(this.mainCondition==null)
                System.out.println("no main condition");
            else{
                res+=this.mainCondition.getPrepdQuery();
            }
        }
        else
            res+=c1.getPrepdQuery();
        if(c2==null){
            System.out.println("no query 2");
        }
        else
            if(this.type==null)
                System.out.println("no type");
            else{
                if(this.type==MergeType.AND)
                    res+=" and ";
                else if(this.type==MergeType.OR)
                    res+=" or ";
                res+=c2.getPrepdQuery();
            }   
       
        
        
        res+=")";
        
        return res;
    }

    @Override
    public ArrayList<Object> getPrepdQueryObject() throws Exception {
        ArrayList<Object> res=new ArrayList<>();
        if(c1==null){
            if(this.mainCondition!=null)
                res.addAll(this.mainCondition.getPrepdQueryObject());
        }
        else{
            res.addAll(this.c1.getPrepdQueryObject());
        }
        if(c2!=null)
            res.addAll(c2.getPrepdQueryObject());
        return res;
    }
    
}
