package puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
            24 puzzle (5x5) con IDA*

    Materia: INTELIGENCIA ARTIFICIAL
    Docente: JOSE MARIO RIOS FELIX
    Alumno: Quevedo Castellon Joey Kelvin
*/
public class App {
    private static final int MEZCLA_MIN = 30;
    private static final int MEZCLA_MAX = 50;
    private static final Random RANDOM = new Random();
    /*
        IDA* (Iterative Deepening A* - Búsqueda en Profundidad Iterativa A*)

        Formula de evaluacion: f(n) = g(n) + h(n)
        
        g(n): Es el costo real acumulado, o sea cuántos movimientos llevo desde el inicio.
        h(n): Es el costo estimado, o sea una aproximación de cuánto falta para llegar al estado objetivo.
    */
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("-----------------------------------------------");
                System.out.println("24 puzzle (5x5) con IDA*");
                System.out.println("\nElavorado por: Quevedo Castellon Joey Kelvin");
                System.out.println("-----------------------------------------------");
                System.out.println("1) Estado manual");
                System.out.println("2) Estado aleatorio resoluble");
                System.out.println("0) Salir");
                System.out.println("-----------------------------------------------");
                int op = leerEntero(sc, "Opcion: ");

                if (op == 0) {
                    return;
                }

                byte[] inicial;

                if (op == 1) {
                    inicial = leerEstadoManual(sc);
                    System.out.println("\nEstado objetivo:");
                    Nodo.imprimir(Nodo.ESTADO_OBJETIVO);
                    System.out.println("\nEstado inicial:");
                    Nodo.imprimir(inicial);
                } else if (op == 2) {
                    int movimientos = MEZCLA_MIN + RANDOM.nextInt(MEZCLA_MAX - MEZCLA_MIN + 1);
                    inicial = Nodo.generarAleatorioDesdeObjetivo(movimientos, RANDOM);
                    System.out.println("\n------------------------------------------------------------------------------------------");
                    System.out.println("Movimientos de mezcla: " + movimientos);
                    System.out.println("Estado inicial: " + Nodo.comoLista(inicial));
                    System.out.println("Estado objetivo: " + Nodo.comoLista(Nodo.ESTADO_OBJETIVO));
                    System.out.println("------------------------------------------------------------------------------------------");
                } else {
                    System.out.println("Opcion invalida.");
                    continue;
                }

                if (Arrays.equals(inicial, Nodo.ESTADO_OBJETIVO)) {
                    System.out.println("Ya esta resuelto.");
                    continue;
                }

                ejecutarMenuHeuristica(sc, inicial);
            }
        }
    }

    private static void ejecutarMenuHeuristica(Scanner sc, byte[] inicial) {
        System.out.println("1) IDA* Manhattan");
        System.out.println("2) IDA* Manhattan + Conflicto Lineal");
        System.out.println("3) Comparar ambas");
        System.out.println("------------------------------------------------------------------------------------------");

        int op = leerEntero(sc, "Opcion: ");

        if (op == 1) {
            ejecutarUna(inicial, Nodo.TipoHeuristica.MANHATTAN);
            return;
        }
        if (op == 2) {
            ejecutarUna(inicial, Nodo.TipoHeuristica.MANHATTAN_CONFLICTO_LINEAL);
            return;
        }
        if (op == 3) {
            Arbol.ResultadoBusqueda r1 = Arbol.resolver(inicial, Nodo.TipoHeuristica.MANHATTAN);
            Arbol.ResultadoBusqueda r2 = Arbol.resolver(inicial, Nodo.TipoHeuristica.MANHATTAN_CONFLICTO_LINEAL);

            if (r1.encontrada) {
                System.out.println("\nManhattan:");
                imprimirPasos(r1.meta);
            }
            if (r2.encontrada) {
                System.out.println("\nManhattan + Conflicto Lineal:");
                imprimirPasos(r2.meta);
            }

            imprimirTabla(r1, r2);
            return;
        }

        System.out.println("Opcion invalida.");
    }

    private static void ejecutarUna(byte[] inicial, Nodo.TipoHeuristica heuristica) {
        Arbol.ResultadoBusqueda r = Arbol.resolver(inicial, heuristica);
        if (r.encontrada) {
            imprimirPasos(r.meta);
        }
        imprimirResultado(r);
    }

    private static void imprimirResultado(Arbol.ResultadoBusqueda r) {
        System.out.println("--------------------------------");
        System.out.println("Heuristica: " + r.heuristica.etiqueta());
        System.out.println("Nodos expandidos: " + r.nodosExpandidos);
        System.out.printf("Tiempo: %.3f ms%n", r.tiempoMs());

        if (r.encontrada) {
            System.out.println("Longitud solucion: " + r.pasos);
            System.out.println("Limite final f: " + r.limiteFinal);
        } else {
            System.out.println("No se encontro solucion.");
        }
        System.out.println("--------------------------------");
    }

    private static void imprimirTabla(Arbol.ResultadoBusqueda r1, Arbol.ResultadoBusqueda r2) {
        System.out.println("------------------------ TABLA COMPARATIVA --------------------------");
        System.out.printf("%-32s %-16s %-12s %-12s%n", "Heuristica", "Nodos", "Tiempo(ms)", "Pasos");
        System.out.println("---------------------------------------------------------------------");
        imprimirFila(r1);
        imprimirFila(r2);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("");
    }

    private static void imprimirFila(Arbol.ResultadoBusqueda r) {
        String pasos = r.encontrada ? String.valueOf(r.pasos) : "-";
        System.out.printf("%-32s %-16d %-12.3f %-12s%n",
                r.heuristica.etiqueta(), r.nodosExpandidos, r.tiempoMs(), pasos);
    }

    private static void imprimirPasos(Nodo meta) {
        List<Nodo> ruta = Nodo.rutaDesdeRaiz(meta);

        System.out.println("\nTotal movimientos: " + (ruta.size() - 1));
        for (int i = 0; i < ruta.size(); i++) {
            Nodo n = ruta.get(i);
            if (i == 0) {
                System.out.println("Paso 0 (Inicio)");
            } else {
                System.out.println("Paso " + i + " (" + n.movimiento + ")");
            }
            Nodo.imprimir(n.estado);
            System.out.println();
        }
    }

    private static byte[] leerEstadoManual(Scanner sc) {
        System.out.println("\nIngresa 25 valores separados por espacio (0 para vacio):");
        System.out.println("Ejemplo: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 0");

        while (true) {
            System.out.print("Estado: ");
            String linea = sc.nextLine().trim();
            Nodo.ValidacionEstado validacion = Nodo.validarEstadoTexto(linea);

            if (!validacion.valido) {
                System.out.println(validacion.error);
                continue;
            }
            if (!Nodo.esResolvible(validacion.estado)) {
                System.out.println("Estado no resoluble para 5x5.");
                continue;
            }

            return validacion.estado;
        }
    }

    private static int leerEntero(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero entero.");
            }
        }
    }
}
