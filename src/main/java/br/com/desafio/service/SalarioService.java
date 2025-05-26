package br.com.desafio.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SalarioService {

    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public void calcularSalarios() {
        em.createNativeQuery("DELETE FROM pessoa_salario_consolidado")
                .executeUpdate();

        em.createNativeQuery(
                "INSERT INTO pessoa_salario_consolidado (pessoa_id, nome_pessoa, nome_cargo, salario) \n" +
                        "                        SELECT p.id AS pessoa_id, p.nome AS nome_pessoa, c.nome AS nome_cargo, \n" +
                        "                        SUM(CASE WHEN v.tipo = 'CREDITO' THEN v.valor WHEN v.tipo = 'DEBITO' THEN -v.valor ELSE 0 END) AS salario \n" +
                        "                        FROM pessoa p \n" +
                        "                        JOIN cargo_vencimentos cv ON p.cargo_id = cv.cargo_id \n" +
                        "                        JOIN cargo c ON cv.cargo_id = c.id \n" +
                        "                        JOIN vencimentos v ON cv.vencimento_id = v.id \n" +
                        "                        GROUP BY p.id, p.nome, c.nome"
        ).executeUpdate();
    }
}
