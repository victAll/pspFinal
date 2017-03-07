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
public class Sedes extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    Socket conector = null;
    DataInputStream fentrada;
    DataOutputStream fsalida;
    static String nombre;
    static JTextField mensaje = new JTextField();
    private JScrollPane scrollpane;
    static JTextArea areatexto;
    JButton boton = new JButton("ENVIAR");
    JButton desconectar = new JButton("SALIR");
    JButton enviarObjeto = new JButton("CENTRAL");
    Boolean repetir = true;
    String comprobar = "";
    HiloCliente h;
    boolean primer = true;

    public Sedes(Socket s, String nombre) {
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
//            String texto = "SE ENTRA EN EL CHAT ----";
//            fsalida.writeUTF(texto);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Sedes() {
    }

    public void escrituraObjetoCuenta() {

        int numeropuerto = 6000;
        // PREPARACION DEL OBJETO Y ENVIO
        Cuenta cuenta = new Cuenta();
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
            System.out.println("ESPERANDO A LA CENTRAL FINANCIERA");
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
            Logger.getLogger(Sede.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sede.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton) {
            String texto = nombre + " >> " + mensaje.getText();
            mensaje.setText("");
            try {
                fsalida.writeUTF(texto);
                fsalida.flush();
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
        String texto;
        while (repetir) {
            comprobar = areatexto.getText();
            if (!comprobar.equalsIgnoreCase("")) {

                System.out.println("comprobar: " + comprobar);
            }
            try {
                texto = fentrada.readUTF();
                if (comprobar.equalsIgnoreCase("")) {
                    areatexto.setText(texto);
                } else {
                    areatexto.setText(texto);
                }
                prueba();
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
            e.printStackTrace();
        }
    }
    public void prueba() {
        if (primer) {
            h = new HiloCliente(areatexto);
            h.start();
            primer = false;
        } else if (!h.isAlive()) {
            h = new HiloCliente(areatexto);
            h.start();
        }
    }
    
    public static void main(String[] args) {
        int puerto = 44444;
        nombre = JOptionPane.showInputDialog("INTRODUZCA TU NOMBRE O NICK");
       
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
            Sedes cliente = new Sedes(s, nombre);
            cliente.setBounds(0, 0, 540, 400);
            cliente.setVisible(true);
            cliente.ejecutar();
        } else {
            System.out.println("EL NOMBRE ESTA VACIO");
        }
    } 
}
