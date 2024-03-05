package pl.malcew.publicmentoringmalcew.repo.impl;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
@Component
public class LabelRepoImpl extends RepoImplConnectionAbstractClass implements LabelRepo {


    @Override
    public void create(Label entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO label (name) VALUES (?)");
            preparedStatement.setString(1, entity.name());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.printf("Error creating label: %s", e);
        }
    }

    @Override
    public Label read(Long id) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM label WHERE id = ?");
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Label(id, resultSet.getString("name"));
            }
        } catch (Exception e) {
            System.err.printf("Error reading label: %s", e);
        }
        return null;
    }

    @Override
    public List<Label> viewAll() {
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
            System.err.printf("Error reading labels: %s", e);
        }
        return entities;
    }

    @Override
    public void update(Label entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE label SET name = ? WHERE id = ?");
            preparedStatement.setString(1, entity.name());
            preparedStatement.setLong(2, entity.id());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.printf("Error updating label: %s", e);
        }
    }

    @Override
    public void delete(Label entity) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM label WHERE id = ?");
            preparedStatement.setLong(1, entity.id());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.printf("Error deleting label: %s", e);
        }
    }
}
