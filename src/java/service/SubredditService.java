package com.example.springredditclone.service;

import com.example.springredditclone.dto.SubredditDto;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.mapper.SubredditMapper;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto)
    {
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;

    }

    //Mapping the Data Transfer Object to an Entity
//    private Subreddit mapSubredditDto(SubredditDto subredditDto)
//    {
//        return Subreddit.builder().name(subredditDto.getName())
//                .description((subredditDto.getDescription()))
//                .build();
//    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit =  subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No subreddit found with Id " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

//    private SubredditDto mapToDto(Subreddit subreddit)
//    {
//        return SubredditDto.builder().name(subreddit.getName())
//                .id(subreddit.getId())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }
}
