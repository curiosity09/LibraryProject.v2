package by.tms.model.service.impl;

import by.tms.model.dao.SectionDao;
import by.tms.model.dto.SectionDto;
import by.tms.model.entity.Section;
import by.tms.model.service.SectionService;
import by.tms.model.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SectionServiceImpl extends GenericServiceImpl<SectionDto, Long, Section> implements SectionService {

    private final SectionDao sectionDao;

    @Override
    public Optional<SectionDto> findSectionByName(String sectionName) {
        Optional<Section> optionalSection = sectionDao.findByName(sectionName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalSection, sectionName);
        return Optional.ofNullable(mapper.mapToDto(optionalSection.orElse(null)));
    }
}
