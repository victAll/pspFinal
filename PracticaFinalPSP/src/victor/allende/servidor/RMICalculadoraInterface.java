/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author allen
 */
public interface RMICalculadoraInterface extends Remote{
    
    public String valorarVentas(int venta, int menor, int mayor) throws RemoteException;  
}
