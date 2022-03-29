package by.tms.model.service.impl;

import by.tms.model.dao.SectionDao;
import by.tms.model.dto.SectionDto;
import by.tms.model.entity.Section;
import by.tms.model.mapper.impl.SectionMapper;
import by.tms.model.service.SectionService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class SectionServiceImpl implements SectionService {

    private final SectionDao sectionDao;
    private final SectionMapper sectionMapper = SectionMapper.getInstance();

    @Override
    public List<SectionDto> findAllSection(int limit, int offset) {
        List<Section> sections = sectionDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, sections);
        return sectionMapper.mapToListDto(sections);
    }

    @Override
    @Transactional
    public Long addNewSection(SectionDto sectionDto) {
        Long saveSection = sectionDao.save(sectionMapper.mapToEntity(sectionDto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, saveSection);
        return saveSection;
    }

    @Override
    public Optional<SectionDto> findSectionByName(String sectionName) {
        Optional<Section> optionalSection = sectionDao.findByName(sectionName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalSection, sectionName);
        return Optional.ofNullable(sectionMapper.mapToDto(optionalSection.orElse(null)));
    }

    @Override
    @Transactional
    public void updateSection(SectionDto sectionDto) {
        sectionDao.update(sectionMapper.mapToEntity(sectionDto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, sectionDto);
    }

    @Override
    @Transactional
    public void deleteSection(SectionDto sectionDto) {
        sectionDao.delete(sectionMapper.mapToEntity(sectionDto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, sectionDto);
    }

    @Override
    public boolean isSectionExist(Long id) {
        boolean exist = sectionDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public Optional<SectionDto> findSectionById(Long id) {
        Optional<Section> optionalSection = sectionDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalSection, id);
        return Optional.ofNullable(sectionMapper.mapToDto(optionalSection.orElse(null)));
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = sectionDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
