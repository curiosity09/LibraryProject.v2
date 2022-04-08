package by.tms.model.mapper.impl;

import by.tms.model.dto.SectionDto;
import by.tms.model.entity.Section;
import by.tms.model.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class SectionMapper implements Mapper<Section, SectionDto> {

    @Override
    public SectionDto mapToDto(Section section) {
        if (Objects.nonNull(section)) {
            return SectionDto.builder()
                    .id(section.getId())
                    .name(section.getName())
                    .build();
        }
        return null;
    }

    @Override
    public List<SectionDto> mapToListDto(List<Section> sections) {
        if (Objects.nonNull(sections)) {
            List<SectionDto> sectionDtoList = new ArrayList<>();
            for (Section section : sections) {
                sectionDtoList.add(mapToDto(section));
            }
            return sectionDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public Section mapToEntity(SectionDto sectionDto) {
        if (Objects.nonNull(sectionDto)) {
            Section section = Section.builder()
                    .name(sectionDto.getName())
                    .build();
            section.setId(sectionDto.getId());
            return section;
        }
        return null;
    }
}
