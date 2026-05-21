package com.redditclone.controller;

import com.redditclone.dto.VoteDtos.VoteRequest;
import com.redditclone.dto.VoteDtos.VoteResponse;
import com.redditclone.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes authenticated vote mutation endpoints.
 */
@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    /**
     * Injects vote behavior.
     */
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Upvotes or downvotes a post and returns the updated score.
     */
    @PostMapping
    public VoteResponse vote(@Valid @RequestBody VoteRequest request) {
        return voteService.vote(request);
    }
}
