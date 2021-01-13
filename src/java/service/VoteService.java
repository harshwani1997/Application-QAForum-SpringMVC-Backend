package com.example.springredditclone.service;

import com.example.springredditclone.dto.VoteDto;
import com.example.springredditclone.exception.PostNotFoundException;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Vote;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.springredditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new PostNotFoundException(voteDto.getPostId().toString()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType()))
        {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }

        if(UPVOTE.equals(voteDto.getVoteType()))
        {
           post.setVoteCount(post.getVoteCount()+1);
        }
        else
        {
            post.setVoteCount(post.getVoteCount()-1);
        }

        postRepository.save(post);
        voteRepository.save(mapToVote(voteDto, post));
    }

    //Mapping the Data Transfer Object to an Entity
    private Vote mapToVote(VoteDto voteDto, Post post)
    {
       return Vote.builder()
               .voteType(voteDto.getVoteType())
               .post(post)
               .user(authService.getCurrentUser())
               .build();
    }
}
