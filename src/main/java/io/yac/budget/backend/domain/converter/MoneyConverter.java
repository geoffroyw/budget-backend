package io.yac.budget.backend.domain.converter;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

/**
 * Created by geoffroy on 23/01/2016.
 */
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return Money.from(attribute).toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        return Money.parse(dbData);
    }
}
