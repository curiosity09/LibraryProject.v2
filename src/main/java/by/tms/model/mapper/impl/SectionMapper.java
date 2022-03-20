package by.tms.mapper.impl;

import by.tms.dto.SectionDto;
import by.tms.entity.Section;
import by.tms.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionMapper implements Mapper<Section, SectionDto> {

    private static final SectionMapper INSTANCE = new SectionMapper();

    public static SectionMapper getInstance() {
        return INSTANCE;
    }

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
