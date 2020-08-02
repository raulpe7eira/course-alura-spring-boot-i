package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalheTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizaTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomeCurso) {
        List<Topico> topicos = nomeCurso == null
                ? topicoRepository.findAll()
                : topicoRepository.findByCursoNome(nomeCurso);
        return TopicoDto.converter(topicos);
    }

    @PostMapping
    public ResponseEntity<TopicoDto> cadastrar(
            @RequestBody @Valid TopicoForm form,
            UriComponentsBuilder uriBuilder
    ) {
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public DetalheTopicoDto detalhar(@PathVariable Long id) {
        Topico topico = topicoRepository.getOne(id);
        return new DetalheTopicoDto(topico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizaTopicoForm form
    ) {
        Topico topico = form.atualizar(id, topicoRepository);
        return ResponseEntity.ok(new TopicoDto(topico));
    }

}
