package com.mhj.anime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mhj.anime.entity.AnimeTag;

import java.util.List;

public interface AnimeTagService extends IService<AnimeTag> {

    List<AnimeTag> listTag();

    AnimeTag createTag(AnimeTag tag);

    AnimeTag updateTag(Long id, AnimeTag tag);

    void deleteTag(Long id);
}
