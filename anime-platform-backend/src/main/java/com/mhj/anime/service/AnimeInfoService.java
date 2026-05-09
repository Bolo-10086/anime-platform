package com.mhj.anime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mhj.anime.entity.AnimeInfo;

import java.util.List;

public interface AnimeInfoService extends IService<AnimeInfo> {

    List<AnimeInfo> listAnime(String keyword, Long categoryId, Long tagId, Integer releaseYear, String status, String region, String type);

    AnimeInfo detail(Long id);

    AnimeInfo createAnime(AnimeInfo animeInfo);

    AnimeInfo updateAnime(Long id, AnimeInfo animeInfo);

    void deleteAnime(Long id);
}
