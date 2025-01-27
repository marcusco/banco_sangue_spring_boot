package br.wk.projeto.bancosangue.service;

import br.wk.projeto.bancosangue.dto.TipoSanguineoDTO;
import br.wk.projeto.bancosangue.model.TipoSanguineo;
import br.wk.projeto.bancosangue.repository.TipoSanguineoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoSanguineoService {

    private final TipoSanguineoRepository repository;

    public List<TipoSanguineoDTO> findAll() {
       return repository.findAll().stream().map(this::toDto).toList();
    }

    public TipoSanguineoDTO save(TipoSanguineoDTO dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    private TipoSanguineoDTO toDto(TipoSanguineo entity) {
        return TipoSanguineoDTO.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .doar(entity.getDoar())
                .receber(entity.getReceber())
                .build();
    }

    private TipoSanguineo toEntity(TipoSanguineoDTO dto) {
        final var tmp = new TipoSanguineo();
        tmp.setId(dto.getId());
        tmp.setCodigo(dto.getCodigo());
        tmp.setDoar(dto.getDoar());
        tmp.setReceber(dto.getReceber());
        return tmp;

    }
}
