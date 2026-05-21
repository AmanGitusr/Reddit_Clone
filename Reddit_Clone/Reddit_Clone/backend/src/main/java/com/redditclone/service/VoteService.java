package com.redditclone.service;

import com.redditclone.domain.Post;
import com.redditclone.domain.UserAccount;
import com.redditclone.domain.Vote;
import com.redditclone.dto.VoteDtos.VoteRequest;
import com.redditclone.dto.VoteDtos.VoteResponse;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns upvote/downvote mutation while enforcing one vote per user per post.
 */
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;

    /**
     * Injects vote persistence, post lookup, and authenticated user access.
     */
    public VoteService(VoteRepository voteRepository, PostRepository postRepository, CurrentUserService currentUserService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Creates or replaces the authenticated user's vote on a post.
     */
    @Transactional
    public VoteResponse vote(VoteRequest request) {
        UserAccount user = currentUserService.currentUser();
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Vote vote = voteRepository.findByUserAndPost(user, post)
                .orElseGet(() -> new Vote(request.type(), user, post));
        vote.setType(request.type());
        voteRepository.save(vote);
        return new VoteResponse(post.getId(), vote.getType(), voteRepository.scoreForPost(post.getId()));
    }
}
