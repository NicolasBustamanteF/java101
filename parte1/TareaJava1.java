package parte1;

import java.util.Arrays;

public class TareaJava1 {
        public static void main(String[] args) {

            int[] listaNumRandom = getListaNumRandom();

            ordenarLista(listaNumRandom);

            mostrarPrimos(listaNumRandom);

            mostrarNoPrimos(listaNumRandom);
        }

        private static void ordenarLista(int[] listaNumRandom) {
            //ordenar
            Arrays.sort(listaNumRandom);
            System.out.println("Números de mayor a menor:");

            for (int i = listaNumRandom.length - 1; i >= 0; i--) {
                System.out.println(listaNumRandom[i]);
            }
            //num mayor y num menor
            System.out.println("Número menor: " + listaNumRandom[0]);
            System.out.println("Número mayor: " + listaNumRandom[listaNumRandom.length - 1]);
        }

        private static int[] getListaNumRandom() {
            int[] listaNumRandom = new int[20];

            for (int i = 0; i < listaNumRandom.length; i++) {
                listaNumRandom[i] = 1 + (int) (Math.random() * 100);
            }
            return listaNumRandom;
        }
    private static boolean esPrimo(int numero) {

        if (numero <= 1) {
            return false;
        }

        for (int i = 2; i < numero; i++) {

            if (numero % i == 0) {
                return false;
            }

        }

        return true;
    }
    private static void mostrarPrimos(int[] listaNumRandom) {

        System.out.println("Números primos:");

        for (int i = 0; i < listaNumRandom.length; i++) {

            if (esPrimo(listaNumRandom[i])) {
                System.out.println(listaNumRandom[i]);
            }

        }

    }
    private static void mostrarNoPrimos(int[] listaNumRandom) {

        System.out.println("Números NO primos:");

        for (int i = 0; i < listaNumRandom.length; i++) {

            if (!esPrimo(listaNumRandom[i])) {
                System.out.println(listaNumRandom[i]);
            }

        }

    }
}
