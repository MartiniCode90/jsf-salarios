# JSF Salários Consolidados

Este projeto é uma aplicação web Java EE (JSF + EJB + JPA + Hibernate) que calcula e consolida os salários de pessoas com base em seus cargos e vencimentos, exibindo-os em páginas paginadas e permitindo exportação de relatórios.

## Tecnologias utilizadas

* Java 8
* Maven 3.x
* WildFly 22 (Java EE 8)
* JSF 2.3 + Facelets
* CDI / EJB (Stateless session bean)
* JPA 2.1 (Hibernate 5.x)
* PostgreSQL 13+
* JasperReports (para geração de PDF)

## Pré-requisitos

1. **Java JDK 1.8** instalado e configurado no `PATH`.
2. **Maven** instalado e configurado no `PATH`.
3. **WildFly 22** instalado.
4. **PostgreSQL** em execução, com:

   * Banco de dados `salarioDB`
   * Usuário `postgres` (ou outro, conforme ajuste)
   * Senha configurada (padrão no projeto: `082125`)

## Estrutura do projeto

```
jsf-salarios/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/           # código-fonte Java
│  │  ├─ resources/
│  │  └─ webapp/
│  │     ├─ pages/       # arquivos .xhtml
│  │     └─ WEB-INF/
│  │         ├─ web.xml
│  │         └─ faces-config.xml
└─ README.md            # este arquivo
```

## Configuração do banco de dados

1. Crie o banco `salarioDB` no PostgreSQL.
2. Importe o **schema** e dados iniciais (ex.: tabelas `pessoa`, `cargo`, `vencimentos`, `cargo_vencimentos`).
3. Verifique o acesso via `psql` ou pgAdmin:

   ```bash
   psql -h localhost -p 5432 -U postgres -d salarioDB
   ```
4. Ajuste usuário e senha, se necessário.

## Configuração do WildFly

1. Copie o driver PostgreSQL (`postgresql-<versão>.jar`) para `WILDFLY_HOME/modules/system/layers/base/org/postgresql/main/`.
2. Crie o módulo PostgreSQL:

   * `module.xml` em `.../org/postgresql/main`:

     ```xml
     <module xmlns="urn:jboss:module:1.5" name="org.postgresql">
       <resources>
         <resource-root path="postgresql-42.6.0.jar"/>
       </resources>
       <dependencies>
         <module name="javax.api"/>
         <module name="javax.transaction.api"/>
       </dependencies>
     </module>
     ```
3. Inicie o WildFly (`WILDFLY_HOME/bin/standalone.sh` ou `.bat`).
4. Conecte-se ao CLI:

   ```bash
   cd WILDFLY_HOME/bin
   ./jboss-cli.sh --connect
   ```
5. Remova qualquer DataSource anterior e adicione o `PostgresDS`:

   ```cli
   /subsystem=datasources/data-source=PostgresDS:remove
   data-source add \
     --name=PostgresDS \
     --jndi-name=java:jboss/datasources/PostgresDS \
     --driver-name=postgresql \
     --connection-url=jdbc:postgresql://localhost:5432/salarioDB \
     --user-name=postgres \
     --password=082125 \
     --use-ccm=false
   /subsystem=datasources/data-source=PostgresDS:test-connection-in-pool
   ```
6. No `standalone.xml`, certifique-se de ter:

   ```xml
   <datasource jndi-name="java:jboss/datasources/PostgresDS" pool-name="PostgresDS">
     <connection-url>jdbc:postgresql://localhost:5432/salarioDB</connection-url>
     <driver>postgresql</driver>
     <security>
       <user-name>postgres</user-name>
       <password>082125</password>
     </security>
   </datasource>
   ```

## Configuração de JPA

No `src/main/webapp/WEB-INF/classes/META-INF/persistence.xml`:

```xml
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">
  <persistence-unit name="PU" transaction-type="JTA">
    <jta-data-source>java:jboss/datasources/PostgresDS</jta-data-source>
    <class>br.com.desafio.model.PessoaSalarioConsolidado</class>
    <properties>
      <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
      <property name="hibernate.hbm2ddl.auto" value="validate"/>
      <property name="hibernate.show_sql" value="false"/>
    </properties>
  </persistence-unit>
</persistence>
```

> **Importante:** usar `transaction-type="JTA"` com `<jta-data-source>` quando for deployar no WildFly.

## Build e Deploy

1. No diretório raiz do projeto, execute:

   ```bash
   mvn clean package
   ```
2. No WildFly Management Console ([http://localhost:9990](http://localhost:9990)) ou via IDE, adicione o artefato WAR (`target/jsf-salarios-1.0-SNAPSHOT.war`) e faça o deploy.
3. Acesse a aplicação em:

   ```
   http://localhost:8080/jsf-salarios-1.0-SNAPSHOT/pages/pessoas.xhtml
   ```

## Relatórios PDF

* As configurações de JasperReports estão no pacote `br.com.desafio.report`.
* Use o botão **Exportar PDF** na tela para gerar o relatório das 20 pessoas exibidas.

## Tela Principal:
![image](https://github.com/user-attachments/assets/565731a3-863a-4ea8-9a93-3f6e2c822cbc)

## Relatório PDF:

![image](https://github.com/user-attachments/assets/b48e737c-7d96-4ed2-8a15-2e5ca59b384b)


