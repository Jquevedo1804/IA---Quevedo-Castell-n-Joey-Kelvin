package 8puzzle;

import java.util.*;

public class Nodo {
    private String estado;
    private Nodo padre;
    private int costoCamino;

    public Nodo(String estado) {
        this.estado = estado;
        this.costoCamino = 0;
    }

    public String getEstado() { 
        return estado; 
    }
    public Nodo getPadre() { 
        return padre; 
    }
    public void setPadre(Nodo padre) { 
        this.padre = padre; 
    }
    public int getCostoCamino() { 
        return costoCamino; 
    }
    public void setCostoCamino(int costoCamino) { 
        this.costoCamino = costoCamino; 
    }

    public List<String> obtenerSucesores() {
        List<String> sucesores = new ArrayList<>();
        int hueco = estado.indexOf("0");
        
        switch (hueco) {
            case 0:
                agregar(sucesores, 0, 1); 
                agregar(sucesores, 0, 3); 
                break;
            case 1:
                agregar(sucesores, 1, 0); 
                agregar(sucesores, 1, 2); 
                agregar(sucesores, 1, 4); 
                break;
            case 2:
                agregar(sucesores, 2, 1); 
                agregar(sucesores, 2, 5);
                break;
            case 3:
                agregar(sucesores, 3, 0); 
                agregar(sucesores, 3, 4); 
                agregar(sucesores, 3, 6); 
                break;
            case 4:
                agregar(sucesores, 4, 1); 
                agregar(sucesores, 4, 3); 
                agregar(sucesores, 4, 5);
                agregar(sucesores, 4, 7);
                break;
            case 5:
                agregar(sucesores, 5, 2); 
                agregar(sucesores, 5, 4); 
                agregar(sucesores, 5, 8); 
                break;
            case 6:
                agregar(sucesores, 6, 3); 
                agregar(sucesores, 6, 7); 
                break;
            case 7: 
                agregar(sucesores, 7, 4); 
                agregar(sucesores, 7, 6); 
                agregar(sucesores, 7, 8); 
                break;
            case 8: 
                agregar(sucesores, 8, 5); 
                agregar(sucesores, 8, 7); 
                break;
        }
        return sucesores;
    }

    private void agregar(List<String> lista, int i, int j) {
        char[] c = estado.toCharArray();
        char temp = c[i];
        c[i] = c[j];
        c[j] = temp;
        lista.add(new String(c));
    }

    public static void imprimirSolucion(Nodo meta, Set<String> visitados, int iteraciones) {
        List<Nodo> camino = new ArrayList<>();
        Nodo actual = meta;
        while (actual != null) {
            camino.add(actual);
            actual = actual.padre;
        }
        Collections.reverse(camino);
        for (int i = 0; i < camino.size(); i++) {
            String s = camino.get(i).estado;
            System.out.println("Paso " + i + ":");
            System.out.println(s.substring(0, 3) + "\n" + s.substring(3, 6) + "\n" + s.substring(6, 9) + "\n");
        }
    }
    
    public static int getMovimientos(Nodo meta) {
        int count = 0;
        Nodo actual = meta;
        while (actual != null && actual.padre != null) {
            count++;
            actual = actual.padre;
        }
        return count;
    }
}