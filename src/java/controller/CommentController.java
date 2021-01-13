package com.example.springredditclone.controller;

import com.example.springredditclone.dto.CommentsDto;
import com.example.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/comments/")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto)
    {
       commentService.save(commentsDto);
       return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId)
    {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }


    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getCommentsByPost(@PathVariable String userName)
    {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }
}
