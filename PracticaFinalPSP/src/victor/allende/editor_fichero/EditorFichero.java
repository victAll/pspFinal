/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.editor_fichero;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import victor.allende.cliente.Cuenta;

/**
 *
 * @author allen
 */
public class EditorFichero {

    int segundos;
    long miliSegundos;
    Calendar calendario = Calendar.getInstance();

    public void escribir(Cuenta cuenta) {
        segundos = calendario.get(Calendar.SECOND);
        miliSegundos = calendario.get(Calendar.MILLISECOND);
        FileWriter escritor = null;
        try {
            String nombre = cuenta.getNombreSede();

            File archivo = new File(nombre + ".txt");

            escritor = new FileWriter(archivo, true);

            escritor.write("Nombre sede: " + cuenta.getNombreSede() + "\r\n");
            escritor.write("fecha: " + cuenta.getFecha() + "\r\n");
            escritor.write("Cantidad total: " + String.valueOf(cuenta.getCantidadTotal()) + " €\r\n");

        } catch (IOException ex) {
            Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                escritor.close();
            } catch (IOException ex) {
                Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void guardarCantidad(Cuenta cuenta) {

        FileWriter escritor = null;
        String nombre = "CuentaPromedio";
        try {

            File archivo = new File(nombre + ".txt");

            escritor = new FileWriter(archivo, true);

            escritor.write(String.valueOf(cuenta.getCantidadTotal()) + "\r\n");

        } catch (IOException ex) {
            Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                escritor.close();
            } catch (IOException ex) {
                Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int imprimirCantidad() {
        String linea = null;
        String suma;
        FileReader fr;
        int total=0;
        try {
            fr = new FileReader("CuentaPromedio.txt");
            BufferedReader br = new BufferedReader(fr);

            while ((linea = br.readLine()) != null) {
                suma = linea;
                total += Integer.parseInt(suma);
                System.out.println("linea "+linea);
                System.out.println("total "+total);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditorFichero.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
}
