package Diagnostico;

public class Main {
    public static void main(String[] args) {
        Arbol arbol = new Arbol();

        arbol.insertar("Joey");
        arbol.insertar("Hiram");
        arbol.insertar("Abel");

        System.out.println(arbol.vacio());
        System.out.println(arbol.buscarNodo("Hiram") != null);
        System.out.println(arbol.buscarNodo("Kelvin") != null);

    }
}