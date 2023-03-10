package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.fixture.CommentFixture;
import com.team9.bucket_list.fixture.MemberFixture;
import com.team9.bucket_list.fixture.PostFixture;
import com.team9.bucket_list.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    Logger log = (Logger) LoggerFactory.getLogger(CommentControllerTest.class);    // Junit?????? log?????? ?????? ??????(Junit????????? @Slf4j ??????????????? ?????? ?????????)

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean   // ?????? ??????
    CommentService commentService;

    @MockBean
    BCryptPasswordEncoder encoder;



    @Nested
    @DisplayName("Comment CRUD")
    class CommentCRUD {


        @Test
        @DisplayName("?????? ?????? ??????")
        @WithMockUser(username = "1")
        void createComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",null);

            Comment comment = request.toEntity(post,null,member);          // ???????????? ??????


            CommentCreateResponse response = new CommentCreateResponse(comment,member.getUserName());



            given(commentService.commentCreate(any(),any(),any()))
                    .willReturn(response);

            mockMvc.perform(post("/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..userName").exists())
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(status().isOk());

            Assertions.assertEquals(response.getParentId(),null);       // ???????????? ??????(???, ?????? ????????? ????????????????????? ???)

            verify(commentService).commentCreate(any(),any(),any());
        }

        @Test
        @DisplayName("????????? ?????? ??????")
        @WithMockUser(username = "1")
        void createReplyComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",1l);      // 1l??? ????????? ??????
            
            Comment parentComment = CommentFixture.get(1l,post,null,member,"firstComment");    // ?????? ??????
            
            Comment comment = request.toEntity(post,parentComment,member);          // ????????? ??????


            CommentCreateResponse response = new CommentCreateResponse(comment,member.getUserName());



            given(commentService.commentCreate(any(),any(),any()))
                    .willReturn(response);

            mockMvc.perform(post("/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..userName").exists())
                    .andExpect(jsonPath("$..parentId").value(1))       // ???????????? ??????(??????????????? ??????????????? ?????? ?????? ????????? ?????????????????? ???)
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(status().isOk());

            verify(commentService).commentCreate(any(),any(),any());
        }

        @Test
        @DisplayName("?????? ?????? ?????? - ????????? ??????")
        @WithMockUser(username = "1")
        void createComment_fail_one() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");

            Post post = null;                   // ?????? ???????????? ?????? ???

            CommentCreateRequest request = new CommentCreateRequest("content",null);      // 1l??? ????????? ??????


                 given(commentService.commentCreate(any(),any(),any()))
                        .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));


            mockMvc.perform(post("/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).commentCreate(any(),any(),any());
        }


        @Test
        @DisplayName("?????? ?????? ?????? - ????????????(???????????????)")
        @WithMockUser(username = "1")
        void createComment_fail_two() throws Exception {
            Member member = null;                       // ???????????? ?????? ?????? ?????? ???????????? ?????????

            CommentCreateRequest request = new CommentCreateRequest("content",null);      // 1l??? ????????? ??????


            given(commentService.commentCreate(any(),any(),any()))
                    .willThrow(new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));


            mockMvc.perform(post("/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).commentCreate(any(),any(),any());
        }


        // ?????? ????????? ?????? ??????
        @GetMapping("/{postId}/comments")
        @Operation(summary = "?????? ????????? ??????", description = "?????? ???????????? ?????? ???????????? ???????????????.")
        public Response<List<CommentListResponse>> commentList(@Parameter(name = "postId", description = "????????? id") @PathVariable(name = "postId") Long id){
            List<CommentListResponse> commentList = commentService.commentList(id);
            return Response.success(commentList);
        }

        @Test
        @DisplayName("?????? ????????? ?????? ??????")
        @WithMockUser(username = "1")
        void readComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",1l);      // 1l??? ????????? ??????

            Comment parentComment = CommentFixture.get(1l,post,null,member,"firstComment");    // ?????? ?????? 1???
            Comment replyComment = request.toEntity(post,parentComment,member);          // ????????? ??????

            Comment parentComment2 = CommentFixture.get(2l,post,null,member,"secondComment");    // ?????? ?????? 2???

            CommentListResponse listResponse1 = new CommentListResponse().EntityToDto(parentComment,member.getUserName(),member.getId());
            CommentListResponse listResponse2 = new CommentListResponse().EntityToDto(replyComment,member.getUserName(),member.getId());
            CommentListResponse listResponse3 = new CommentListResponse().EntityToDto(parentComment2,member.getUserName(),member.getId());

            List<CommentListResponse> responses = new ArrayList<>();

            responses.add(listResponse1);
            responses.add(listResponse2);
            responses.add(listResponse3);


            given(commentService.commentList(any()))
                    .willReturn(responses);

            mockMvc.perform(get("/comment/1/comments")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isOk());


            verify(commentService).commentList(any());
        }


        // ?????? ??????
        @PutMapping("/{postId}/comments/{commentId}")
        @Operation(summary = "?????? ??????", description = "????????? ???????????????.")
        public Response<List<CommentListResponse>> commentUpdate(@Parameter(name = "postId", description = "????????? id") @PathVariable(name = "postId")Long postid,
                                                                 @Parameter(name = "commentId", description = "?????? id") @PathVariable(name="commentId")Long id,
                                                                 @RequestBody CommentCreateRequest request,Authentication authentication){
            Long memberId = Long.valueOf(authentication.getName());
//        Long memberId = 1l;
            List<CommentListResponse> commentList = commentService.updateComment(postid,id,request,memberId);
            return Response.success(commentList);
        }

        @Test
        @DisplayName("?????? ?????? ??????")
        @WithMockUser(username = "1")
        void updateComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);

            Comment comment = CommentFixture.get(1l,post,null,member,"firstComment");    // ?????? ?????? 1???

            comment.update(request.getContent());               // ????????? ????????????

            List<CommentListResponse> responses = new ArrayList<>();

            CommentListResponse listResponse = new CommentListResponse().EntityToDto(comment,member.getUserName(),member.getId());

            responses.add(listResponse);

            given(commentService.updateComment(any(),any(),any(),any()))
                    .willReturn(responses);

            mockMvc.perform(put("/comment/1/comments/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..content").value("updatecontent"))
                    .andExpect(status().isOk());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("?????? ?????? ?????? - ????????? ??????")
        @WithMockUser(username = "1")
        void updateComment_fail_one() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = null;

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);


            given(commentService.updateComment(any(),any(),any(),any()))
                    .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(put("/comment/1/comments/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("?????? ?????? ?????? - ?????? ?????????")
        @WithMockUser(username = "1")
        void updateComment_fail_two() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");      // ?????? ?????? ??????
            Member member2 = MemberFixture.get("test2@naver.com","1234","test2");   // ?????? ???????????? ??????
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);

            Comment comment = CommentFixture.get(1l,post,null,member,"firstComment");    // ?????? ?????? 1???

            comment.update(request.getContent());               // ????????? ????????????

            List<CommentListResponse> responses = new ArrayList<>();

            CommentListResponse listResponse = new CommentListResponse().EntityToDto(comment,member.getUserName(),member.getId());

            responses.add(listResponse);

            if(!member.getUserName().equals(member2.getUserName())){
                given(commentService.updateComment(any(),any(),any(),any()))
                        .willThrow(new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
            }else{
                given(commentService.updateComment(any(),any(),any(),any()))
                        .willReturn(responses);
            }


            mockMvc.perform(put("/comment/1/comments/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("?????? ?????? ??????")
        @WithMockUser(username = "1")
        void deleteComment_success() throws Exception {
            mockMvc.perform(delete("/comment/1/comments/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

}