package br.com.diego.soares.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "coordenador")
public class Coordenador extends PanacheEntity {

}
