package qc.query;

import java.util.ArrayList;
import qc.exception.NoQueryTypeException;
import qc.query.ca.condition.Condition;
import qc.query.ca.condition.ConditionMerge;

/**
 *
 * @author Hart
 */

public class Query extends Queriable{

   
    QueryType queryType;
    ConditionMerge condition;
    String tableName;
    Integer limit, offset;

    

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getTableName() {
        return tableName;
    }

    public ConditionMerge getCondition() {
        return condition;
    }

    public void setCondition(ConditionMerge condition) {
        this.condition = condition;
    }
   public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getQueryStart() throws NoQueryTypeException{
        String query="";
        if(null==queryType){
            throw new NoQueryTypeException("Query Type not given");
        }
        else switch (queryType) {
            case INSERT:
                query+="INSERT INTO ";
                break;
            case DELETE:
                query+="DELETE FROM";
                break;
            case UPDATE:
                query+="UPDATE ";
                break;
            case SELECT:
                query+="SELECT * FROM ";
                break;
            default:
                break;
        }
        query+=tableName;
        return query;
        
    }
    
    public String checkPaginate(String query){
        if(this.queryType==QueryType.SELECT){
            if(limit!=null){
                query+=" LIMIT "+limit +" OFFSET "+offset;
            }
        }


        return query;
    }

    @Override
    public String getQuery() throws Exception{
        String query=this.getQueryStart();
        if(this.condition !=null && this.queryType!=QueryType.INSERT){
            query+=" WHERE ";
            query+=condition.getQuery();
        }
        query=this.checkPaginate(query);
        return query;
    }
     @Override
    public String getPrepdQuery() throws Exception {
        String query=this.getQueryStart();
        if(this.condition !=null && this.queryType!=QueryType.INSERT){
            query+=" WHERE ";
            query+=condition.getPrepdQuery();
        }
        query=this.checkPaginate(query);
        return query;
    }

    @Override
    public ArrayList<Object> getPrepdQueryObject() throws Exception {
        return condition.getPrepdQueryObject();
    }
    
    
}
