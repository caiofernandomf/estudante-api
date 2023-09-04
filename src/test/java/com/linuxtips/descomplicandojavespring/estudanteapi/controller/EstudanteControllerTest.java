package com.linuxtips.descomplicandojavespring.estudanteapi.controller;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.repository.EstudanteRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.linuxtips.descomplicandojavespring.estudanteapi.data.MockEstudante.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class EstudanteControllerTest {

    private RequestSpecification documentationSpec;

    static {
        RestAssured.baseURI = "HTTP://localhost:8080/v1";
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        estudanteRepository.deleteAll();
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation)).build();
    }

    @Autowired
    EstudanteRepository estudanteRepository;

    public Response criaEstudante(Estudante estudante) throws Exception {
        RequestSpecification requestSpecification =
                given(documentationSpec).accept("application/json")
                        .filter(document("Estudante", PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id").description("Id do estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("nome").description("Nome do estudante"),
                                PayloadDocumentation.fieldWithPath("endereco").description("Endereço do estudante"),
                                PayloadDocumentation.fieldWithPath("curso").description("Curso do estudante"),
                                PayloadDocumentation.fieldWithPath("criadoEm").description("Auditoria - momento de criação do registro de estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("atualizadoEm").description("Auditoria - momento de alteração do registro de estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.id").description("Id dos dados bancários estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.agencia").description("Agência do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.conta").description("Nª da Conta do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.digito").description("Nª da Conta do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.tipoContaBancaria").description("tipo de conta bancaria do estudante")

                        )))
                        .contentType("application/json")
                        .body(estudante);
        return requestSpecification.post("/estudantes");
    }

    private ResponseSpecification responseSpecification(int responseStatus) {
        return new ResponseSpecBuilder()
                .expectStatusCode(responseStatus)
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar um estudante com sucesso")
    public void deveCriarEstudanteComSucesso() {
        try {
            var estudante = dadoParaCriarNovoEstudante();
            criaEstudante(estudante)
                    .then()
                    .assertThat().spec(responseSpecification(201))
                    .and()
                    .assertThat()
                    .body("nome", equalTo("Caio Fernando"));
        } catch (Exception e) {
            fail("Não foi possível cadastrar um estudante", e);
        }
    }

    @Test
    @DisplayName("Deve retornar um estudante pelo id com sucesso")
    public void deveRetornarEstudanteComSucesso() {
        var estudanteInserido =
                estudanteRepository.save(dadoParaBuscarEstudante());

        var estudante =
                given(documentationSpec)
                        .filter(document("Get one Estudante",
                                RequestDocumentation.pathParameters(
                                        RequestDocumentation.parameterWithName("id").description("Id do estudante")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id do estudante"),
                                        PayloadDocumentation.fieldWithPath("nome").description("Nome do estudante"),
                                        PayloadDocumentation.fieldWithPath("endereco").description("Endereço do estudante"),
                                        PayloadDocumentation.fieldWithPath("curso").description("Curso do estudante"),
                                        PayloadDocumentation.fieldWithPath("criadoEm").description("Auditoria - momento de criação do registro de estudante"),
                                        PayloadDocumentation.fieldWithPath("atualizadoEm").description("Auditoria - momento de alteração do registro de estudante"),
                                        PayloadDocumentation.fieldWithPath("dadosBancarios.id").description("Id dos dados bancários estudante"),
                                        PayloadDocumentation.fieldWithPath("dadosBancarios.agencia").description("Agência do estudante"),
                                        PayloadDocumentation.fieldWithPath("dadosBancarios.conta").description("Nª da Conta do estudante"),
                                        PayloadDocumentation.fieldWithPath("dadosBancarios.digito").description("Nª da Conta do estudante"),
                                        PayloadDocumentation.fieldWithPath("dadosBancarios.tipoContaBancaria").description("tipo de conta bancaria do estudante")

                                )))
                        .when()
                        .basePath("/estudantes")
                        .get("/{id}", estudanteInserido.getId())
                        .then()
                        .assertThat()
                        .spec(responseSpecification(200))
                        .assertThat()
                        .body("id", equalTo(estudanteInserido.getId().intValue()))
                        .body("nome", equalTo("Fernando"))
                        .body("curso", equalTo("Descomplicando o Rust"))
                        .extract().as(Estudante.class);

        assertEquals(estudante, estudanteInserido);
    }

    @Test
    @DisplayName("Deve excluir um estudante com sucesso")
    public void deveExcluirEstudanteComSucesso() {
        try {

            var estudante = estudanteRepository.save(dadoParaBuscarEstudante());

            estudanteRepository.findAll().forEach(System.out::println);

            given(documentationSpec)
                    .filter(document("Excluir Estudante",
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("id").description("Id do estudante")
                            )))
                    .when()
                    .basePath("/estudantes")
                    .delete("/{id}", estudante.getId())
                    .then()
                    .assertThat()
                    .spec(responseSpecification(204));
        } catch (Exception e) {
            fail("Não foi possível excluir um estudante");
        }
    }

    @Test
    @DisplayName("Deve atualizar um estudante com sucesso")
    public void deveAtualizarUmEstudanteComSucesso() throws Exception {
        var estudante = estudanteRepository.save(dadoParaBuscarEstudante());

        estudante.setNome("Everton");
        estudante.setCurso("Linux Admin");

        Estudante estudanteAtualizado = given(documentationSpec)
                .contentType("application/json")
                .filter(document("Estudante",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").description("Id do estudante")
                        ),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id").description("Id do estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("nome").description("Nome do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("endereco").description("Endereço do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("curso").description("Curso do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("criadoEm").description("Auditoria - momento de criação do registro de estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("atualizadoEm").description("Auditoria - momento de alteração do registro de estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.id").description("Id dos dados bancários estudante").ignored(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.agencia").description("Agência do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.conta").description("Nª da Conta do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.digito").description("Nª da Conta do estudante").optional(),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.tipoContaBancaria").description("tipo de conta bancaria do estudante").optional()

                        )
                        ,
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("Id do estudante"),
                                PayloadDocumentation.fieldWithPath("nome").description("Nome do estudante"),
                                PayloadDocumentation.fieldWithPath("endereco").description("Endereço do estudante"),
                                PayloadDocumentation.fieldWithPath("curso").description("Curso do estudante"),
                                PayloadDocumentation.fieldWithPath("criadoEm").description("Auditoria - momento de criação do registro de estudante"),
                                PayloadDocumentation.fieldWithPath("atualizadoEm").description("Auditoria - momento de alteração do registro de estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.id").description("Id dos dados bancários estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.agencia").description("Agência do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.conta").description("Nª da Conta do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.digito").description("Nª da Conta do estudante"),
                                PayloadDocumentation.fieldWithPath("dadosBancarios.tipoContaBancaria").description("tipo de conta bancaria do estudante")

                        )))
                .body(estudante)
                .when()
                .basePath("/estudantes")
                .put("/{id}", estudante.getId())
                .then()
                .assertThat()
                .spec(responseSpecification(200))
                .and()
                .extract()
                .as(Estudante.class);

        assertEquals(estudante.getNome(), estudanteAtualizado.getNome());
        assertEquals(estudante.getCurso(), estudanteAtualizado.getCurso());
    }

    @Test
    @DisplayName("Deve retornar uma lista de estudantes sucesso")
    public void deveRetornarListaDeProdutosComSucesso() {
        estudanteRepository.saveAll(mockData());
        List<Estudante> listaEstudantes =
                given(documentationSpec)
                        .contentType("application/json")
                        .filter(document("Lista de Estudantes",
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("[].id").description("Id do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].nome").description("Nome do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].endereco").description("Endereço do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].curso").description("Curso do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].criadoEm").description("Auditoria - momento de criação do registro de estudante"),
                                        PayloadDocumentation.fieldWithPath("[].atualizadoEm").description("Auditoria - momento de alteração do registro de estudante"),
                                        PayloadDocumentation.fieldWithPath("[].dadosBancarios.id").description("Id dos dados bancários estudante"),
                                        PayloadDocumentation.fieldWithPath("[].dadosBancarios.agencia").description("Agência do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].dadosBancarios.conta").description("Nª da Conta do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].dadosBancarios.digito").description("Nª da Conta do estudante"),
                                        PayloadDocumentation.fieldWithPath("[].dadosBancarios.tipoContaBancaria").description("tipo de conta bancaria do estudante")

                                )
                                ))
                        .when()
                        .basePath("/estudantes")
                        .contentType("application/json")
                        .get("")
                        .then()
                        .assertThat()
                        .spec(responseSpecification(200))
                        .and().extract().jsonPath().getList(".", Estudante.class);


        assertTrue(listaEstudantes.size() > 0);
        listaEstudantes.forEach(System.out::println);
    }
}
