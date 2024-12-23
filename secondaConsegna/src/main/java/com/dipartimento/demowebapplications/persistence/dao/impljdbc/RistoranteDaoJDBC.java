package com.dipartimento.demowebapplications.persistence.dao.impljdbc;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.model.Ristorante;
import com.dipartimento.demowebapplications.persistence.DBManager;
import com.dipartimento.demowebapplications.persistence.dao.PiattoDao;
import com.dipartimento.demowebapplications.persistence.dao.RistoranteDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RistoranteDaoJDBC implements RistoranteDao {
    Connection connection = null;

    public RistoranteDaoJDBC(Connection conn){ this.connection = conn;}

    @Override
    public List<Ristorante> findAll() {
        List<Ristorante> ristoranti = new ArrayList<>();
        String query = "SELECT nome, descrizione, ubicazione FROM ristorante";
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                RistoranteProxy proxy = new RistoranteProxy();
                proxy.setNome(resultSet.getString("nome"));
                proxy.setDescrizione(resultSet.getString("descrizione"));
                proxy.setUbicazione(resultSet.getString("ubicazione"));
                ristoranti.add(proxy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ristoranti;
    }



    @Override
    public Ristorante findByID(String nome) {
        String query = "SELECT nome, descrizione, ubicazione FROM ristorante WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                RistoranteProxy proxy = new RistoranteProxy();
                proxy.setNome(resultSet.getString("nome"));
                proxy.setDescrizione(resultSet.getString("descrizione"));
                proxy.setUbicazione(resultSet.getString("ubicazione"));
                return proxy;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void save(Ristorante ristorante) {
        String query = "INSERT INTO ristorante (nome, descrizione, ubicazione) VALUES (?, ?, ?) " +
                "ON CONFLICT (nome) DO UPDATE SET " +
                "   descrizione = EXCLUDED.descrizione , "+
                "   ubicazione = EXCLUDED.ubicazione ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristorante.getNome());
            statement.setString(2, ristorante.getDescrizione());
            statement.setString(3, ristorante.getUbicazione());
            statement.executeUpdate();

            List<Piatto> piatti = ristorante.getPiatti();
            if(piatti==null || piatti.isEmpty()){
                return;
            }

            // reset all relation present in the join table
            restRelationsPResentInTheJoinTable(connection , ristorante.getNome());

            PiattoDao pd = DBManager.getInstance().getPiattoDao();

            for (Piatto tempP : piatti) {
                pd.save(tempP);
                insertJoinRistorantePiatto(connection , ristorante.getNome() , tempP.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Ristorante ristorante) {
        String query = "INSERT INTO ristorante (nome, descrizione, ubicazione) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristorante.getNome());
            statement.setString(2, ristorante.getDescrizione());
            statement.setString(3, ristorante.getUbicazione());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ristorante ristorante) {
        String query = "UPDATE ristorante SET descrizione = ?, ubicazione = ? WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristorante.getDescrizione());
            statement.setString(2, ristorante.getUbicazione());
            statement.setString(3, ristorante.getNome());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void restRelationsPResentInTheJoinTable(Connection connection, String nomeRistorante) throws Exception {
        String query="Delete FROM ristorante_piatto WHERE ristorante_nome= ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomeRistorante);

        preparedStatement.execute();
    }

    private void insertJoinRistorantePiatto(Connection connection , String nomeRistorante, String nomePiatto) throws SQLException {
        String query="INSERT INTO ristorante_piatto (ristorante_nome,piatto_nome) VALUES (? , ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomeRistorante);
        preparedStatement.setString(2, nomePiatto);

        preparedStatement.execute();
    }

    @Override
    public void delete(Ristorante ristorante) {
        String query = "DELETE FROM ristorante WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristorante.getNome());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Ristorante> findAllByPiattoName(String piattoNome) {
        List<Ristorante> ristoranti = new ArrayList<>();
        String query = "SELECT r.nome, r.descrizione, r.ubicazione FROM ristorante r " +
                       "JOIN ristorante_piatto rp ON r.nome = rp.ristorante_nome " +
                       "WHERE rp.piatto_nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, piattoNome);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RistoranteProxy proxy = new RistoranteProxy();
                proxy.setNome(resultSet.getString("nome"));
                proxy.setDescrizione(resultSet.getString("descrizione"));
                proxy.setUbicazione(resultSet.getString("ubicazione"));
                ristoranti.add(proxy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ristoranti;
    }
}


