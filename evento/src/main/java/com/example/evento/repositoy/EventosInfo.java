package com.example.evento.repositoy;

import com.example.evento.modells.Eventos;
import org.springframework.data.repository.CrudRepository;

public interface EventosInfo extends CrudRepository<Eventos, String> {
    Eventos findByCodigo(long codigo);
}
