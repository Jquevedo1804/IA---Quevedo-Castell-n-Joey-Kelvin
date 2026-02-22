package Puzzle;

import java.util.*;

public class App {
    public static void main(String[] args) {
        String inicio = "123804765";
        String meta   = "128437605";
        List<TBComparativa> resultados = new ArrayList<>();

        System.out.println("--- Primero en Anchura ---");
        new Arbol(new Nodo(inicio), meta).busquedaAnchura(resultados);

        System.out.println("\n--- Primero en Profundidad ---");
        new Arbol(new Nodo(inicio), meta).busquedaProfundidad(resultados);

        System.out.println("\n--- Costo Uniforme ---");
        new Arbol(new Nodo(inicio), meta).costoUniforme(resultados);

        System.out.println("Inicio: " + inicio);
        System.out.println("Meta: " + meta);
        TBComparativa.imprimirTabla(resultados);
    }
}
