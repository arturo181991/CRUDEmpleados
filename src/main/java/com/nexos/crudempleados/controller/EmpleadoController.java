/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nexos.crudempleados.controller;

import com.nexos.crudempleados.ejb.DepartamentoFacade;
import com.nexos.crudempleados.ejb.EmpleadoFacade;
import com.nexos.crudempleados.model.Departamento;
import com.nexos.crudempleados.model.Empleado;
import com.nexos.crudempleados.util.Utilities;
import com.nexos.crudempleados.util.Utilities.TiposDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author artur
 */
@Named
@ViewScoped
public class EmpleadoController implements Serializable {

    @EJB
    private EmpleadoFacade empleadoFacade;
    @EJB
    private DepartamentoFacade departamentoFacade;
    private Empleado empleadoSelected, empleadoSelectedTMP;
    private List<Empleado> listaEmpleados;

    public Empleado getEmpleadoSelected() {
        if (empleadoSelected == null) {
            empleadoSelected = new Empleado();
        }
        return empleadoSelected;
    }

    public void setEmpleadoSelected(Empleado empleado) {
        this.empleadoSelected = empleado;
    }

    public Empleado getEmpleadoSelectedTMP() {
        return empleadoSelectedTMP;
    }

    public void setEmpleadoSelectedTMP(Empleado empleadoSelectedTMP) {
        this.empleadoSelectedTMP = empleadoSelectedTMP;
    }

    public List<Empleado> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = empleadoFacade.findAll();
        }
        return listaEmpleados;
    }

    public void setListaDepartamentos(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }
    
    public void createOrEdit() {
        try {
            Date date = new Date();
            boolean creando = empleadoSelected.getId() == null;
            if (creando) {
                empleadoSelected.setFechaHoraCrea(date);
                empleadoFacade.create(empleadoSelected);
            } else {
                empleadoSelected.setFechaHoraModifica(date);
                empleadoFacade.edit(empleadoSelected);
            }

            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Empleado " + (creando ? "Creado" : "Actualizado"),
                    "Se ha " + (creando ? "Creado" : "Actualizado") + " correctamente el Empleado: " + empleadoSelected.getNombres());            
            validate();

        } catch (Exception e) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error creando Empleado", "Ocurrió un error creando el Empleado: " + empleadoSelected.getNombres());
            e.printStackTrace(System.err);
        }
    }
    
    public void delete() {
        try {
            empleadoFacade.remove(empleadoSelectedTMP);
            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Empleado borrado", "Se ha eliminado correctamente el Empleado: " + empleadoSelectedTMP.getNombres());
            validate();
        } catch (Exception e) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error borrando el Empleado", "Ocurrio un error eliminado el Empleado: " + empleadoSelectedTMP.getNombres());
            e.printStackTrace(System.err);
        }
    }
    
    public void prepareUpdate() {
        try {
            empleadoSelected = SerializationUtils.clone(empleadoSelectedTMP);
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }
    
    private void validate() {
        //Se inicializan de nuevo los objetos para limpiar el form y actulizar la tabla
        empleadoSelected = null;
        empleadoSelectedTMP = null;
        listaEmpleados = null;
    }
    

    public TiposDocumentos[] getTiposDocumentos() {
        return Utilities.TiposDocumentos.values();
    }

    public List<Departamento> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Departamento> departamentos = departamentoFacade.findAll();
        return departamentos.stream().filter(t -> t.getDepartamentoNombre().toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }
}
