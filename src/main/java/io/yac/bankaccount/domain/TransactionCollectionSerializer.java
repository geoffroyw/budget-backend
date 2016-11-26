package io.yac.bankaccount.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.yac.transaction.domain.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Created by geoffroy on 26/11/2016.
 */
public class TransactionCollectionSerializer extends JsonSerializer<List<Transaction>> {
    @Override public void serialize(List<Transaction> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {

        gen.writeStartArray();
        for (Transaction transaction : value) {
            gen.writeRawValue(transaction.getId().toString());
        }
        gen.writeEndArray();

    }
}
