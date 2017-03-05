/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.central_financiera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import victor.allende.cliente.Cuenta;
import victor.allende.editor_fichero.EditorFichero;

/**
 *
 * @author allen
 */
public class RecibirObjetoCuenta {

    EditorFichero ef = new EditorFichero();
    
    public int recibirObjeto() throws IOException {
        int cantidad=0;
        String ordenadorServidor = "localhost";
        int puerto = 6000;
        System.out.println("PROGRAMA CLIENTE INICIADO");
        try {
            Socket cliente = new Socket(ordenadorServidor, puerto);

            ObjectInputStream leerObjetos  = new ObjectInputStream(cliente.getInputStream());
            Cuenta cuentaRecibida = (Cuenta) leerObjetos.readObject();
            System.out.println("EN EL CLIENTE SE HA RECIBIDO OBJETO DE LA SEDE: "
                    + cuentaRecibida.getNombreSede() + " FECHA "
                    + cuentaRecibida.getFecha()+ " cuenta total: "+ cuentaRecibida.getCantidadTotal());

            ObjectOutputStream escribirObjeto = new ObjectOutputStream(cliente.getOutputStream());

            escribirObjeto.writeObject(cuentaRecibida);
            cantidad = cuentaRecibida.getCantidadTotal();
            System.out.println("EN EL CLIENTE SE HA MANDADO OBJETO DE LA SEDE: "
                    + cuentaRecibida.getNombreSede() + " FECHA "
                    + cuentaRecibida.getFecha()+ " CUENTA TOTAL: "+ cuentaRecibida.getCantidadTotal());
            
            ef.escribir(cuentaRecibida);
            
            
            leerObjetos.close();
            escribirObjeto.close();
            cliente.close();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cantidad;
    }
    
//    public void imprimir(){
//        
//        String ordenadorServidor = "localhost";
//        int puerto = 6000;
//        
//        try {
//            Socket cliente = new Socket(ordenadorServidor, puerto);
//
//            ObjectInputStream leerObjetos  = new ObjectInputStream(cliente.getInputStream());
//            Cuenta cuentaRecibida = (Cuenta) leerObjetos.readObject();
//       
//
//            ObjectOutputStream escribirObjeto = new ObjectOutputStream(cliente.getOutputStream());
//
//            escribirObjeto.writeObject(cuentaRecibida);
//            
//           ef.escribir(cuentaRecibida);
//            
//            leerObjetos.close();
//            escribirObjeto.close();
//            cliente.close();
//            
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    
//    }
}
