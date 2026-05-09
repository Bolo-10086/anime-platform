package com.mhj.anime.service;

import com.mhj.anime.dto.CommentRequest;
import com.mhj.anime.dto.RatingRequest;
import com.mhj.anime.vo.CommentVO;
import com.mhj.anime.vo.UserAnimeStateVO;
import com.mhj.anime.vo.UserCommentVO;
import com.mhj.anime.vo.UserFavoriteVO;
import com.mhj.anime.vo.UserRatingVO;

import java.util.List;

public interface AnimeInteractionService {

    List<CommentVO> listComments(Long animeId);

    CommentVO addComment(Long animeId, Long userId, CommentRequest request);

    UserAnimeStateVO getUserState(Long animeId, Long userId);

    UserAnimeStateVO favorite(Long animeId, Long userId);

    UserAnimeStateVO cancelFavorite(Long animeId, Long userId);

    UserAnimeStateVO rate(Long animeId, Long userId, RatingRequest request);

    List<UserFavoriteVO> listUserFavorites(Long userId);

    List<UserRatingVO> listUserRatings(Long userId);

    List<UserCommentVO> listUserComments(Long userId);
}
