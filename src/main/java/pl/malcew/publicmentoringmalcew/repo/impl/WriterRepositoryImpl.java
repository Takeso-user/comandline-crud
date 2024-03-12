package pl.malcew.publicmentoringmalcew.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WriterRepositoryImpl extends RepoImplConnectionAbstractClass implements WriterRepo {

    private final Logger LOGGER = LoggerFactory.getLogger(WriterRepositoryImpl.class);

    @Override
    public Long create(Writer entity) {
        LOGGER.info("Creating writer: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO writer (firstName, lastName) VALUES (?, ?)");
            preparedStatement.setString(1, entity.firstName());
            preparedStatement.setString(2, entity.lastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error creating writer: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Writer read(Long id) {
        LOGGER.info("Reading writer with id: {}", id);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM writer WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                return new Writer(id, firstName, lastName, null);
            }
        } catch (SQLException e) {
            LOGGER.error("Error reading writer: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Writer> viewAll() {
        LOGGER.info("Viewing all writers");
        List<Writer> entities = new ArrayList<>();
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM writer");

            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                entities.add(new Writer(id, firstName, lastName, null));
            }
        } catch (SQLException e) {
            LOGGER.error("Error viewing writers: ", e);
            throw new RuntimeException(e);
        }
        return entities;
    }

    @Override
    public Writer update(Writer entity) {
        LOGGER.info("Updating writer: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE writer SET firstName = ?, lastName = ? WHERE id = ?");
            preparedStatement.setString(1, entity.firstName());
            preparedStatement.setString(2, entity.lastName());
            preparedStatement.setLong(3, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating writer: ", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public Long delete(Writer entity) {
        LOGGER.info("Deleting writer: {}", entity);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM writer WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Cannot delete writer due to existing references: ", e);
            return 0L;
        } catch (Exception e) {
            LOGGER.error("Error deleting writer: ", e);
            return 0L;
        }
        return entity.id();
    }
}