package pl.malcew.publicmentoringmalcew.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.malcew.publicmentoringmalcew.model.*;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepoImpl extends RepoImplConnectionAbstractClass implements PostRepo {

    private final Logger LOGGER = LoggerFactory.getLogger(PostRepoImpl.class);

    @Override
    public Long create(Post entity) {
        LOGGER.info("Creating post: {}", entity);
        try {
            if (!isWriterActive(entity.writer().id())) {
                LOGGER.info("The writer is deleted");
                return null;
            }
            return createPost(entity);
        } catch (SQLException ex) {
            LOGGER.error("Error creating post: ", ex);
            throw new RuntimeException(ex);
        }
    }

    Long createPost(Post entity) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO post (content, created, updated, status_id, writer_id) VALUES (?, ?, ?, ?, ?)",
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
        }
    }

    boolean isWriterActive(Long writerId) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement checkWriterStatement = connection.prepareStatement(
                    "SELECT status_id, firstName, lastName FROM writer WHERE id = ?");
            checkWriterStatement.setLong(1, writerId);
            ResultSet writerStatusResult = checkWriterStatement.executeQuery();
            if (writerStatusResult.next()) {
                Long statusId = writerStatusResult.getLong("status_id");
                String firstName = writerStatusResult.getString("firstName");
                String lastName = writerStatusResult.getString("lastName");
                LOGGER.info("Writer's name: {} {}", firstName, lastName); // Log the writer's first name and last name
                return statusId == 1L; // return true if the writer's status is ACTIVE
            }
        }
        return false;
    }

    @Override
    public Post read(Long id) {
        LOGGER.info("Reading post with id: {}", id);
        List<Post> entities = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = """
                    SELECT p.id, p.content, p.created, p.updated, s.status, w.firstName, w.lastName, w.status_id, 
                    GROUP_CONCAT(l.id) as label_ids, GROUP_CONCAT(l.name) as label_names, 
                    GROUP_CONCAT(l.status_id) as label_status_ids 
                    FROM post p 
                    LEFT JOIN post_labels pl ON p.id = pl.post_id 
                    LEFT JOIN label l ON pl.label_id = l.id 
                    LEFT JOIN poststatus s ON p.status_id = s.id 
                    LEFT JOIN writer w ON p.writer_id = w.id 
                    WHERE p.id = ? 
                    GROUP BY p.id, p.content, p.created, p.updated, s.status, w.firstName, w.lastName, w.status_id
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Label> labels = new ArrayList<>();
                String labelIds = resultSet.getString("label_ids");
                String labelNames = resultSet.getString("label_names");
                String labelStatusIds = resultSet.getString("label_status_ids");
                if (labelIds != null && labelNames != null && labelStatusIds != null) {
                    String[] ids = labelIds.split(",");
                    String[] names = labelNames.split(",");
                    String[] statusIds = labelStatusIds.split(",");
                    for (int i = 0; i < ids.length; i++) {
                        labels.add(new Label(
                                Long.parseLong(ids[i]),
                                names[i],
                                LabelStatus.getById(Long.parseLong(statusIds[i]))));
                    }
                }

                String statusString = resultSet.getString("status");
                PostStatus status = statusString != null ? PostStatus.valueOf(statusString) : PostStatus.ACTIVE; // replace DEFAULT_STATUS with the default status you want to use
                WriterStatus writerStatus = WriterStatus.getById(resultSet.getLong("status_id"));
                Writer writer = new Writer(null,
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null,
                        writerStatus);
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
        return entities.get(0);
    }

    @Override
    public List<Post> viewAll() {
        LOGGER.info("Viewing all posts");
        List<Post> entities = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = """
                    SELECT p.id, p.content, p.created, p.updated, s.status, w.firstName, w.lastName, w.status_id,
                    GROUP_CONCAT(l.id) as label_ids, GROUP_CONCAT(l.name) as label_names,
                    GROUP_CONCAT(l.status_id) as label_status_ids
                    FROM post p
                    LEFT JOIN post_labels pl ON p.id = pl.post_id
                    LEFT JOIN label l ON pl.label_id = l.id
                    LEFT JOIN poststatus s ON p.status_id = s.id
                    LEFT JOIN writer w ON p.writer_id = w.id
                    GROUP BY p.id, p.content, p.created, p.updated, s.status, w.firstName, w.lastName, w.status_id
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Label> labels = new ArrayList<>();
                String labelIds = resultSet.getString("label_ids");
                String labelNames = resultSet.getString("label_names");
                String labelStatusIds = resultSet.getString("label_status_ids");
                if (labelIds != null && labelNames != null && labelStatusIds != null) {
                    String[] ids = labelIds.split(",");
                    String[] names = labelNames.split(",");
                    String[] statusIds = labelStatusIds.split(",");
                    for (int i = 0; i < ids.length; i++) {
                        labels.add(new Label(
                                Long.parseLong(ids[i]),
                                names[i],
                                LabelStatus.getById(Long.parseLong(statusIds[i]))));
                    }
                }

                String statusString = resultSet.getString("status");
                PostStatus status = statusString != null ? PostStatus.valueOf(statusString) : PostStatus.ACTIVE; // replace DEFAULT_STATUS with the default status you want to use
                WriterStatus writerStatus = WriterStatus.getById(resultSet.getLong("status_id"));
                Writer writer = new Writer(null,
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null,
                        writerStatus);
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
                    "UPDATE post SET content = ?, created = ?, updated = ?, status_id = ?, writer_id = ? WHERE id = ?");
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
        String sql = "UPDATE post SET status_id = 3 WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)"
            );
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Error adding label to post: ", ex);
            throw new RuntimeException(ex);
        }
    }
}