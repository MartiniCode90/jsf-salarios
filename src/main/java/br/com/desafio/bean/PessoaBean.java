package br.com.desafio.bean;

import br.com.desafio.model.PessoaSalarioConsolidado;
import br.com.desafio.service.SalarioService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("pessoaBean")
@ViewScoped
public class PessoaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private SalarioService salarioService;

    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    private List<PessoaSalarioConsolidado> lista;
    private int page;              // página atual, começa em 1
    private int pageSize = 20;     // registros por página

    @PostConstruct
    public void init() {
        recarregar();
    }

    private void recarregar() {
        lista = em.createQuery(
                "FROM PessoaSalarioConsolidado p" +
                        " ORDER BY p.pessoaId",
                PessoaSalarioConsolidado.class
        ).getResultList();
        page = 1;
    }

    public void recalcular() {
        salarioService.calcularSalarios();
        recarregar();
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                        "Salários recalculados com sucesso"));
    }

    public List<PessoaSalarioConsolidado> getPagina() {
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }
        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, lista.size());
        return lista.subList(from, to);
    }

    public void nextPage() {
        if (page < getTotalPages()) {
            page++;
        }
    }

    public void prevPage() {
        if (page > 1) {
            page--;
        }
    }

    public void goToFirstPage() {
        page = 1;
    }

    public int getCurrentPage() {
        return page;
    }

    public int getTotalPages() {
        return (lista == null || lista.isEmpty())
                ? 1
                : (int) Math.ceil(lista.size() / (double) pageSize);
    }

    public int getTotalRecords() {
        return lista == null ? 0 : lista.size();
    }

    public int getFromRecord() {
        return lista == null || lista.isEmpty()
                ? 0
                : (page - 1) * pageSize + 1;
    }

    public int getToRecord() {
        return lista == null || lista.isEmpty()
                ? 0
                : Math.min(page * pageSize, lista.size());
    }
}
