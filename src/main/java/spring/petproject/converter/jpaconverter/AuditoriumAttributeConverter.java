package spring.petproject.converter.jpaconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.petproject.domain.Auditorium;
import spring.petproject.service.AuditoriumService;

import javax.persistence.AttributeConverter;

@Component
public class AuditoriumAttributeConverter implements AttributeConverter<Auditorium, String>{

    private static AuditoriumService auditoriumService;

    @Override
    public String convertToDatabaseColumn(Auditorium attribute) {
        return attribute.getName();
    }

    @Override
    public Auditorium convertToEntityAttribute(String dbData) {
        return auditoriumService.getByName(dbData);
    }

    @Autowired //some kind of hack
    public void setAuditoriumService(AuditoriumService auditoriumService) {
        AuditoriumAttributeConverter.auditoriumService = auditoriumService;
    }
}
