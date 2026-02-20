package Diagnostico;

public class Arbol {
    Nodo raiz;

    boolean vacio() {
        return raiz == null;
    }

    Nodo buscarNodo(String nombre) {
        Nodo a = raiz;
        while (a != null) {
            int c = nombre.compareTo(a.nombre);
            if (c == 0) return a;
            a = (c < 0) ? a.izq : a.der;
        }
        return null;
    }

    void insertar(String nombre) {
        raiz = insertarRec(raiz, nombre);
    }

    private Nodo insertarRec(Nodo a, String nombre) {
        if (a == null) return new Nodo(nombre);
        int c = nombre.compareTo(a.nombre);
        if (c < 0) a.izq = insertarRec(a.izq, nombre);
        else if (c > 0) a.der = insertarRec(a.der, nombre);
        return a;
    }
}