/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JTextArea;

/**
 *
 * @author allen
 */
public class HiloCliente extends Thread {

    MulticastSocket smulti;
    InetAddress grupo;
    DatagramPacket paquete;
    String mensaje = "";
    JTextArea areatexto;
    
    public HiloCliente( JTextArea areatexto) {
        this.areatexto=areatexto;
        this.mensaje = "";
    }

    @Override
    public void run() {

        int puerto = 12345;
        try {
            smulti = new MulticastSocket(puerto);
            grupo = InetAddress.getByName("225.0.0.0");

            // NOS VAMOS A UNIR AL GRUPO DE ORDENADORES
            smulti.joinGroup(grupo);

            byte[] buf = new byte[1000];
            // SE RECIBE EL PAQUETE DEL SERVIDOR MULTICAST
            paquete = new DatagramPacket(buf, buf.length);
            smulti.receive(paquete);
            mensaje = new String(paquete.getData());
            System.out.println("SE HA RECIBIDO DEL SERVIDOR MULTICAST EL MENSAJE " + mensaje.trim());

            String comprobar = areatexto.getText();
            if(comprobar.equalsIgnoreCase("")){
            
                areatexto.setText("Mensaje de la sede central: " + mensaje + "\n");
            }else{
                areatexto.append("Mensaje de la sede central: " + mensaje + "\n");
            }
            
            
            
            
            

            // ABANDONAMOS EL GRUPO DE ORDENADORES
            smulti.leaveGroup(grupo);
            smulti.close();
            System.out.println("SE HA CERRADO EL SOCKET MULTICAST");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
