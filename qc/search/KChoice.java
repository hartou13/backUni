package qc.search;

/**
 *
 * @author Hart
 */
public class KChoice {
    String key;
    Object[] tab;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object[] getTab() {
        return tab;
    }

    public void setTab(Object[] tab) {
        this.tab = tab;
    }

    public KChoice() {
    }

    public KChoice(String key, Object[] tab) {
        this.key = key;
        this.tab = tab;
    }
    
    
}
