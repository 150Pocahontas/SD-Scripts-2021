package Guiao5;

import java.util.*;

class Warehouse {
    private Map<String, Product> m =  new HashMap<String, Product>();

    private class Product { int q = 0; }

    private Product get(String s) {
        Product p = m.get(s);
        if (p != null) return p;
        p = new Product();
        m.put(s, p);
        return p;
    }

    public void supply(String s, int q) {
        Product p = get(s);
        p.q += q;
    }

    // Errado se faltar algum produto...
    public void consume(String[] a) {
        for (String s : a)
            get(s).q--;
    }

}
