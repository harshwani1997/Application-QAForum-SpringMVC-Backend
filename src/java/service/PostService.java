package com.example.springredditclone.service;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.exception.PostNotFoundException;
import com.example.springredditclone.exception.SubredditNotFoundException;
import com.example.springredditclone.mapper.PostMapper;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.SubredditRepository;
import com.example.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostMapper postMapper;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post save(PostRequest postRequest)
    {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName()).orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        User user = authService.getCurrentUser();
        return postRepository.save(postMapper.map(postRequest, subreddit, user));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts()
    {
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id)
    {
        return postMapper.mapToDto(postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString())));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username)
    {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with name - " + username));
        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId)
    {
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        return postRepository.findAllBySubreddit(subreddit).stream().map(postMapper::mapToDto).collect(toList());
    }
}
