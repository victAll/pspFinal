/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.allende.cliente;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author allen
 */
public class Cuenta implements Serializable{
    private String nombreSede;
    private String fecha;
    private int cantidadTotal;

    public Cuenta(String nombreSede, String fecha, int cantidadTotal) {
        this.nombreSede = nombreSede;
        this.fecha = fecha;
        this.cantidadTotal = cantidadTotal;
    }
    public Cuenta(){
        super();
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    
    
    
}
