package pl.malcew.publicmentoringmalcew.repo.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class PostRepoImpl extends RepoImplConnectionAbstractClass implements PostRepo {


    @Override
    public void create(Post entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO post (content, created, updated, status, writer_id) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, entity.content());
            preparedStatement.setObject(2, entity.created());
            preparedStatement.setObject(3, entity.updated());
            preparedStatement.setString(4, entity.status().name());
            preparedStatement.setLong(5, entity.writer().id());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Post read(Long id) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE id = ?");
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Post(
                        null,
                        resultSet.getString("content"),
                        resultSet.getObject("created", java.time.LocalDateTime.class),
                        resultSet.getObject("updated", java.time.LocalDateTime.class),
                        null,
                        null,
                        null
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
            var resultSet = statement.executeQuery("SELECT * FROM post");
            while (resultSet.next()) {
                entities.add(new Post(
                        null,
                        resultSet.getString("content"),
                        resultSet.getObject("created", java.time.LocalDateTime.class),
                        resultSet.getObject("updated", java.time.LocalDateTime.class),
                        null,
                        null,
                        null
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
}
