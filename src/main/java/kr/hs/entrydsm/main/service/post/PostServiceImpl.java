package kr.hs.entrydsm.main.service.post;

import kr.hs.entrydsm.main.enitity.repository.PostRepository;
import kr.hs.entrydsm.main.payload.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostResponse> getListPost() {
        return postRepository.findAllBy().stream()
                    .map(post -> PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .image(post.getImage().getPath())
                            .type(post.getPostType())
                            .build())
                    .collect(Collectors.toList());
    }
}

