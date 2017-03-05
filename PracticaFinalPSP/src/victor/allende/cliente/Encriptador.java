/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author allen
 */
public class Encriptador {

    public void encriptar(ObjectOutputStream cuenta) {

        try {

            //AQUI SE ENCRIPTA
            // CREACION Y OBTENCION DE LAS CLAVES ESPECIFICANDO EL ALGORITMO
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair clavesRSA = keyGen.generateKeyPair();
//CLAVE PRIVADA
            PrivateKey clavePrivada = clavesRSA.getPrivate();
//CLAVE PUBLICA
            PublicKey clavePublica = clavesRSA.getPublic();
//SE VAN A MOSTRAR LAS CLAVES, AUNQUE NO ES ACONSEJABLE
            System.out.println("clavePublica: " + clavePublica);
            System.out.println("clavePrivada: " + clavePrivada);


            byte[] bufferClaro = ("La información de la sede es: " + cuenta).getBytes();

            // SE VA A CIFRAR EL TEXTO PLANO CON LA CLAVE PUBLICA RSA, SE PREPARA EL CIFRADOR
            Cipher cifrador = Cipher.getInstance("RSA");
            cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
            System.out.println("Cifrar con clave pública el Texto:");
// CIFRADO DEL TEXTO PLANO
            byte[] bufferCifrado = cifrador.doFinal(bufferClaro);
            System.out.println("Texto CIFRADO");
// SE MUESTRA EL TEXTO CIFRADO. SI SE EJECUTA EL PROGRAMA VARIAS VECES, SE VERA
// QUE EL TEXTO CIFRADO SIEMPRE ES DISTINTO
            System.out.write(bufferCifrado);
            System.out.println("\n_______________________________");

            cifrador.init(Cipher.DECRYPT_MODE, clavePrivada);
            System.out.println("Descifrar con clave privada");
            byte[] bufferClaro2;
// OBTENER Y MOSTRAR EL TEXTO DESCIFRADO
            bufferClaro2 = cifrador.doFinal(bufferCifrado);
            System.out.println("TEXTO DESCIFRADO");
            System.out.write(bufferClaro2);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }

}
