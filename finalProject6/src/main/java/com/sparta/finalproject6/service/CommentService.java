package com.sparta.finalproject6.service;

import com.sparta.finalproject6.dto.requestDto.CommentRequestDto;
import com.sparta.finalproject6.dto.responseDto.CommentResponseDto;
import com.sparta.finalproject6.model.Comment;
import com.sparta.finalproject6.model.Post;
import com.sparta.finalproject6.model.User;
import com.sparta.finalproject6.repository.CommentRepository;
import com.sparta.finalproject6.repository.PostRepository;
import com.sparta.finalproject6.repository.UserRepository;
import com.sparta.finalproject6.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 댓글 조회
    public List<CommentResponseDto> getComment(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 없습니다.")
        );
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        List<Comment> comments = post.getComments();
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        for(Comment comment : comments) {
            Long commentId = comment.getId();
            String nickname = comment.getNickname();
            String userImgUrl = comment.getUserImgUrl();
            String myComment = comment.getComment();
            LocalDateTime createdAt = comment.getPost().getCreatedAt();

            CommentResponseDto commentResponseDto = new CommentResponseDto(postId, commentId, myComment, nickname, userImgUrl, createdAt);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;

    }

    // 댓글 작성
    @Transactional
    public void addComment(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        Comment comment = new Comment(commentRequestDto.getComment(), post, user);

        commentRepository.save(comment);

        int commentCount = post.getCommentCount();
        commentCount++;
        post.updateCommentCount(commentCount);
        post.updateCommentCount(post.getCommentCount());
    }

    // 댓글 수정
//    public void updateComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
//        Comment comment = commentRepository.findById(commentId).orElseThrow(
//                () -> new NullPointerException("댓글이 없습니다.")
//        );
//        String username = comment.getUser().getUsername();
//        if (username.equals(userDetails.getUsername())) {
//            comment.setComment(requestDto.getComment());
//            commentRepository.save(comment);
//        }
//        else {
//            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
//        }
//    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String nickname) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        Post post = new Post();
        int commentCount = post.getCommentCount();
        String commentWriter = comment.getNickname();
        if (commentWriter.equals(nickname)) {
            commentRepository.delete(comment);
            commentCount--;
            post.updateCommentCount(commentCount);
        } else {
            throw new IllegalArgumentException("댓글을 작성한 유저가 아닙니다.");
        }
    }

}
