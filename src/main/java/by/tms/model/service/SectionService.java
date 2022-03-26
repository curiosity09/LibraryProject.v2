package by.tms.model.service;

import by.tms.model.dto.SectionDto;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    List<SectionDto> findAllSection(int limit, int offset);

    Long addNewSection(SectionDto sectionDto);

    Optional<SectionDto> findSectionByName(String sectionName);

    void updateSection(SectionDto sectionDto);

    void deleteSection(SectionDto sectionDto);

    boolean isSectionExist(Long id);

    Optional<SectionDto> findSectionById(Long id);

    List<Long> getCountPages();
}
