package by.tms.service;

import by.tms.dto.SectionDto;
import by.tms.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    List<SectionDto> findAllSection();

    Long addNewSection(Section section);

    Optional<SectionDto> findSectionByName(String sectionName);

    void updateSection(Section section);

    void deleteSection(Section section);

    boolean isSectionExist(Long id);

    Optional<SectionDto> findSectionById(Long id);
}
