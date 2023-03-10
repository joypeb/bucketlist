package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Slf4j
public class PostReadResponse {

    private Long postId;
    private String userName;    // 유저 아이디이름
    private Long userId;    // 유저 아이디
    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private Double lat;
    private Double lng; // 위경도
    private String untilRecruit; //모집종료날짜
    private int entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private PostStatus status; //defalt = 모집중
    private String category; //카테고리
    private String fileName; //S3에 저장된 이미지 파일이름
    private Long permitNum;
    private Long fileId;    // DB에 저장된 파일 ID
//    private Member member; // 버킷리스트를 만든 member --> member_id, nickname
//    private List<Application> applicationList; // 버킷리스트 참가자 목록
//    private List<Likes> likeList; // 버킷리스트 좋아요 누른 사람 목록
//    private List<Comment> commentList; // 버킷리스트의 댓글들

    // 글 하나 상세볼 때 사용하는 메서드
    public static PostReadResponse detailOf(Post post, Double lat, Double lng) {  // 위경도는 DB에 저장하지 않으므로 매개변수로 받아서 DTO화 시킨다.
        log.info("post.getfile :"+post.getPostFileList().isEmpty());
        log.info("post.getfileId :"+post.getPostFileList().isEmpty());
        return PostReadResponse.builder()
                .postId(post.getId())
                .userName(post.getMember().getUserName())
                .userId(post.getMember().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .cost(post.getCost())
                .location(post.getLocation())
                .lat(lat)
                .lng(lng)
                .untilRecruit(post.getUntilRecruit())
                .entrantNum(post.getEntrantNum())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .status(post.getStatus())
                .category(post.getCategory())
                .fileName(post.getPostFileList().isEmpty()?  "noImage" : post.getPostFileList().get(0).getAwsS3FileName())
                .permitNum(post.getPermitNum())
                .fileId(post.getPostFileList().isEmpty()? 0 : post.getPostFileList().get(0).getId() )
//                .member(post.getMember())
//                .applicationList(post.getApplicationList())
//                .likeList(post.getLikesList())
//                .commentList(post.getCommentList())
                .build();
    }



    // list로 볼 때 사용하는 메서드
    // builder로 구성된 것은 조회 시, 제목처럼 한 줄에 나와있을 내용을 나열한 것이다.
    public static Page<PostReadResponse> listOf(Page<Post> posts) {
        return posts.map(post -> PostReadResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .userName(post.getMember().getUserName())
                .status(post.getStatus())
                .untilRecruit(post.getUntilRecruit())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .location(post.getLocation())
                .cost(post.getCost())
                .fileName(post.getPostFileList().isEmpty()?  "noImage" : post.getPostFileList().get(0).getAwsS3FileName())
//                .applicationList(post.getApplicationList()) // 총 승인 인원 확인
                .build()
        );
    }
}
