package pl.malcew.publicmentoringmalcew.repo.impl;

import org.springframework.stereotype.Repository;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class WriterRepositoryImpl extends RepoImplConnectionAbstractClass implements WriterRepo {


    @Override
    public void create(Writer entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO writer (firstName, lastName) VALUES (?, ?)");
            preparedStatement.setString(1, entity.firstName());
            preparedStatement.setString(2, entity.lastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Error creating writer: %s", e);

        }
    }

    @Override
    public Writer read(Long id) {

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
            System.err.printf("Error interacting with DB: %s", e);
            return null;
        }
        return null;
    }

    @Override
    public List<Writer> viewAll() {
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
            System.err.printf("Error interacting with DB: %s", e);
        }
        return entities;
    }

    @Override
    public void update(Writer entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE writer SET firstName = ?, lastName = ? WHERE id = ?");
            preparedStatement.setString(1, entity.firstName());
            preparedStatement.setString(2, entity.lastName());
            preparedStatement.setLong(3, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Error interacting with DB: %s", e);

        }
    }

    @Override
    public void delete(Writer entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM writer WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Error interacting with DB: %s", e);
        }
    }


}
