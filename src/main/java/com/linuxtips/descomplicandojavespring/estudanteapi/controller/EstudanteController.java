package com.linuxtips.descomplicandojavespring.estudanteapi.controller;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.service.EstudanteService;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import jakarta.persistence.EntityManagerFactory;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class EstudanteController {
    private final EstudanteService estudanteService;

    private final Counter novosEstudantesCounter;
    private final Counter estudantesDesmatriculadosCounter;

   // private final Gauge numeroConexoesAbertas;

    private final Timer timer;
    private final EntityManagerFactory entityManagerFactory;

    private final DataSource dataSource;
    public EstudanteController(
            MeterRegistry meterRegistry
            , EstudanteService estudanteService
            , EntityManagerFactory entityManagerFactory
            , DataSource dataSource){

        this.dataSource=dataSource;
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

        this.estudanteService=estudanteService;
        this.entityManagerFactory=entityManagerFactory;
        this.novosEstudantesCounter= Counter.builder("novos_estudantes_counter")
                .description("Estudantes matriculados").register(meterRegistry);
        this.estudantesDesmatriculadosCounter= Counter.builder("estudantes_desmatriculados_counter")
                .description("Estudantes desmatriculados").register(meterRegistry);
        new JvmMemoryMetrics(Tags.of("mem_usage","mem_usage")).bindTo(meterRegistry);
        new FileDescriptorMetrics(Tags.of("file_descriptors","file_descriptors")).bindTo(meterRegistry);
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        timer=Timer.builder("my.timer.listar.estudantes")
                .publishPercentiles(0.5, 0.95) // median and 95th percentile (1)
                .publishPercentileHistogram() // (2)
                .serviceLevelObjectives(Duration.ofMillis(100)) // (3)
                .minimumExpectedValue(Duration.ofMillis(1)) // (4)
                .maximumExpectedValue(Duration.ofSeconds(10)).register(meterRegistry);

        /*this.numeroConexoesAbertas =
                Gauge.builder("numero_threads_esperando_banco",hikariDataSource.getHikariPoolMXBean(),HikariPoolMXBean::getThreadsAwaitingConnection )
                        .description("Threads esperando conexão com o banco de dados")
                        .register(meterRegistry);*/



    }


    @SneakyThrows(Exception.class)
    @PostMapping("/estudantes")
    @ResponseStatus(HttpStatus.CREATED)
    public Estudante criarEstudante(@RequestBody Estudante estudante) {
        novosEstudantesCounter.increment();
        return estudanteService.criarEstudante(estudante);
    }

    @GetMapping("/estudantes")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarEstudantes(){
        return
                timer.record(estudanteService::listarEstudantes);

    }
    @SneakyThrows
    @GetMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> listarPorId(@PathVariable long id){
        return estudanteService.buscarEstudantePorId(id);
    }

    @SneakyThrows
    @PutMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> atuallizarPorId(@PathVariable long id, @RequestBody Estudante estudante){
        return estudanteService.atualizarEstudantePorId(estudante,id);
    }

    @SneakyThrows
    @DeleteMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> excluirPorId(@PathVariable long id){
        estudantesDesmatriculadosCounter.increment();
        return estudanteService.excluirPorId(id);
    }

    @SneakyThrows
    @GetMapping("/estudantes/nome/{nome}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> buscarPorNome(@PathVariable String nome){
        return estudanteService.buscarEstudantePorNome(nome);
    }

    @GetMapping("/estudantes/curso")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> buscarPorCurso(@RequestParam(required = true) String curso){
        return estudanteService.listarEstudantesPorCurso(curso);
    }

    @GetMapping("/estudantes/nome")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarPorNome(@RequestParam(required = true) String nome){
        return estudanteService.listarEstudantesPorNome(nome);
    }

    @GetMapping("/estudantes/promo")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarPrimeiros(@RequestParam(required = true) int numero){
        return estudanteService.listarEstudantesPrimeirosEstudantes(numero);
    }
}
