/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qc;

import qc.exception.NoQueryTypeException;
import qc.query.Query;
import qc.query.QueryType;
import qc.query.ca.condition.Condition;
import qc.query.ca.condition.ConditionMerge;
import qc.query.ca.condition.MergeType;
import qc.search.KChoice;
import qc.search.KIDate;
import qc.search.KINumber;
import qc.search.KV;
import qc.search.SearchParameters;

/**
 *
 * @author Hart
 */
public class QC {

    /**
     * @param args the command line arguments
     * @throws qc.exception.NoQueryTypeException
     */
    public static void main(String[] args) throws NoQueryTypeException, Exception {
        // TODO code application logic here
        // Query q=new Query();
        // q.setQueryType(QueryType.SELECT);
        // q.setTableName("testTable");
        // Condition cond=Condition.like("test", "test");
        // Condition cond2=Condition.equals("test2", "test2");
        // Condition cond3=Condition.like("test3", "test3");
        // q.setCondition(new ConditionMerge(new ConditionMerge(cond), new
        // ConditionMerge(cond2,cond3, MergeType.OR), MergeType.AND));
        // System.out.println(q.getQuery());
        // System.out.println(q.getPrepdQuery());
        // System.out.println(q.getPrepdQueryObject());
        SearchParameters p = new SearchParameters();
        KV[] kv = { new KV("test", "test"), new KV("test2", "test2"), new KV("tes3", "test3") };
        p.setKv(kv);
        KINumber[] kin = { new KINumber("testnombre", 0d, 2d) };
        p.setKin(kin);
        Integer[] test = { 0, 1, 2 };
        KChoice[] kc = { new KChoice("cat", test), new KChoice("cat2", test) };
        p.setkChoice(kc);
        System.out.println(QC.buildMultiQuery(p, "tableTest"));
    }

    public static Query buildMultiQuery(SearchParameters sp, String tableName) throws Exception {
        String res = "";
        Query q = new Query();
        q.setQueryType(QueryType.SELECT);
        q.setTableName(tableName);
        KV[] kv = sp.getKv();
        ConditionMerge cm = new ConditionMerge(Condition.equals("1", 1));
        if (kv != null && kv.length > 0) {
            cm = new ConditionMerge(Condition.like(kv[0].getKey(), kv[0].getValue()));
            for (int i = 1; i < kv.length; i++) {
                KV kv1 = kv[i];
                cm = cm.add(Condition.like(kv1.getKey(), kv1.getValue()), MergeType.AND);
            }
        }
        ConditionMerge cm2 = new ConditionMerge(Condition.equals("1", 1));
        KINumber[] kin = sp.getKin();
        if (kin != null && kin.length > 0) {
            cm2 = new ConditionMerge(Condition.between(kin[0].getKey(), kin[0].getMin(), kin[0].getMax()));
            for (int i = 1; i < kin.length; i++) {
                KINumber kv1 = kin[i];
                if (kv1.getMin() == null)
                    cm2 = cm2.add(Condition.infEq(kv1.getKey(), kv1.getMax()), MergeType.AND);
                else if (kv1.getMax() == null)
                    cm2 = cm2.add(Condition.supEq(kv1.getKey(), kv1.getMin()), MergeType.AND);
                else
                    cm2 = cm2.add(Condition.between(kv1.getKey(), kv1.getMin(), kv1.getMax()), MergeType.AND);
            }
        }
        KIDate[] kid = sp.getKiDates();
        if (kid != null && kid.length > 0) {
            for (KIDate kv1 : kid) {
                if (kv1.getMin() == null)
                    cm2 = cm2.add(Condition.infEq(kv1.getKey(), kv1.getMax()), MergeType.AND);
                else if (kv1.getMax() == null)
                    cm2 = cm2.add(Condition.supEq(kv1.getKey(), kv1.getMin()), MergeType.AND);
                else
                    cm2 = cm2.add(Condition.between(kv1.getKey(), kv1.getMin(), kv1.getMax()), MergeType.AND);
            }
        }
        KChoice[] kc = sp.getkChoice();
        if (kc != null && kc.length > 0) {
            // System.out.println("misy kc");
            for (int i = 0; i < kc.length; i++) {
                KChoice kv1 = kc[i];
                Object[] tab = kv1.getTab();
                try {
                    int val = Integer.parseInt(tab[0].toString());
                    ConditionMerge or = new ConditionMerge(Condition.equals(kv1.getKey(), val));
                    for (int j = 1; j < tab.length; j++) {
                        val = Integer.parseInt(tab[j].toString());
                        or = or.add(Condition.equals(kv1.getKey(), val), MergeType.OR);
                    }
                    cm2 = cm2.add(or, MergeType.AND);
                } catch (Exception e) {
                    // int val=Integer.parseInt(tab[0].toString());
                    ConditionMerge or = new ConditionMerge(Condition.equals(kv1.getKey(), tab[0]));
                    for (int j = 1; j < tab.length; j++) {
                        Object object = tab[j];
                        or = or.add(Condition.equals(kv1.getKey(), object), MergeType.OR);
                    }
                    cm2 = cm2.add(or, MergeType.AND);
                }

            }
        }

        System.out.println(cm2);
        cm = cm.add(cm2, MergeType.AND);
        q.setCondition(cm);
        if (sp.getNb() != null) {
            q.setLimit(sp.getNb());
            q.setOffset((sp.getPage() - 1) * sp.getNb());
        }
        res = q.getQuery();
        return q;
    }

}
