package br.wk.projeto.bancosangue.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MediaIdadeTipoSanguineoDTO {

    private String tipoSanguineo;
    private Double mediaIdade;
}
