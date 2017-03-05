/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.servidor;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allen
 */
public class RMICalculadoraServidor implements RMICalculadoraInterface {

   public RMICalculadoraServidor(){
   
   }

    public void nn() {
        System.out.println("CREANDO EL REGISTRO DE OBJETOS REMOTOS");
        Registry reg = null;

        try {
            reg = LocateRegistry.createRegistry(5555);
        } catch (RemoteException e) {
            System.out.println("ERROR NO SE HA PODIDO CREAR EL REGISTRO");
            e.printStackTrace();
        }
        System.out.println("CREANDO EL OBJETO SERVIDOR E INCRIBIENDOLO EN EL REGISTRO");
        RMICalculadoraServidor objetoServidor = new RMICalculadoraServidor();
        try {
            reg.rebind("CALCULADORA",(RMICalculadoraInterface) UnicastRemoteObject.exportObject(objetoServidor, 0));
            
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println("ERROR NO SE HA PODIDO INSCRIBIR EL OBJETO SERVIDOR");
            e.printStackTrace();
        }  
    }
    
     @Override
    public String valorarVentas(int venta) throws RemoteException {
        String mensaje = "";

        if (venta <= 1000) {
            mensaje = "Eres un mal vendedor";
        } else if (venta > 1000 && venta <= 10000) {
            mensaje = "Has vendido lo normal, superate!";
        } else if (venta > 10000) {
            mensaje = "Eres una máquina sigue asi!!!";
        }

        return mensaje;
    }
    
}
