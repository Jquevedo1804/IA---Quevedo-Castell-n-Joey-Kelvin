package puzzle;

import java.util.HashSet;
import java.util.Set;

public class Arbol {
    private static final int ENCONTRADO = -1;

    private final Nodo raiz;
    private long expandidos;
    private Nodo objetivo;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    public static ResultadoBusqueda resolver(byte[] inicial, Nodo.TipoHeuristica heuristica) {
        Nodo raiz = new Nodo(inicial, null, 'S', heuristica);
        Arbol arbol = new Arbol(raiz);
        return arbol.idaStar(heuristica);
    }

    public ResultadoBusqueda idaStar(Nodo.TipoHeuristica heuristica) {
        long inicio = System.nanoTime();
        expandidos = 0;
        objetivo = null;

        int limite = raiz.g + raiz.h;

        while (true) {
            Set<String> camino = new HashSet<>();
            camino.add(raiz.clave());

            int siguienteLimite = dfs(raiz, limite, heuristica, camino);

            if (objetivo != null) {
                return new ResultadoBusqueda(heuristica, true, objetivo, expandidos,
                        System.nanoTime() - inicio, objetivo.g, limite);
            }

            if (siguienteLimite == Integer.MAX_VALUE) {
                return new ResultadoBusqueda(heuristica, false, null, expandidos,
                        System.nanoTime() - inicio, -1, limite);
            }

            limite = siguienteLimite;
        }
    }

    private int dfs(Nodo actual, int limite, Nodo.TipoHeuristica heuristica, Set<String> camino) {
        // F del nodo actual
        int f = actual.g + actual.h;

        if (f > limite) {
            return f;
        }

        if (actual.esObjetivo()) {
            objetivo = actual;
            return ENCONTRADO;
        }

        expandidos++;
        int minimoExcedido = Integer.MAX_VALUE;

        for (Nodo sig : actual.sucesores(heuristica)) {
            String clave = sig.clave();
            if (camino.contains(clave)) {
                continue;
            }

            camino.add(clave);
            int r = dfs(sig, limite, heuristica, camino);
            camino.remove(clave);

            if (r == ENCONTRADO) {
                return ENCONTRADO;
            }

            if (r < minimoExcedido) {
                minimoExcedido = r;
            }
        }

        return minimoExcedido;
    }

    public static class ResultadoBusqueda {
        public final Nodo.TipoHeuristica heuristica;
        public final boolean encontrada;
        public final Nodo meta;
        public final long nodosExpandidos;
        public final long tiempoNs;
        public final int pasos;
        public final int limiteFinal;

        public ResultadoBusqueda(
                Nodo.TipoHeuristica heuristica,
                boolean encontrada,
                Nodo meta,
                long nodosExpandidos,
                long tiempoNs,
                int pasos,
                int limiteFinal
        ) {
            this.heuristica = heuristica;
            this.encontrada = encontrada;
            this.meta = meta;
            this.nodosExpandidos = nodosExpandidos;
            this.tiempoNs = tiempoNs;
            this.pasos = pasos;
            this.limiteFinal = limiteFinal;
        }

        public double tiempoMs() {
            return tiempoNs / 1_000_000.0;
        }
    }
}
