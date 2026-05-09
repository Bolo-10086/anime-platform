package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.AnimeTag;
import com.mhj.anime.mapper.AnimeTagMapper;
import com.mhj.anime.service.AnimeTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AnimeTagServiceImpl extends ServiceImpl<AnimeTagMapper, AnimeTag> implements AnimeTagService {

    @Override
    public List<AnimeTag> listTag() {
        return list(new LambdaQueryWrapper<AnimeTag>()
                .orderByAsc(AnimeTag::getSortOrder)
                .orderByAsc(AnimeTag::getId));
    }

    @Override
    public AnimeTag createTag(AnimeTag tag) {
        validate(tag);
        if (tag.getSortOrder() == null) {
            tag.setSortOrder(0);
        }
        save(tag);
        return tag;
    }

    @Override
    public AnimeTag updateTag(Long id, AnimeTag tag) {
        if (id == null || getById(id) == null) {
            throw new IllegalArgumentException("标签不存在");
        }
        validate(tag);
        tag.setId(id);
        updateById(tag);
        return getById(id);
    }

    @Override
    public void deleteTag(Long id) {
        if (id == null || !removeById(id)) {
            throw new IllegalArgumentException("标签不存在");
        }
    }

    private void validate(AnimeTag tag) {
        if (tag == null || !StringUtils.hasText(tag.getName())) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
    }
}
