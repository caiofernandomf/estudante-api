package com.linuxtips.descomplicandojavespring.estudanteapi.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.function.Supplier;

@Component
@EnableScheduling
public class Monitoramento {

    private final MeterRegistry meterRegistry;

    private final HikariDataSource hikariDataSource;
    private final EntityManagerFactory entityManagerFactory;

    public Monitoramento(MeterRegistry meterRegistry,DataSource dataSource,
                         EntityManagerFactory entityManagerFactory){
        this.meterRegistry=meterRegistry;
        this.hikariDataSource= (HikariDataSource) dataSource;
        this.entityManagerFactory=entityManagerFactory;
    }

    @Bean
    public Gauge getThreadsEsperandoConexaoBanco() {
        return Gauge.builder("numero_threads_esperando_banco", hikariDataSource.getHikariPoolMXBean(), HikariPoolMXBean::getThreadsAwaitingConnection)
                .description("Threads esperando conexão com o banco de dados")
                .register(meterRegistry);
    }

    @Bean
    public Gauge getStatiscs(EntityManagerFactory entityManagerFactory){
        return Gauge.builder("numero_conexoes_ativas", fetchConnectionCount())
                .description("numero de  conexõe com o banco de dados")
                .register(meterRegistry);
    }

    public Supplier<Number> fetchConnectionCount() {
        return ()->hikariDataSource.getHikariPoolMXBean().getActiveConnections();
    }

    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void schedulingTask() {
        System.out.println("Está rodando as tasks");
        numeroInsertsComSucesso();
    }

    public Gauge numeroInsertsComSucesso(){
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        return  Gauge.builder("numero_estudantes_inseridos",()->statistics.getEntityStatistics(
                statistics.getEntityNames()[0]
        ).getInsertCount())
                .description("número de estudantes inseridos")
                .register(meterRegistry);
    }

}

