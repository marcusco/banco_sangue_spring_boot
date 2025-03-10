package br.wk.projeto.bancosangue.service;

import br.wk.projeto.bancosangue.dto.ImcFaixaPorIdadeDTO;
import br.wk.projeto.bancosangue.dto.ObesosPorSexoDTO;
import br.wk.projeto.bancosangue.model.Doador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImcService {

    private final DoadorService doadorService;

    public Double calcularImc(int peso, Double altura) {
        return peso / (altura * altura);
    }

    public Integer calcularIdade(LocalDate dataNascimento) {
        return LocalDate.now().getYear() - dataNascimento.getYear();
    }

    public List<ImcFaixaPorIdadeDTO> calcularPorFaixaEtaria() {
        final var listaFaixaPorIdade = new ArrayList<ImcFaixaPorIdadeDTO>();
        Map<String, double[]> faixaEtariaIMC = new HashMap<>();
        //
        final var doadores = doadorService.todos();
        for (Doador doador : doadores) {
            final var idade = calcularIdade(doador.getDataNascimento());
            String faixa = faixaEtaria(idade);

            faixaEtariaIMC.putIfAbsent(faixa, new double[]{0, 0});

            double[] valores = faixaEtariaIMC.get(faixa);
            valores[0] += calcularImc(doador.getPeso(), doador.getAltura()); // Soma dos IMCs
            valores[1]++;

        }
        //
        for (Map.Entry<String, double[]> entry : faixaEtariaIMC.entrySet()) {
            String faixa = entry.getKey();
            double somaIMC = entry.getValue()[0];
            double contador = entry.getValue()[1];
            double mediaIMC = somaIMC / contador;
            //
            listaFaixaPorIdade.add(ImcFaixaPorIdadeDTO
                    .builder()
                    .faixa(faixa)
                    .somaImc(somaIMC)
                    .mediaImc(mediaIMC)
                    .quantidade(contador)
                    .build());
        }
        return listaFaixaPorIdade;
    }

    private String faixaEtaria(int idade) {
        int faixaInferior = (idade / 10) * 10;
        int faixaSuperior = faixaInferior + 9;
        return faixaInferior + " a " + faixaSuperior;
    }

    public ObesosPorSexoDTO calcularPercentualObesosPorSexo() {
        final var doadores = doadorService.todos();
        int totalHomens = 0;
        int totalMulheres = 0;
        int obesosHomens = 0;
        int obesosMulheres = 0;

        for (Doador doador : doadores) {
            final var imc = calcularImc(doador.getPeso(), doador.getAltura());
            if (doador.getSexo().equalsIgnoreCase("Masculino")) {
                totalHomens++;
                if (imc > 30) {
                    obesosHomens++;
                }
            } else if (doador.getSexo().equalsIgnoreCase("Feminino")) {
                totalMulheres++;
                if (imc > 30) {
                    obesosMulheres++;
                }
            }
        }
        double percentualObesosHomens = (totalHomens > 0) ? (obesosHomens * 100.0 / totalHomens) : 0.0;
        double percentualObesosMulheres = (totalMulheres > 0) ? (obesosMulheres * 100.0 / totalMulheres) : 0.0;
        //
        BigDecimal bdM = BigDecimal.valueOf(percentualObesosHomens);
        bdM = bdM.setScale(2, RoundingMode.DOWN);
        BigDecimal bdF = BigDecimal.valueOf(percentualObesosMulheres);
        bdF = bdF.setScale(2, RoundingMode.DOWN);
        return ObesosPorSexoDTO.
                builder()
                .percentualFeminino(bdM.doubleValue())
                .percentualMasculino(bdF.doubleValue())
                .build();


    }



}
