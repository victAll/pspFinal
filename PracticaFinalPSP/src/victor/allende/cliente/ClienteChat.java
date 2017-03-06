/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author allen
 */
public class ClienteChat extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    Socket conector = null;
    DataInputStream fentrada;
    DataOutputStream fsalida;
    String nombre;
    static JTextField mensaje = new JTextField();
    private JScrollPane scrollpane;
    static JTextArea areatexto;
    JButton boton = new JButton("ENVIAR");
    JButton desconectar = new JButton("SALIR");
    JButton enviarObjeto = new JButton("Enviar a central");
    Boolean repetir = true;

    public ClienteChat(Socket s, String nombre) {
        super("CONEXION DEL CLIENTE DEL CHAT: " + nombre);
        setLayout(null);
        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);
        areatexto = new JTextArea();
        scrollpane = new JScrollPane(areatexto);
        scrollpane.setBounds(10, 50, 400, 300);
        add(scrollpane);
        boton.setBounds(420, 10, 100, 30);
        boton.addActionListener(this);
        add(boton);
        desconectar.setBounds(420, 50, 100, 30);
        desconectar.addActionListener(this);
        add(desconectar);
        enviarObjeto.setBounds(420, 90, 100, 30);
        enviarObjeto.addActionListener(this);
        add(enviarObjeto);
        areatexto.setEditable(false);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        conector = s;
        this.nombre = nombre;
        try {
            fentrada = new DataInputStream(conector.getInputStream());
            fsalida = new DataOutputStream(conector.getOutputStream());
            String texto = "SE ENTRA EN EL CHAT ----";
            fsalida.writeUTF(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClienteChat() {
    }

    public void escrituraObjetoCuenta() {

        int numeropuerto = 6000;
        // PREPARACION DEL OBJETO Y ENVIO
        Cuenta cuenta = new Cuenta();
        String nombre = "Sede Valencia";
        Date fecha = new Date();
        int cantidadTotal;
        String cadenaFecha;

        cuenta.setNombreSede(nombre);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        cadenaFecha = formato.format(fecha);
        cuenta.setFecha(cadenaFecha);
        cantidadTotal = (int) (Math.random() * (20000 - 200 + 1) + 200);
        cuenta.setCantidadTotal(cantidadTotal);
        System.out.println("aqui");
        try {
            ServerSocket servidor;
            servidor = new ServerSocket(numeropuerto);
            System.out.println("ESPERANDO A LA CENTRAL FINNCIERA");
            Socket cliente = servidor.accept();
            ObjectOutputStream escrituraObjeto = new ObjectOutputStream(cliente.getOutputStream());
            escrituraObjeto.writeObject(cuenta);

            Encriptador encriptador = new Encriptador();
            encriptador.encriptar(escrituraObjeto);

            ObjectInputStream lecturaObjetos = new ObjectInputStream(cliente.getInputStream());
            Cuenta cuentaRecibida = (Cuenta) lecturaObjetos.readObject();

            System.out.println(cuenta.getNombreSede());

            lecturaObjetos.close();
            escrituraObjeto.close();
            cliente.close();
            servidor.close();
        } catch (IOException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton) {
            String texto = nombre + " >> " + mensaje.getText();
            mensaje.setText(" ");
            try {
                fsalida.writeUTF(texto);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == enviarObjeto) {

            escrituraObjetoCuenta();

        }
        if (e.getSource() == desconectar) {
            String texto = " <<< ABANDONA EL CHAT " + nombre;
            try {
                fsalida.writeUTF(texto);
                fsalida.writeUTF("*");
                repetir = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void ejecutar() {
        System.out.println("ejecutar");

        String texto = "";
        while (repetir) {
            try {
                texto = fentrada.readUTF();
                areatexto.setText(texto);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n"
                        + e.getMessage(), "<< mensaje de error >>", JOptionPane.ERROR_MESSAGE);
                repetir = false;
                e.printStackTrace();
            }
        }
        try {
            conector.close();
            System.exit(0);
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    public void recibirMulticast() {
//
//        int puerto = 12345;
//        try {
//            
//            InetAddress grupo = InetAddress.getLocalHost();
//            MulticastSocket smulti = new MulticastSocket(puerto);
//// NOS VAMOS A UNIR AL GRUPO DE ORDENADORES
//            smulti.joinGroup(grupo);
//            String mensaje = "";
//            byte[] buf = new byte[1000];
//            while (!mensaje.trim().equals("*")) {
//// SE RECIBE EL PAQUETE DEL SERVIDOR MULTICAST
//                DatagramPacket paquete = new DatagramPacket(buf, buf.length);
//                smulti.receive(paquete);
//                mensaje = new String(paquete.getData());
//                System.out.println("SE HA RECIBIDO DEL SERVIDOR MULTICAST EL MENSAJE " + mensaje.trim());
//                areatexto.setText(mensaje);
//                System.out.println(mensaje);
//           }
//// ABANDONAMOS EL GRUPO DE ORDENADORES
//            smulti.leaveGroup(grupo);
//            smulti.close();
//            System.out.println("SE HA CERRADO EL SOCKET MULTICAST");
//        } catch (IOException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    public void recibirMulticast() {
        byte[] bufer = new byte[1024];
        int puerto = 12345;
        try {
            DatagramSocket socket = new DatagramSocket(puerto);
            System.out.println("ESPERANDO DATAGRAMA DESDE EL CLIENTE");
            DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
            try {
                socket.receive(recibo);
                int byterecibidos = recibo.getLength();
                String mensaje = new String(recibo.getData());
                
               areatexto.append("mensaje desde la central"+mensaje);
                
                
                System.out.println("NUMERO DE BYTE RECIBIDOS : " + byterecibidos);
                System.out.println("CONTENIDO DEL PAQUETE : " + mensaje.trim());
                System.out.println("PUERTO ORIGEN DEL MENSAJE : " + recibo.getPort());
                System.out.println("IP ORIGEN : " + recibo.getAddress().getHostAddress());
                System.out.println("PUERTO DESTINO DEL MENSAJE : " + socket.getLocalPort());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        
        int puerto = 44444;
        String nombre = JOptionPane.showInputDialog("INTRODUZCA TU NOMBRE O NICK");
        Socket s = null;
        try {
            s = new Socket("localhost", puerto);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR"
                    + e.getMessage(), "<< MENSAJE DE ERROR >>", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            e.printStackTrace();
        }
        if (!nombre.trim().equals("")) {
            ClienteChat cliente = new ClienteChat(s, nombre);
            cliente.setBounds(0, 0, 540, 400);
            cliente.setVisible(true);
            cliente.ejecutar();
            cliente.recibirMulticast();
            //cc.escrituraObjetoCuenta();

        } else {
            System.out.println("EL NOMBRE ESTA VACIO");
        }
        

    }
}
