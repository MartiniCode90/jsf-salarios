package br.com.desafio.bean;

import br.com.desafio.bean.PessoaBean;
import br.com.desafio.model.PessoaSalarioConsolidado;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("reportBean")
@ViewScoped
public class ReportBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaBean pessoaBean;

    public void downloadPdf() {
        try {
            List<PessoaSalarioConsolidado> dados = pessoaBean.getPagina();

            InputStream jrxml = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("reports/salarios.jrxml");
            JasperReport jasper = JasperCompileManager.compileReport(jrxml);

            JRBeanCollectionDataSource ds =
                    new JRBeanCollectionDataSource(dados);

            Map<String, Object> params = new HashMap<>();
            JasperPrint jp = JasperFillManager.fillReport(jasper, params, ds);

            FacesContext fc  = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpServletResponse resp =
                    (HttpServletResponse) ec.getResponse();
            resp.setContentType("application/pdf");
            resp.setHeader(
                    "Content-Disposition",
                    "attachment; filename=salarios_pagina" +
                            pessoaBean.getCurrentPage() + ".pdf"
            );

            JasperExportManager.exportReportToPdfStream(jp, resp.getOutputStream());
            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Falha ao gerar PDF", e.getMessage()));
        }
    }
}
