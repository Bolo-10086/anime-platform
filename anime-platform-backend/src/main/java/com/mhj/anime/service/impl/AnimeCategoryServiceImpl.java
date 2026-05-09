package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.AnimeCategory;
import com.mhj.anime.mapper.AnimeCategoryMapper;
import com.mhj.anime.service.AnimeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AnimeCategoryServiceImpl extends ServiceImpl<AnimeCategoryMapper, AnimeCategory> implements AnimeCategoryService {

    @Override
    public List<AnimeCategory> listCategory() {
        return list(new LambdaQueryWrapper<AnimeCategory>()
                .orderByAsc(AnimeCategory::getSortOrder)
                .orderByAsc(AnimeCategory::getId));
    }

    @Override
    public AnimeCategory createCategory(AnimeCategory category) {
        validate(category);
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        save(category);
        return category;
    }

    @Override
    public AnimeCategory updateCategory(Long id, AnimeCategory category) {
        if (id == null || getById(id) == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        validate(category);
        category.setId(id);
        updateById(category);
        return getById(id);
    }

    @Override
    public void deleteCategory(Long id) {
        if (id == null || !removeById(id)) {
            throw new IllegalArgumentException("分类不存在");
        }
    }

    private void validate(AnimeCategory category) {
        if (category == null || !StringUtils.hasText(category.getName())) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
    }
}
