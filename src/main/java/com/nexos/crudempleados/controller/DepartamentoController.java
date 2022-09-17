/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nexos.crudempleados.controller;

import com.nexos.crudempleados.ejb.DepartamentoFacade;
import com.nexos.crudempleados.model.Departamento;
import com.nexos.crudempleados.util.Utilities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author artur
 */
@Named
@ViewScoped
public class DepartamentoController implements Serializable {

    @EJB
    private DepartamentoFacade departamentoFacade;
    private Departamento departamentoSelected, departamentoSelectedTMP;
    private List<Departamento> listaDepartamentos;

    public Departamento getDepartamentoSelected() {
        if (departamentoSelected == null) {
            departamentoSelected = new Departamento();
        }
        return departamentoSelected;
    }

    public void setDepartamentoSelected(Departamento departamento) {
        this.departamentoSelected = departamento;
    }

    public Departamento getDepartamentoSelectedTMP() {
        return departamentoSelectedTMP;
    }

    public void setDepartamentoSelectedTMP(Departamento departamentoSelectedTMP) {
        this.departamentoSelectedTMP = departamentoSelectedTMP;
    }

    public List<Departamento> getListaDepartamentos() {
        if (listaDepartamentos == null) {
            listaDepartamentos = departamentoFacade.findAll();
        }
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public void createOrEdit() {
        try {
            Date date = new Date();
            boolean creando = departamentoSelected.getId() == null;
            if (creando) {
                departamentoSelected.setFechaHoraCrea(date);
                departamentoFacade.create(departamentoSelected);
            } else {
                departamentoSelected.setFechaHoraModifica(date);
                departamentoFacade.edit(departamentoSelected);
            }

            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Departamento " + (creando ? "Creado" : "Actualizado"),
                    "Se ha " + (creando ? "Creado" : "Actualizado") + " correctamente el Departamento de: " + departamentoSelected.getDepartamentoNombre());            
            validate();

        } catch (Exception e) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error creando Departamento", "Ocurrió un error creando el Departamento: " + departamentoSelected.getDepartamentoNombre());
            e.printStackTrace(System.err);
        }
    }

    public void delete() {
        try {
            departamentoFacade.remove(departamentoSelectedTMP);
            Utilities.addMessage(FacesMessage.SEVERITY_INFO, "Departamento borrado", "Se ha eliminado correctamente el Departamento: " + departamentoSelectedTMP.getDepartamentoNombre());
            validate();
        } catch (Exception e) {
            Utilities.addMessage(FacesMessage.SEVERITY_ERROR, "Error borrando el Departamento", "Ocurrio un error eliminado el Departamento: " + departamentoSelectedTMP.getDepartamentoNombre());
            e.printStackTrace(System.err);
        }
    }

    public void prepareUpdate() {
        try {
            departamentoSelected = SerializationUtils.clone(departamentoSelectedTMP);
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private void validate() {
        //Se inicializan de nuevo los objetos para limpiar el form y actulizar la tabla
        departamentoSelected = null;
        departamentoSelectedTMP = null;
        listaDepartamentos = null;
    }
    
    @FacesConverter(forClass = Departamento.class, value = "departamentoConverter")
    public static class DepartamentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartamentoController controller = (DepartamentoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departamentoController");
            return controller.getDepartamento(getKey(value));
        }

        Integer getKey(String value) {
            Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Departamento) {
                Departamento o = (Departamento) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Departamento.class.getName()});
                return null;
            }
        }

    }
    
    public Departamento getDepartamento(java.lang.Integer id) {
        return departamentoFacade.find(id);
    }
}
