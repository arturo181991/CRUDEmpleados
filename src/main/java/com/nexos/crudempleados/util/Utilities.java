/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nexos.crudempleados.util;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author artur
 */
public class Utilities {

    public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static List<String> getTiposDocumentos() {
        List<String> documentos = new ArrayList<>();
        documentos.add("RC");
        documentos.add("TI");
        documentos.add("CC");
        documentos.add("CE");
        return documentos;
    }

    public static enum TiposDocumentos {
        DOCUMENTO_REGISTRO_CIVIL("RC", "Registro Civil"),
        DOCUMENTO_TARJETA_IDENTIDAD("TI", "Tarjeta de Identidad"),
        DOCUMENTO_CEDULA_DE_CIUDADANIA("CC", "Cédula de Ciudadanía"),
        DOCUMENTO_CEDULA_DE_EXTRANJERIA("CE", "Cédula de Extranjeria");

        private String valor;
        private String detalle;

        private TiposDocumentos(String valor, String detalle) {
            this.valor = valor;
            this.detalle = detalle;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public String getDetalle() {
            return detalle;
        }

        public void setDetalle(String detalle) {
            this.detalle = detalle;
        }

        @Override
        public String toString() {
            return detalle;
        }
    }
}
