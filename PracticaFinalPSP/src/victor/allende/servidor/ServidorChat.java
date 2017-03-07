/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.servidor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import victor.allende.hilo_servidor.HiloServidor;

/**
 *
 * @author allen
 */
public class ServidorChat extends JFrame implements ActionListener {

    private static final long serialversionUID = 1L;
    private static ServerSocket servidor;
    private static final int PUERTO = 44444;
    public static int CONEXIONES = 0;
    public static int ACTUALES = 0;
    private static int MAXIMO = 4;
    public static JTextField mensaje = new JTextField("");
    public static JTextField mensaje2 = new JTextField("");
    static JTextField mensajeSedes = new JTextField();
    private JScrollPane scrollpanel;
    public static JTextArea areaTexto;
    private JButton boton = new JButton("ENVIAR");
    private JButton salir = new JButton("SALIR");
    private JButton enviar = new JButton("MENSAJE");

    public static Socket[] tabla = new Socket[10];
    //para mandar mensajes
    public Socket socketServidor = null;
    private DataInputStream fentrada;
    private DataOutputStream fsalida;
    Boolean repetir = true;
    MulticastServidor multicastServidor = new MulticastServidor();

    public ServidorChat() {
        super("VENTANA DEL SERVIDOR DEL CHAT");
        setLayout(null);
        mensajeSedes.setBounds(10, 40, 400, 30);
        add(mensajeSedes);
        mensaje.setBounds(10, 10, 400, 30);
        mensaje.setEditable(false);
        add(mensaje);
        mensaje2.setBounds(10, 338, 400, 30);
        mensaje2.setEditable(false);
        add(mensaje2);
        areaTexto = new JTextArea();
        scrollpanel = new JScrollPane(areaTexto);
        scrollpanel.setBounds(10, 70, 400, 300);
        add(scrollpanel);
//        boton.setBounds(420, 10, 100, 30);
//        boton.addActionListener(this);
//        add(boton);
        salir.setBounds(420, 50, 100, 30);
        salir.addActionListener(this);
        add(salir);
        enviar.setBounds(420, 90, 100, 30);
        enviar.addActionListener(this);
        add(enviar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //pruebaas remoto
//        RMICalculadoraServidor cs = new RMICalculadoraServidor();
//        cs.nn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == enviar) {

            multicastServidor.envioMulticast(mensajeSedes);
            mensajeSedes.setText("");
        }
        if (e.getSource() == salir) {
            try {
                servidor.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        RMICalculadoraServidor cs = new RMICalculadoraServidor();
        cs.nn();
        try {
            Socket s = new Socket();
            servidor = new ServerSocket(PUERTO);

            System.out.println("SERVIDOR INICIADO");
            ServidorChat pantalla = new ServidorChat();
            pantalla.setBounds(0, 0, 540, 400);
            pantalla.setVisible(true);
            mensaje.setText("NUMERO DE CONEXIONES ACTUALES: " + CONEXIONES);
            while (CONEXIONES < MAXIMO) {

                s = new Socket();
                try {
                    s = servidor.accept();
                } catch (SocketException ns) {
                    break;
                }
                tabla[CONEXIONES] = s;
                CONEXIONES++;
                ACTUALES++;
                HiloServidor hilo = new HiloServidor(s);
                hilo.start();

            }
            if (!servidor.isClosed()) {
                mensaje2.setForeground(Color.red);
                mensaje2.setText("MAXIMO NUMERO DE CONEXIONES ESTABLECIDAD : " + CONEXIONES);
                servidor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
