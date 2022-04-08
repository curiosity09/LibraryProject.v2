package by.tms.model.service;

import by.tms.model.dto.SectionDto;
import by.tms.model.entity.Section;

import java.util.Optional;

public interface SectionService extends GenericService<SectionDto, Long, Section> {

    Optional<SectionDto> findSectionByName(String sectionName);
}
