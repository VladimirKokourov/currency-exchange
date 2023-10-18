package ru.vkokourov.service;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.ExchangeRate;
import ru.vkokourov.repository.ExchangeRateRepository;

import javax.servlet.ServletException;
import java.util.List;

@Slf4j
public class ExchangeRateService {

    private final ExchangeRateRepository repository;

    public ExchangeRateService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    public List<ExchangeRate> getAll() throws ServletException {
        log.info("Get all exchange rates from service");
        return repository.getAll();
    }
}
