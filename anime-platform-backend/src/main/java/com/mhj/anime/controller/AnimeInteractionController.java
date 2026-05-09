package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.dto.CommentRequest;
import com.mhj.anime.dto.RatingRequest;
import com.mhj.anime.service.AnimeInteractionService;
import com.mhj.anime.vo.CommentVO;
import com.mhj.anime.vo.UserAnimeStateVO;
import com.mhj.anime.vo.UserCommentVO;
import com.mhj.anime.vo.UserFavoriteVO;
import com.mhj.anime.vo.UserRatingVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnimeInteractionController {

    private final AnimeInteractionService animeInteractionService;

    public AnimeInteractionController(AnimeInteractionService animeInteractionService) {
        this.animeInteractionService = animeInteractionService;
    }

    @GetMapping(value = "/api/anime/{animeId}/comments", produces = "application/json;charset=UTF-8")
    public ApiResult<List<CommentVO>> comments(@PathVariable Long animeId) {
        return ApiResult.success(animeInteractionService.listComments(animeId));
    }

    @GetMapping(value = "/api/user/anime/{animeId}/state", produces = "application/json;charset=UTF-8")
    public ApiResult<UserAnimeStateVO> state(@PathVariable Long animeId,
                                             @RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.getUserState(animeId, currentUserId));
    }

    @PostMapping(value = "/api/user/anime/{animeId}/comments", produces = "application/json;charset=UTF-8")
    public ApiResult<CommentVO> addComment(@PathVariable Long animeId,
                                           @RequestAttribute("currentUserId") Long currentUserId,
                                           @RequestBody CommentRequest request) {
        return ApiResult.success(animeInteractionService.addComment(animeId, currentUserId, request));
    }

    @PostMapping(value = "/api/user/anime/{animeId}/favorite", produces = "application/json;charset=UTF-8")
    public ApiResult<UserAnimeStateVO> favorite(@PathVariable Long animeId,
                                                @RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.favorite(animeId, currentUserId));
    }

    @DeleteMapping(value = "/api/user/anime/{animeId}/favorite", produces = "application/json;charset=UTF-8")
    public ApiResult<UserAnimeStateVO> cancelFavorite(@PathVariable Long animeId,
                                                      @RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.cancelFavorite(animeId, currentUserId));
    }

    @PostMapping(value = "/api/user/anime/{animeId}/rating", produces = "application/json;charset=UTF-8")
    public ApiResult<UserAnimeStateVO> rating(@PathVariable Long animeId,
                                              @RequestAttribute("currentUserId") Long currentUserId,
                                              @RequestBody RatingRequest request) {
        return ApiResult.success(animeInteractionService.rate(animeId, currentUserId, request));
    }

    @GetMapping(value = "/api/user/favorites", produces = "application/json;charset=UTF-8")
    public ApiResult<List<UserFavoriteVO>> favorites(@RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.listUserFavorites(currentUserId));
    }

    @GetMapping(value = "/api/user/ratings", produces = "application/json;charset=UTF-8")
    public ApiResult<List<UserRatingVO>> ratings(@RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.listUserRatings(currentUserId));
    }

    @GetMapping(value = "/api/user/comments", produces = "application/json;charset=UTF-8")
    public ApiResult<List<UserCommentVO>> userComments(@RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(animeInteractionService.listUserComments(currentUserId));
    }
}
