package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mhj.anime.dto.CommentRequest;
import com.mhj.anime.dto.RatingRequest;
import com.mhj.anime.entity.AnimeComment;
import com.mhj.anime.entity.AnimeFavorite;
import com.mhj.anime.entity.AnimeInfo;
import com.mhj.anime.entity.AnimeRating;
import com.mhj.anime.mapper.AnimeCommentMapper;
import com.mhj.anime.mapper.AnimeFavoriteMapper;
import com.mhj.anime.mapper.AnimeRatingMapper;
import com.mhj.anime.service.AnimeInfoService;
import com.mhj.anime.service.AnimeInteractionService;
import com.mhj.anime.vo.CommentVO;
import com.mhj.anime.vo.UserAnimeStateVO;
import com.mhj.anime.vo.UserCommentVO;
import com.mhj.anime.vo.UserFavoriteVO;
import com.mhj.anime.vo.UserRatingVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AnimeInteractionServiceImpl implements AnimeInteractionService {

    private final AnimeInfoService animeInfoService;
    private final AnimeCommentMapper animeCommentMapper;
    private final AnimeFavoriteMapper animeFavoriteMapper;
    private final AnimeRatingMapper animeRatingMapper;

    public AnimeInteractionServiceImpl(AnimeInfoService animeInfoService,
                                       AnimeCommentMapper animeCommentMapper,
                                       AnimeFavoriteMapper animeFavoriteMapper,
                                       AnimeRatingMapper animeRatingMapper) {
        this.animeInfoService = animeInfoService;
        this.animeCommentMapper = animeCommentMapper;
        this.animeFavoriteMapper = animeFavoriteMapper;
        this.animeRatingMapper = animeRatingMapper;
    }

    @Override
    public List<CommentVO> listComments(Long animeId) {
        ensureAnimeExists(animeId);
        return animeCommentMapper.selectCommentList(animeId);
    }

    @Override
    public CommentVO addComment(Long animeId, Long userId, CommentRequest request) {
        ensureAnimeExists(animeId);
        if (request == null || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        String content = request.getContent().trim();
        if (content.length() > 500) {
            throw new IllegalArgumentException("评论内容不能超过500字");
        }
        AnimeComment comment = new AnimeComment();
        comment.setAnimeId(animeId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setStatus(1);
        animeCommentMapper.insert(comment);
        return animeCommentMapper.selectCommentList(animeId).stream()
                .filter(item -> item.getId().equals(comment.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserAnimeStateVO getUserState(Long animeId, Long userId) {
        ensureAnimeExists(animeId);
        UserAnimeStateVO state = new UserAnimeStateVO();
        state.setFavorite(countFavorite(animeId, userId) > 0);
        AnimeRating rating = selectRating(animeId, userId);
        state.setRating(rating == null ? null : rating.getRating());
        return state;
    }

    @Override
    public UserAnimeStateVO favorite(Long animeId, Long userId) {
        ensureAnimeExists(animeId);
        if (countFavorite(animeId, userId) == 0) {
            AnimeFavorite favorite = new AnimeFavorite();
            favorite.setAnimeId(animeId);
            favorite.setUserId(userId);
            animeFavoriteMapper.insert(favorite);
        }
        return getUserState(animeId, userId);
    }

    @Override
    public UserAnimeStateVO cancelFavorite(Long animeId, Long userId) {
        ensureAnimeExists(animeId);
        animeFavoriteMapper.delete(new LambdaQueryWrapper<AnimeFavorite>()
                .eq(AnimeFavorite::getAnimeId, animeId)
                .eq(AnimeFavorite::getUserId, userId));
        return getUserState(animeId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAnimeStateVO rate(Long animeId, Long userId, RatingRequest request) {
        ensureAnimeExists(animeId);
        if (request == null || request.getRating() == null) {
            throw new IllegalArgumentException("评分不能为空");
        }
        BigDecimal ratingValue = request.getRating();
        if (ratingValue.compareTo(BigDecimal.ZERO) < 0 || ratingValue.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("评分范围必须在0到10之间");
        }
        AnimeRating rating = selectRating(animeId, userId);
        if (rating == null) {
            rating = new AnimeRating();
            rating.setAnimeId(animeId);
            rating.setUserId(userId);
            rating.setRating(ratingValue);
            animeRatingMapper.insert(rating);
        } else {
            rating.setRating(ratingValue);
            animeRatingMapper.updateById(rating);
        }
        refreshAnimeScore(animeId);
        return getUserState(animeId, userId);
    }

    @Override
    public List<UserFavoriteVO> listUserFavorites(Long userId) {
        return animeFavoriteMapper.selectUserFavorites(userId);
    }

    @Override
    public List<UserRatingVO> listUserRatings(Long userId) {
        return animeRatingMapper.selectUserRatings(userId);
    }

    @Override
    public List<UserCommentVO> listUserComments(Long userId) {
        return animeCommentMapper.selectUserComments(userId);
    }

    private void ensureAnimeExists(Long animeId) {
        if (animeId == null || animeInfoService.getById(animeId) == null) {
            throw new IllegalArgumentException("动漫不存在");
        }
    }

    private int countFavorite(Long animeId, Long userId) {
        return Math.toIntExact(animeFavoriteMapper.selectCount(new LambdaQueryWrapper<AnimeFavorite>()
                .eq(AnimeFavorite::getAnimeId, animeId)
                .eq(AnimeFavorite::getUserId, userId)));
    }

    private AnimeRating selectRating(Long animeId, Long userId) {
        return animeRatingMapper.selectOne(new LambdaQueryWrapper<AnimeRating>()
                .eq(AnimeRating::getAnimeId, animeId)
                .eq(AnimeRating::getUserId, userId)
                .last("LIMIT 1"));
    }

    private void refreshAnimeScore(Long animeId) {
        BigDecimal average = animeRatingMapper.selectAverageRating(animeId);
        if (average != null) {
            AnimeInfo animeInfo = animeInfoService.getById(animeId);
            animeInfo.setScore(average);
            animeInfoService.updateById(animeInfo);
        }
    }
}
