package by.tms.model.service.impl;

import by.tms.model.dao.SectionDao;
import by.tms.model.dto.SectionDto;
import by.tms.model.entity.Section;
import by.tms.model.mapper.impl.SectionMapper;
import by.tms.model.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class SectionServiceImpl implements SectionService {

    private final SectionDao sectionDao;
    private final SectionMapper sectionMapper = SectionMapper.getInstance();

    @Override
    public List<SectionDto> findAllSection() {
        List<Section> sections = sectionDao.findAll();
        return sectionMapper.mapToListDto(sections);
    }

    @Override
    @Transactional
    public Long addNewSection(SectionDto sectionDto) {
        return sectionDao.save(sectionMapper.mapToEntity(sectionDto));
    }

    @Override
    public Optional<SectionDto> findSectionByName(String sectionName) {
        Optional<Section> optionalSection = sectionDao.findByName(sectionName);
        return Optional.ofNullable(sectionMapper.mapToDto(optionalSection.orElse(null)));
    }

    @Override
    @Transactional
    public void updateSection(SectionDto sectionDto) {
        sectionDao.update(sectionMapper.mapToEntity(sectionDto));
    }

    @Override
    @Transactional
    public void deleteSection(SectionDto sectionDto) {
        sectionDao.delete(sectionMapper.mapToEntity(sectionDto));
    }

    @Override
    public boolean isSectionExist(Long id) {
        return sectionDao.isExist(id);
    }

    @Override
    public Optional<SectionDto> findSectionById(Long id) {
        Optional<Section> optionalSection = sectionDao.findById(id);
        return Optional.ofNullable(sectionMapper.mapToDto(optionalSection.orElse(null)));
    }
}
