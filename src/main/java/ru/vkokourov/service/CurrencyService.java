package ru.vkokourov.service;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.*;
import ru.vkokourov.model.Currency;
import ru.vkokourov.repository.CurrencyRepository;
import ru.vkokourov.util.ValidationUtil;

import javax.servlet.ServletException;
import java.util.List;

@Slf4j
public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public List<Currency> getAll() throws ServletException {
        log.info("Get all currencies from service");
        return repository.getAll();
    }

    public Currency getByCode(String code) throws ServletException {
        log.info("Get currency {} from service", code);
        if (code.isEmpty()) {
            log.error("The currency code is missing from the address");
            throw new InvalidDataException("Код валюты отсутствует в адресе");
        }
        if (!ValidationUtil.isCodeValid(code)) {
            log.error("Incorrect code input {}", code);
            throw new InvalidDataException("Некорректный ввод кода валюты");
        }
        Currency currency = repository.getByCode(code);
        if (currency.getId() == 0) {
            throw new NotExistException("Валюта не найдена");
        }
        return currency;
    }

    public Currency create(Currency currency) throws ApplicationException {
        log.info("Create currency from service. {}", currency);
        return repository.create(currency);
    }

    public void update(Currency currency) throws ApplicationException {
        log.info("Update currency from service. {}", currency);
        repository.update(currency);
    }
}
