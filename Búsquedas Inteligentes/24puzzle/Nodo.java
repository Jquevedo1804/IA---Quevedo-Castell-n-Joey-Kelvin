package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Nodo {
    public enum TipoHeuristica {
        MANHATTAN("Manhattan"),
        MANHATTAN_CONFLICTO_LINEAL("Manhattan + Conflicto Lineal");

        private final String etiqueta;

        TipoHeuristica(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String etiqueta() {
            return etiqueta;
        }
    }

    public static class ValidacionEstado {
        public final boolean valido;
        public final byte[] estado;
        public final String error;

        private ValidacionEstado(boolean valido, byte[] estado, String error) {
            this.valido = valido;
            this.estado = estado;
            this.error = error;
        }

        public static ValidacionEstado ok(byte[] estado) {
            return new ValidacionEstado(true, estado, null);
        }

        public static ValidacionEstado error(String error) {
            return new ValidacionEstado(false, null, error);
        }
    }

    public static final byte VACIO = 0;

    public static final byte[] ESTADO_OBJETIVO = new byte[] {
        1, 2, 3, 4, 5,
        6, 7, 8, 9, 10,
        11, 12, 13, 14, 15,
        16, 17, 18, 19, 20,
        21, 22, 23, 24, 0
    };

    public final byte[] estado;
    public final int vacio;
    public final Nodo padre;
    public final int g;
    public final int h;
    public final char movimiento;

    public Nodo(byte[] estado, Nodo padre, char movimiento, TipoHeuristica tipoHeuristica) {
        this.estado = Arrays.copyOf(estado, estado.length);
        this.vacio = encontrarVacio(this.estado);
        this.padre = padre;
        this.movimiento = movimiento;
        // g(n) para la formula de evaluacion
        this.g = (padre == null) ? 0 : padre.g + 1;
        this.h = calcularHeuristica(this.estado, tipoHeuristica);
    }

    public static byte[] copiarObjetivo() {
        return Arrays.copyOf(ESTADO_OBJETIVO, ESTADO_OBJETIVO.length);
    }

    public static ValidacionEstado validarEstadoTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return ValidacionEstado.error("Entrada vacia.");
        }

        String[] t = texto.replace(',', ' ').replace('_', '0').trim().split("\\s+");

        if (t.length != 25) {
            return ValidacionEstado.error("Se esperaban 25 valores.");
        }

        boolean[] usado = new boolean[25];
        byte[] estado = new byte[25];

        for (int i = 0; i < t.length; i++) {
            int v;
            try {
                v = Integer.parseInt(t[i]);
            } catch (NumberFormatException e) {
                return ValidacionEstado.error("Valor invalido: " + t[i]);
            }

            if (v < 0 || v >= 25) {
                return ValidacionEstado.error("Fuera de rango [0,24]: " + v);
            }

            if (usado[v]) {
                return ValidacionEstado.error("Valor repetido: " + v);
            }

            usado[v] = true;
            estado[i] = (byte) v;
        }

        return ValidacionEstado.ok(estado);
    }

    public static boolean esResolvible(byte[] estado) {
        int inv = 0;

        for (int i = 0; i < estado.length; i++) {
            if (estado[i] == VACIO) {
                continue;
            }
            for (int j = i + 1; j < estado.length; j++) {
                if (estado[j] == VACIO) {
                    continue;
                }
                if (estado[i] > estado[j]) {
                    inv++;
                }
            }
        }

        return inv % 2 == 0;
    }

    public static byte[] generarAleatorioDesdeObjetivo(int movimientos, Random random) {
        if (movimientos < 0) {
            throw new IllegalArgumentException("movimientos debe ser >= 0");
        }
        if (random == null) {
            throw new IllegalArgumentException("random no puede ser null");
        }

        byte[] e = copiarObjetivo();
        int vacio = 25 - 1;
        int anterior = -1;

        for (int i = 0; i < movimientos; i++) {
            List<Integer> vecinos = vecinos(vacio);
            if (anterior != -1 && vecinos.size() > 1) {
                vecinos.remove(Integer.valueOf(anterior));
            }

            int sig = vecinos.get(random.nextInt(vecinos.size()));
            e[vacio] = e[sig];
            e[sig] = VACIO;
            anterior = vacio;
            vacio = sig;
        }

        return e;
    }

    public static String comoLista(byte[] estado) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < estado.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(estado[i]);
        }
        return sb.toString();
    }

    public static List<Nodo> rutaDesdeRaiz(Nodo meta) {
        List<Nodo> ruta = new ArrayList<>();
        Nodo actual = meta;

        while (actual != null) {
            ruta.add(actual);
            actual = actual.padre;
        }

        Collections.reverse(ruta);
        return ruta;
    }

    public boolean esObjetivo() {
        return Arrays.equals(estado, ESTADO_OBJETIVO);
    }

    public String clave() {
        return Arrays.toString(estado);
    }

    public static int encontrarVacio(byte[] estado) {
        for (int i = 0; i < estado.length; i++) {
            if (estado[i] == VACIO) {
                return i;
            }
        }
        throw new IllegalArgumentException("El estado no contiene 0.");
    }

    public List<Nodo> sucesores(TipoHeuristica tipoHeuristica) {
        List<Nodo> lista = new ArrayList<>(4);

        int fila = vacio / 5;
        int col = vacio % 5;

        if (fila > 0) {
            lista.add(mover(vacio - 5, 'U', tipoHeuristica));
        }
        if (fila < 5 - 1) {
            lista.add(mover(vacio + 5, 'D', tipoHeuristica));
        }
        if (col > 0) {
            lista.add(mover(vacio - 1, 'L', tipoHeuristica));
        }
        if (col < 5 - 1) {
            lista.add(mover(vacio + 1, 'R', tipoHeuristica));
        }

        return lista;
    }

    private static List<Integer> vecinos(int vacio) {
        List<Integer> v = new ArrayList<>(4);

        int fila = vacio / 5;
        int col = vacio % 5;

        if (fila > 0) {
            v.add(vacio - 5);
        }
        if (fila < 5 - 1) {
            v.add(vacio + 5);
        }
        if (col > 0) {
            v.add(vacio - 1);
        }
        if (col < 5 - 1) {
            v.add(vacio + 1);
        }

        return v;
    }

    private Nodo mover(int nuevoVacio, char mov, TipoHeuristica tipoHeuristica) {
        byte[] nuevo = Arrays.copyOf(estado, estado.length);
        nuevo[vacio] = nuevo[nuevoVacio];
        nuevo[nuevoVacio] = VACIO;
        return new Nodo(nuevo, this, mov, tipoHeuristica);
    }

    public static int calcularHeuristica(byte[] estado, TipoHeuristica tipoHeuristica) {
        int manhattan = distanciaManhattan(estado);
        if (tipoHeuristica == TipoHeuristica.MANHATTAN) {
            return manhattan;
        }
        return manhattan + conflictoLineal(estado);
    }

    private static int distanciaManhattan(byte[] estado) {
        int total = 0;

        for (int i = 0; i < estado.length; i++) {
            int pieza = estado[i];
            if (pieza == VACIO) {
                continue;
            }

            int filaActual = i / 5;
            int colActual = i % 5;
            int filaObjetivo = (pieza - 1) / 5;
            int colObjetivo = (pieza - 1) % 5;

            // Formula manhattan: h(n) = |filaActual - filaObjetivo| + |colActual - colObjetivo|
            total += Math.abs(filaActual - filaObjetivo) + Math.abs(colActual - colObjetivo);
        }
        // return h(n) que sera usada para f(n) = g(n) + h(n) 
        return total;
    }

    /* Reglas:  1. Las dos fichas deben de estar en la misma fila/columna
                2. Ambas fichas pertenecen a la fila/columan de su estado objetivo.
                3. Ambas fichas deben estar en orden invertido.
    */
    private static int conflictoLineal(byte[] estado) {
        int extra = 0;

        for (int fila = 0; fila < 5; fila++) {
            for (int c1 = 0; c1 < 5; c1++) {
                int p1 = estado[fila * 5 + c1];
                if (p1 == VACIO || (p1 - 1) / 5 != fila) {
                    continue;
                }

                for (int c2 = c1 + 1; c2 < 5; c2++) {
                    int p2 = estado[fila * 5 + c2];
                    if (p2 == VACIO || (p2 - 1) / 5 != fila) {
                        continue;
                    }

                    if ((p1 - 1) % 5 > (p2 - 1) % 5) {
                        extra += 2;
                    }
                }
            }
        }

        for (int col = 0; col < 5; col++) {
            for (int f1 = 0; f1 < 5; f1++) {
                int p1 = estado[f1 * 5 + col];
                if (p1 == VACIO || (p1 - 1) % 5 != col) {
                    continue;
                }

                for (int f2 = f1 + 1; f2 < 5; f2++) {
                    int p2 = estado[f2 * 5 + col];
                    if (p2 == VACIO || (p2 - 1) % 5 != col) {
                        continue;
                    }

                    if ((p1 - 1) / 5 > (p2 - 1) / 5) {
                        extra += 2;
                    }
                }
            }
        }
        // return extra el cual se sumara al h(n) de Manhattan.
        return extra;
    }

    public static void imprimir(byte[] estado) {
        for (int fila = 0; fila < 5; fila++) {
            StringBuilder sb = new StringBuilder("|");
            for (int col = 0; col < 5; col++) {
                int v = estado[fila * 5 + col];
                if (v == VACIO) {
                    sb.append(" __");
                } else if (v < 10) {
                    sb.append("  ").append(v);
                } else {
                    sb.append(" ").append(v);
                }
            }
            sb.append(" |");
            System.out.println(sb.toString());
        }
    }
}

