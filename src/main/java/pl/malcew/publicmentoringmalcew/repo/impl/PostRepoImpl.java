package pl.malcew.publicmentoringmalcew.repo.impl;

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


    @Override
    public Long create(Post entity) {
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
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Post read(Long id) {
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
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Post> viewAll() {
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
            throw new RuntimeException(ex);
        }
        return entities;
    }
    @Override
    public void update(Post entity) {
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
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void delete(Post entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM post WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void addLabelToPost(Long postId, Long labelId) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)");
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
