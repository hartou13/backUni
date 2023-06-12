package qc.search;

import java.util.Arrays;

public class SearchParameters {
    KV[] kv;
    KINumber[] kin;
    KIDate[] kiDates;
    KChoice[] kChoice;
    Integer page, nb;
    
   

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNb() {
        return nb;
    }

    public void setNb(Integer nb) {
        this.nb = nb;
    }

    public KChoice[] getkChoice() {
        return kChoice;
    }

    public void setkChoice(KChoice[] kChoice) {
        this.kChoice = kChoice;
    }
    public KV[] getKv() {
        return kv;
    }
    public void setKv(KV[] kv) {
        this.kv = kv;
    }
    public KINumber[] getKin() {
        return kin;
    }
    public void setKin(KINumber[] kin) {
        this.kin = kin;
    }
    public KIDate[] getKiDates() {
        return kiDates;
    }
    public void setKiDates(KIDate[] kiDates) {
        this.kiDates = kiDates;
    }

    @Override
    public String toString() {
        return "SearchParameters [kv=" + Arrays.toString(kv) + ", kin=" + Arrays.toString(kin) + ", kiDates="
                + Arrays.toString(kiDates) + ", kChoice=" + Arrays.toString(kChoice) + ", page=" + page + ", nb=" + nb
                + "]";
    }
    
}
