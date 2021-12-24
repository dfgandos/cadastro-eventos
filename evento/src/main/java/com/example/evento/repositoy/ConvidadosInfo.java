package com.example.evento.repositoy;

import com.example.evento.modells.Convidados;
import com.example.evento.modells.Eventos;
import org.springframework.data.repository.CrudRepository;

public interface ConvidadosInfo extends CrudRepository<Convidados, String> {
   Iterable<Convidados> findByEventos(Eventos eventos);
   Convidados findByRg(String rg);
}
