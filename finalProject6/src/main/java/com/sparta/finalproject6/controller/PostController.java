package com.sparta.finalproject6.controller;


import com.sparta.finalproject6.dto.requestDto.PlaceRequestDto;
import com.sparta.finalproject6.dto.requestDto.PostRequestDto;
import com.sparta.finalproject6.dto.requestDto.ThemeCategoryDto;
import com.sparta.finalproject6.dto.responseDto.PostDetailResponseDto;
import com.sparta.finalproject6.dto.responseDto.PostResponseDto;
import com.sparta.finalproject6.security.UserDetailsImpl;
import com.sparta.finalproject6.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    //포스트 메인페이지 조회
    @GetMapping("/api/posts")
    public ResponseEntity<Slice<PostResponseDto>> getAllPosts(@RequestParam(value = "keyword") String keyword,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              Pageable pageable) {

        if (userDetails == null) {
            return postService.getGuestPosts(keyword, pageable);
        } else {
            return postService.getPosts(keyword, pageable, userDetails);
        }
    }


    //필터 적용된 게시물 조회
    @GetMapping("/api/posts/filter")
    public ResponseEntity<Slice<PostResponseDto>> getFilterPosts(@RequestParam(value = "region") String region,
                                                                 @RequestParam(value = "price") String price,
                                                                 @RequestParam(value = "theme") List<String> theme,
                                                                 Pageable pageable,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return postService.getGuestFilterPosts(region, price, theme, pageable);
        } else {
            return postService.getFilterPosts(region, price, theme, pageable, userDetails);
        }

    }


    // 포스트 상세페이지
    @GetMapping("/api/post/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            return new ResponseEntity(postService.getPostDetail(postId, userDetails), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // 포스트 등록
    @PostMapping("api/post")
    public ResponseEntity<String> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam("regionCategory") String regionCategory,
                                             @RequestParam("themeCategory") List<ThemeCategoryDto> themeCategory,
                                             @RequestParam("priceCategory") String priceCategory,
                                             @RequestPart("places") List<PlaceRequestDto> placeRequestDto,
                                             @RequestPart("imgUrl") List<MultipartFile> multipartFile) {
        log.info("postUserDetails = {}", userDetails);
        PostRequestDto requestDto = new PostRequestDto(title,content,regionCategory,priceCategory,themeCategory);
        try{
            postService.addPost(userDetails, requestDto, placeRequestDto ,multipartFile);
            return new ResponseEntity<>("게시글을 작성했습니다.", HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // 포스트 수정
    @PutMapping("/api/post/{postId}")
    public ResponseEntity<String> modifyPost(@PathVariable Long postId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam("regionCategory") String regionCategory,
                                             @RequestParam("themeCategory") List<ThemeCategoryDto> themeCategory,
                                             @RequestParam("priceCategory") String priceCategory,
                                             @RequestPart("places") List<PlaceRequestDto> placeRequestDto,
                                             @RequestPart(value = "imgUrl",required = false) List<MultipartFile> multipartFile){
        PostRequestDto requestDto = new PostRequestDto(title,content,regionCategory,priceCategory,themeCategory);
        try{
            postService.modifyPost(userDetails, requestDto, placeRequestDto,multipartFile,postId);
            return new ResponseEntity<>("게시글을 수정했습니다.",HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }


    // 포스트 삭제
    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<String> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails ,
                                             @PathVariable Long postId){
        try{
            postService.deletePost(userDetails,postId);
            return new ResponseEntity<>("게시글을 삭제했습니다.",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
