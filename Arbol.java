package Puzzle;

import java.util.*;

public class Arbol {
    private Nodo raiz;
    private String objetivo;

    public Arbol(Nodo raiz, String objetivo) {
        this.raiz = raiz;
        this.objetivo = objetivo;
    }

    public void busquedaAnchura(List<TBComparativa> resultados) {
        long inicioTiempo = System.nanoTime();
        Queue<Nodo> cola = new LinkedList<Nodo>();
        HashSet<String> visitados = new HashSet<String>();
        cola.add(raiz);
        visitados.add(raiz.getEstado());
        Nodo actual = null;
        boolean encontrado = false;
        int iteraciones = 0;

        while (!encontrado && cola.size() > 0) {
            actual = cola.poll();
            iteraciones++;

            if (actual.getEstado().equals(objetivo)) {
                encontrado = true;
            } else {
                List<String> sucesores = actual.obtenerSucesores();

                for (String sucesor : sucesores) {
                    if (visitados.contains(sucesor)) continue;
                    Nodo hijo = new Nodo(sucesor);
                    hijo.setPadre(actual);
                    hijo.setCostoCamino(actual.getCostoCamino() + 1);
                    cola.add(hijo);
                    visitados.add(sucesor);
                }
            }
        }

        long tiempoEjecucion = (System.nanoTime() - inicioTiempo) / 1_000_000;

        if (encontrado) {
            Nodo.imprimirSolucion(actual, visitados, iteraciones);
            resultados.add(new TBComparativa("BusquedaAnchura", Nodo.getMovimientos(actual), visitados.size(), tiempoEjecucion, true));
            return;
        }
        resultados.add(new TBComparativa("BusquedaAnchura", 0, visitados.size(), tiempoEjecucion, false));
    }

    public void busquedaProfundidad(List<TBComparativa> resultados) {
        long inicioTiempo = System.nanoTime();
        Stack<Nodo> pila = new Stack<Nodo>();
        HashSet<String> visitados = new HashSet<String>();
        pila.push(raiz);
        visitados.add(raiz.getEstado());
        Nodo actual = null;
        boolean encontrado = false;
        int iteraciones = 0;

        while (!encontrado && !pila.isEmpty()) {
            actual = pila.pop();
            iteraciones++;

            if (actual.getEstado().equals(objetivo)) {
                encontrado = true;
            } else {
                List<String> sucesores = actual.obtenerSucesores();

                for (String sucesor : sucesores) {
                    if (visitados.contains(sucesor)) continue;
                    Nodo hijo = new Nodo(sucesor);
                    hijo.setPadre(actual);
                    hijo.setCostoCamino(actual.getCostoCamino() + 1);
                    pila.push(hijo);
                    visitados.add(sucesor);
                }
            }
        }

        long tiempoEjecucion = (System.nanoTime() - inicioTiempo) / 1_000_000;

        if (encontrado) {
            Nodo.imprimirSolucion(actual, visitados, iteraciones);
            resultados.add(new TBComparativa("BusquedaProfundidad", Nodo.getMovimientos(actual), visitados.size(), tiempoEjecucion, true));
            return;
        }
        resultados.add(new TBComparativa("BusquedaProfundidad", 0, visitados.size(), tiempoEjecucion, false));
    }

    public void costoUniforme(List<TBComparativa> resultados) {
        long inicioTiempo = System.nanoTime();
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<Nodo>(Comparator.comparingInt(Nodo::getCostoCamino));
        HashSet<String> visitados = new HashSet<String>();
        raiz.setCostoCamino(0);
        colaPrioridad.add(raiz);
        Nodo actual = null;
        boolean encontrado = false;
        int iteraciones = 0;

        while (!encontrado && !colaPrioridad.isEmpty()) {
            actual = colaPrioridad.poll();
            iteraciones++;

            if (visitados.contains(actual.getEstado())) continue;
            visitados.add(actual.getEstado());

            if (actual.getEstado().equals(objetivo)) {
                encontrado = true;
            } else {
                List<String> sucesores = actual.obtenerSucesores();

                for (String sucesor : sucesores) {
                    if (visitados.contains(sucesor)) continue;
                    Nodo hijo = new Nodo(sucesor);
                    hijo.setPadre(actual);
                    hijo.setCostoCamino(actual.getCostoCamino() + 1);
                    colaPrioridad.add(hijo);
                }
            }
        }

        long tiempoEjecucion = (System.nanoTime() - inicioTiempo) / 1_000_000;

        if (encontrado) {
            Nodo.imprimirSolucion(actual, visitados, iteraciones);
            resultados.add(new TBComparativa("Costo Uniforme", Nodo.getMovimientos(actual), visitados.size(), tiempoEjecucion, true));
            return;
        }
        resultados.add(new TBComparativa("Costo Uniforme", 0, visitados.size(), tiempoEjecucion, false));
    }
}