package com.example.evento.controller;

import com.example.evento.modells.Convidados;
import com.example.evento.modells.Eventos;
import com.example.evento.repositoy.ConvidadosInfo;
import com.example.evento.repositoy.EventosInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventosInfo er;

    @Autowired
    private ConvidadosInfo cr;

    @RequestMapping(value = "/cadastrar", method = RequestMethod.GET)
    public String form(){
        return "evento/form";
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public String form(@Valid Eventos eventos, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem","Verifique os campos!");
            return "redirect:/cadastrar";
        }
        er.save(eventos);
        attributes.addFlashAttribute("mensagem","Evento cadastrado com sucesso!");
        return "redirect:/cadastrar";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Eventos> evento = er.findAll();
        mv.addObject("eventos", evento);
        return mv;
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalheEvento(@PathVariable("codigo") long codigo){
        Eventos eventos = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("eventos", eventos);
        Iterable<Convidados> conv = cr.findByEventos(eventos);
        mv.addObject("convidados", conv);
        return mv;
    }

    @RequestMapping("/deletarEventos")
    public String deletarEvento(long codigo){
        Eventos eventos = er.findByCodigo(codigo);
        Iterable<Convidados> convidados= cr.findByEventos(eventos);
        cr.deleteAll(convidados);
        er.delete(eventos);
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg){
        Convidados convidados = cr.findByRg(rg);
        cr.delete(convidados);
        Eventos eventos = convidados.getEventos();
        long codigoL  = eventos.getCodigo();
        String codigo = "" + codigoL;
        return "redirect:/" + codigo;
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
    public String detalheEventoPost(@PathVariable("codigo") long codigo, @Valid Convidados convidados, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/{codigo}";
        }
        Eventos eventos = er.findByCodigo(codigo);
        convidados.setEventos(eventos);
        cr.save(convidados);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/{codigo}";
    }


}
