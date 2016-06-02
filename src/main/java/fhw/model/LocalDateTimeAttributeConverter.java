package fhw.model;

import java.sql.Timestamp;
import java.time.*;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocalDateTimeAttributeConverter
        implements AttributeConverter<LocalDateTime, Timestamp>
{

    @Override
    public java.sql.Timestamp convertToDatabaseColumn(java.time.LocalDateTime attribute)
    {
        return attribute == null ? null : java.sql.Timestamp.valueOf(attribute);
    }

    @Override
    public java.time.LocalDateTime convertToEntityAttribute(java.sql.Timestamp dbData)
    {
        return dbData == null ? null : dbData.toLocalDateTime();
    }
}
