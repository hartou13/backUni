package qc.query;

import java.util.ArrayList;
import qc.exception.NoQueryTypeException;

/**
 *
 * @author Hart
 */
public abstract class Queriable {
    public abstract String getQuery() throws Exception;
    public abstract String getPrepdQuery() throws Exception;
    public abstract ArrayList<Object> getPrepdQueryObject() throws Exception;
}
