package com.mhj.anime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mhj.anime.entity.AnimeCategory;

import java.util.List;

public interface AnimeCategoryService extends IService<AnimeCategory> {

    List<AnimeCategory> listCategory();

    AnimeCategory createCategory(AnimeCategory category);

    AnimeCategory updateCategory(Long id, AnimeCategory category);

    void deleteCategory(Long id);
}
