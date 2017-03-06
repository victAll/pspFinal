/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author allen
 */
public class MulticastServidor {

    
    public void envioMulticast(JTextField jtfCadena) {

        MulticastSocket smulti;
        try {
            String cadena = "";
            smulti = new MulticastSocket();
            int puerto = 12345;
            InetAddress grupo = InetAddress.getByName("localhost");
            
            while (cadena != null) {
                cadena = jtfCadena.getText();
                System.out.println("DATOS QUE SE VAN A ENVIAR AL GRUPO DE ORDENADORES");
// SE PRODUCE EL ENVIO DE DATOS AL GRUPO
                DatagramPacket paquete = new DatagramPacket(cadena.getBytes(), cadena.length(), grupo, puerto);
                smulti.send(paquete);
                
                System.out.println("cadena "+ cadena + paquete);
            }
            smulti.close();
            System.out.println("EL SOCKET SE HA CERRADO ");

        } catch (IOException ex) {
            Logger.getLogger(MulticastServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
