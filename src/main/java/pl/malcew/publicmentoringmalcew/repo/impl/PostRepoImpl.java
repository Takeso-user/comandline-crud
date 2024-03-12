package pl.malcew.publicmentoringmalcew.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.PostStatus;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostRepoImpl extends RepoImplConnectionAbstractClass implements PostRepo {

    private final Logger LOGGER = LoggerFactory.getLogger(PostRepoImpl.class);

    @Override
    public Long create(Post entity) {
        LOGGER.info("Creating post: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO post (content, created, updated, status, writer_id) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.content());
            preparedStatement.setObject(2, entity.created());
            preparedStatement.setObject(3, entity.updated());
            preparedStatement.setLong(4, entity.status().id());
            preparedStatement.setLong(5, entity.writer().id());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating post failed, no ID obtained.");
            }
        } catch (SQLException ex) {
            LOGGER.error("Error creating post: ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Post read(Long id) {
        LOGGER.info("Reading post with id: {}", id);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT p.id, p.content, p.created, p.updated, s.status, w.firstName, w.lastName, GROUP_CONCAT(l.name) as labels " +
                            "FROM post p " +
                            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
                            "LEFT JOIN label l ON pl.label_id = l.id " +
                            "LEFT JOIN poststatus s ON p.status = s.id " +
                            "LEFT JOIN writer w ON p.writer_id = w.id " +
                            "WHERE p.id = ? " +
                            "GROUP BY p.id"
            );
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<Label> labels;
                String labelsString = resultSet.getString("labels");
                if (labelsString != null) {
                    labels = Arrays.stream(labelsString.split(","))
                            .map(name -> new Label(null, name))
                            .collect(Collectors.toList());
                } else {
                    labels = new ArrayList<>();
                }
                PostStatus status = PostStatus.valueOf(resultSet.getString("status"));
                Writer writer = new Writer(null,
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null);
                return new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getObject("created", java.time.LocalDateTime.class),
                        resultSet.getObject("updated", java.time.LocalDateTime.class),
                        labels,
                        status,
                        writer
                );
            }
        } catch (SQLException ex) {
            LOGGER.error("Error reading post: ", ex);
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Post> viewAll() {
        LOGGER.info("Viewing all posts");
        List<Post> entities = new ArrayList<>();
        try (Connection connection = getConnection()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(
                    "SELECT p.id, p.content, p.created, p.updated, s.status, w.firstName,  w.lastName , GROUP_CONCAT(l.name) as labels " +
                            "FROM post p " +
                            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
                            "LEFT JOIN label l ON pl.label_id = l.id " +
                            "LEFT JOIN poststatus s ON p.status = s.id " +
                            "LEFT JOIN writer w ON p.writer_id = w.id " +
                            "GROUP BY p.id"
            );

            while (resultSet.next()) {
                List<Label> labels;
                String labelsString = resultSet.getString("labels")==null ? "" : resultSet.getString("labels");
                if (!labelsString.isEmpty()) {
                    labels = Arrays.stream(labelsString.split(","))
                            .map(name -> new Label(null, name))
                            .collect(Collectors.toList());
                } else {
                    labels = new ArrayList<>();
                }
                PostStatus status = PostStatus.valueOf(resultSet.getString("status"));
                Writer writer = new Writer(null,
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null);
                entities.add(new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getObject("created", java.time.LocalDateTime.class),
                        resultSet.getObject("updated", java.time.LocalDateTime.class),
                        labels,
                        status,
                        writer
                ));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error viewing posts: ", ex);
            throw new RuntimeException(ex);
        }
        return entities;
    }

    @Override
    public Post update(Post entity) {
        LOGGER.info("Updating post: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE post SET content = ?, created = ?, updated = ?, status = ?, writer_id = ? WHERE id = ?");
            preparedStatement.setString(1, entity.content());
            preparedStatement.setObject(2, entity.created());
            preparedStatement.setObject(3, entity.updated());
            preparedStatement.setString(4, entity.status().name());
            preparedStatement.setLong(5, entity.writer().id());
            preparedStatement.setLong(6, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Error updating post: ", ex);
            throw new RuntimeException(ex);
        }
        return entity;
    }

    @Override
    public Long delete(Post entity) {
        LOGGER.info("Deleting post: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM post WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Cannot delete post due to existing references: ", e);

        } catch (Exception e) {
            LOGGER.error("Error deleting post: ", e);
            return 0L;
        }
        return entity.id();
    }

    @Override
    public void addLabelToPost(Long postId, Long labelId) {
        LOGGER.info("Adding label with id {} to post with id {}", labelId, postId);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)");
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Error adding label to post: ", ex);
            throw new RuntimeException(ex);
        }
    }
}