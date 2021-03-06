package com.sparta.finalproject6.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class
PostCommentResponseDto {

    private Long commentId;
    private Long userId;
    private String nickname;
    private String userImgUrl;
    private String comment;
    private String createdAt;


}
