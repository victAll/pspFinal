/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.central_financiera;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import victor.allende.editor_fichero.EditorFichero;
import victor.allende.servidor.RMICalculadoraInterface;

/**
 *
 * @author allen
 */
public class RMICalculadoraCliente extends JFrame implements ActionListener {

    static JTextField mensaje = new JTextField();
    private JScrollPane scrollpane;
    static JTextArea areatexto;
    JButton boton = new JButton("aceptar objeto");
    JButton desconectar = new JButton("SALIR");
    RecibirObjetoCuenta roc = new RecibirObjetoCuenta();
    EditorFichero ef = new EditorFichero();

    public RMICalculadoraCliente() {
        super("CENTRAL FINANCIERA: ");
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
        areatexto.setEditable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton) {
            String texto = " >> " + mensaje.getText();
            mensaje.setText(" ");
            aceptarObjeto();
        }
        if (e.getSource() == desconectar) {

            System.exit(0);
        }
    }

    public void aceptarObjeto() {

        Registry reg = null;
        int informecantidad = 0;

        RMICalculadoraInterface calculadora = null;
        System.out.println("LOCALIZANDO REGISTRO DE OBJETOS REMOTOS");
        try {
            reg = LocateRegistry.getRegistry("localhost", 5555);
            System.out.println("OBTENIENDO EL STUB DEL OBJETO REMOTO");
            calculadora = (RMICalculadoraInterface) reg.lookup("CALCULADORA");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException ex) {
            Logger.getLogger(RMICalculadoraCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (calculadora != null) {
            System.out.println("REALIZANDO OPERACIONES CON EL OBJETO REMOTO");
            try {
                informecantidad = roc.recibirObjeto();
                String menor = JOptionPane.showInputDialog("ingrese la cantidad menor");
                String mayor = JOptionPane.showInputDialog("ingrese la cantidad mayor");
                int _menor = Integer.parseInt(menor);
                int _mayor = Integer.parseInt(mayor);
                //total del dia de todas las sedes
                int total = ef.imprimirCantidad();
                //llamada al metodo rmi
                String m = calculadora.valorarVentas(total,_menor, _mayor);
                JOptionPane.showMessageDialog(null, "La venta es:   " + informecantidad + " " + m);
                JOptionPane.showMessageDialog(null, "La suma total es:   " + total);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(RMICalculadoraCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    

    public static void main(String[] args) {
        RMICalculadoraCliente pantalla = new RMICalculadoraCliente();
        pantalla.setBounds(200, 100, 540, 400);
        pantalla.setVisible(true);
    }
}
