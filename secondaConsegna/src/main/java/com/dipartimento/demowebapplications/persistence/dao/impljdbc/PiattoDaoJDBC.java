package com.dipartimento.demowebapplications.persistence.dao.impljdbc;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.persistence.dao.PiattoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PiattoDaoJDBC implements PiattoDao {
    Connection connection;

    public PiattoDaoJDBC(Connection conn){ this.connection = conn;}

    @Override
    public List<Piatto> findAll() {
        List<Piatto> piatti = new ArrayList<>();
        String query = "SELECT nome, ingredienti FROM piatto";
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String ingredienti = resultSet.getString("ingredienti");
                PiattoProxy proxy = new PiattoProxy();
                proxy.setNome(nome);
                proxy.setIngredienti(ingredienti);
                piatti.add(proxy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return piatti;
    }


    @Override
    public Piatto findByID(String nome) {
        String query = "SELECT nome, ingredienti FROM piatto WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PiattoProxy proxy = new PiattoProxy();
                proxy.setNome(resultSet.getString("nome"));
                proxy.setIngredienti(resultSet.getString("ingredienti"));
                return proxy;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Piatto piatto) {
        String query = "INSERT INTO piatto (nome, ingredienti) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, piatto.getNome());
            statement.setString(2, piatto.getIngredienti());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Piatto piatto) {
        String query = "UPDATE piatto SET ingredienti = ? WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, piatto.getIngredienti());
            statement.setString(2, piatto.getNome());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    @Override
    public void save(Piatto piatto) {
        String query = "INSERT INTO piatto (nome, ingredienti) VALUES (?, ?) " +
                "ON CONFLICT (nome) DO UPDATE SET ingredienti = EXCLUDED.ingredienti";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, piatto.getNome());
            statement.setString(2, piatto.getIngredienti());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Piatto piatto) {
        String query = "DELETE FROM piatto WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, piatto.getNome());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Piatto> findAllByRistoranteName(String ristoranteNome) {
        List<Piatto> piatti = new ArrayList<>();
        String query = "SELECT p.nome, p.ingredienti FROM piatto p " +
                       "JOIN ristorante_piatto rp ON p.nome = rp.piatto_nome " +
                       "WHERE rp.ristorante_nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristoranteNome);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PiattoProxy proxy = new PiattoProxy();
                proxy.setNome(resultSet.getString("nome"));
                proxy.setIngredienti(resultSet.getString("ingredienti"));
                piatti.add(proxy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return piatti;
    }
}
