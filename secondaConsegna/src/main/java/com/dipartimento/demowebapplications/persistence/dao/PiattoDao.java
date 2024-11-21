package com.dipartimento.demowebapplications.persistence.dao;

import com.dipartimento.demowebapplications.model.Piatto;

import java.util.List;

public interface PiattoDao {
    public List<Piatto> findAll();
    public Piatto findByID(String nome);
    public void create(Piatto piatto);
    public void update(Piatto piatto);
    public void save(Piatto piatto);
    public void delete(Piatto piatto);
    List<Piatto> findAllByRistoranteName(String name);
}
