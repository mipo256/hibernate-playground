package org.example.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.example.AbstractIntegrationTest;
import org.example.model.entity_graph_multiple_queries.Post;
import org.example.model.entity_graph_multiple_queries.PostComment;
import org.example.model.entity_graph_multiple_queries.User;
import org.example.repository.entity_graph_multiple_queries.UserRepository;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManagerFactory;

public class MergePersistRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testStatelessSessionLoading() {
        // given
        User saved = prepareUser();

        // and
        SessionFactory unwrap = emf.unwrap(SessionFactory.class);
        StatelessSession statelessSession = unwrap.openStatelessSession();

        // when.
        User fromDb = statelessSession.get(User.class, saved.getId());
        List<Post> postsFromDb = fromDb.getPosts();
        statelessSession.fetch(postsFromDb);
        postsFromDb.forEach(post -> statelessSession.fetch(post.getPostComments()));

        Assertions.assertThat(fromDb.getName()).isEqualTo("MyPost");
        Assertions.assertThat(fromDb.getPosts()).hasSize(1).first().satisfies(post -> {
            Assertions.assertThat(post.getContent()).isEqualTo("Some content");
            Assertions.assertThat(post.getPostComments()).hasSize(1).first().extracting(PostComment::getComment).isEqualTo("That was awesome!");
        });
    }

    private @NotNull User prepareUser() {
        User user = new User().setName("MyPost");

        Post post = new Post().setContent("Some content");
        post.setUser(user);

        user.setPosts(List.of(post));

        List<PostComment> postComments = List.of(
          new PostComment()
            .setComment("That was awesome!")
            .setPost(post)
        );

        post.setPostComments(postComments);

        User saved = userRepository.save(user);
        return saved;
    }
}
