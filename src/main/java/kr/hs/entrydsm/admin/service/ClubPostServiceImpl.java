package kr.hs.entrydsm.admin.service;

import kr.hs.entrydsm.admin.entity.Post;
import kr.hs.entrydsm.admin.entity.ClubPostRepository;
import kr.hs.entrydsm.admin.entity.PostType;
import kr.hs.entrydsm.admin.service.dto.PostPreviewResponse;
import kr.hs.entrydsm.admin.service.dto.PostRequest;
import kr.hs.entrydsm.admin.service.dto.PostResponse;
import kr.hs.entrydsm.admin.service.dto.PostsResponse;
import kr.hs.entrydsm.admin.service.exception.ForbiddenException;
import kr.hs.entrydsm.admin.service.exception.InvalidPostTypeException;
import kr.hs.entrydsm.admin.service.exception.PostNotFoundException;
import kr.hs.entrydsm.common.security.AuthMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClubPostServiceImpl implements ClubPostService {
    private final ClubPostRepository clubPostRepository;

    @Override
    public void createPost(PostRequest postRequest) {
        clubPostRepository.save(requestToeEntity(postRequest));
    }

    @Override
    @Transactional
    public void deletePost(long postId) {
        Post post = clubPostRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (!AuthMiddleware.currentClub().getId().equals(post.getClub().getId())) { throw new ForbiddenException(); }
        clubPostRepository.deleteById(post.getId());
    }

    @Override
    public void updatePost(long postId, PostRequest postRequest) {
        Post post = requestToeEntity(postRequest);
        clubPostRepository.findById(postId)
                .map(p -> clubPostRepository.save(p.update(post)))
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public PostsResponse getPosts() {
        List<PostPreviewResponse> posts = clubPostRepository.findAllByClub(AuthMiddleware.currentClub()).stream().map(post -> {
            return PostPreviewResponse.builder()
                    .postId(post.getId())
                    .author(post.getAuthor())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .createdAt(post.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());

        return PostsResponse.builder()
                .posts(posts)
                .build();
    }

    @Override
    public PostResponse getPost(long postId) {
        Post post = clubPostRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return PostResponse.builder()
                .author(post.getAuthor())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .type(post.getType().toString())
                .build();
    }

    private Post requestToeEntity(PostRequest postRequest) {
        try {
            return Post.builder()
                    .club(AuthMiddleware.currentClub())
                    .imageId(postRequest.getImageId())
                    .title(postRequest.getTitle())
                    .author(postRequest.getAuthor())
                    .description(postRequest.getDescription())
                    .content(postRequest.getContent())
                    .type(PostType.valueOf(postRequest.getType()))
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new InvalidPostTypeException();
        }
    }
}
