package qc.search;

public class KINumber {
    String key;
    Double min, max;

    public KINumber() {
    }

    public KINumber(String key, Double min, Double max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Double getMin() {
        return min;
    }
    public void setMin(Double min) {
        this.min = min;
    }
    public Double getMax() {
        return max;
    }
    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "KINumber [key=" + key + ", min=" + min + ", max=" + max + "]";
    }
    
}
