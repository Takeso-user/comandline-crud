package pl.malcew.publicmentoringmalcew.repo.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LabelRepoImpl extends RepoImplConnectionAbstractClass implements LabelRepo {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LabelRepoImpl.class);

    public Long create(Label entity) {
        LOGGER.info("Creating label: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT IGNORE INTO label (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.name());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating label failed, no ID obtained.");
            }
        } catch (Exception e) {
            LOGGER.error("Error creating label: ", e);
            throw new RuntimeException("Error creating label: ", e);
        }
    }

    @Override
    public Label read(Long id) {
        LOGGER.info("Reading label with id: {}", id);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM label WHERE id = ?");
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Label(id, resultSet.getString("name"));
            }
        } catch (Exception e) {
            LOGGER.error("Error reading label: ", e);
        }
        return null;
    }

    @Override
    public List<Label> viewAll() {
        LOGGER.info("Viewing all labels");
        List<Label> entities = new ArrayList<>();
        try (Connection connection = getConnection()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM label");

            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                entities.add(new Label(id, name));
            }
        } catch (Exception e) {
            LOGGER.error("Error reading labels: ", e);
        }
        return entities;
    }

    @Override
    public Label update(Label entity) {
        LOGGER.info("Updating label: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE label SET name = ? WHERE id = ?");
            preparedStatement.setString(1, entity.name());
            preparedStatement.setLong(2, entity.id());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error updating label: ", e);
        }
        return entity;
    }

    @Override
    public Long delete(Label entity) {
        LOGGER.info("Deleting label: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM label WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Cannot delete label due to existing references: ", e);

        } catch (Exception e) {
            LOGGER.error("Error deleting label: ", e);
            return 0L;
        }
        return entity.id();
    }
}