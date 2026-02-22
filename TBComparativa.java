package Puzzle;

import java.util.*;

public class TBComparativa {
    private String algoritmo;
    private int movimientos;
    private int nodos;
    private long tiempoEjecucion;
    private boolean encontrado;

    public TBComparativa(String algoritmo, int movimientos, int nodos, long tiempoEjecucion, boolean encontrado) {
        this.algoritmo = algoritmo;
        this.movimientos = movimientos;
        this.nodos = nodos;
        this.tiempoEjecucion = tiempoEjecucion;
        this.encontrado = encontrado;
    }

    public String getAlgoritmo() { 
        return algoritmo; 
    }
    public int getMovimientos() { 
        return movimientos; 
    }
    public int getNodos() { 
        return nodos; 
    }
    public long getTiempoEjecucion() { 
        return tiempoEjecucion; 
    }
    public boolean isEncontrado() { 
        return encontrado; 
    }

    public static void imprimirTabla(List<TBComparativa> resultados) {
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-12s | %-15s | %-11s |\n", "Algoritmo", "Movimientos", "Tiempo ejecucion", "Nodos");
        System.out.println("------------------------------------------------------------------------");
        
        for (TBComparativa r : resultados) {
            String mov = r.isEncontrado() ? String.valueOf(r.getMovimientos()) : "Error";
            System.out.printf("| %-20s | %-12s | %-16d | %-11d |\n", 
                r.getAlgoritmo(), mov, r.getTiempoEjecucion(), r.getNodos());
        }
        System.out.println("------------------------------------------------------------------------");
    }
}
