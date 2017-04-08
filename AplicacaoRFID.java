/*
 * Copyright (C) 2017 vitorcezar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author vitorcezar
 */
public class AplicacaoRFID {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InterfaceGrafica interfaceGrafica = new InterfaceGrafica();

        /* APENAS UM TESTE MOSTRANDO A UTILIZAÇÃO. AQUI ESTÁ SENDO CHAMADA A FUNÇÃO
        QUE EXIBIRÁ A INTERFACE GRÁFICA. NOTE QUE QUANDO O 'X' DA INTERFACE É CLICADO O
        PROGRAMA FECHA */
        interfaceGrafica.mostreInterfaceGrafica();
        
        /* AQUI NOVAS INFORMAÇÕES ESTÃO SENDO ADICIONADAS. CADA INFORMAÇÃO É CORRESPONDENTE
        A UMA ETIQUETA. NOTE QUE APÓS FAZER ISSO, HAVERÁ 'INFINITY' EM UMA DAS COLUNAS, ISSO
        É NORMAL POIS O TEMPO INTERNO DA INTERFACE ESTÁ IGUAL A 0. ISSO NÃO IRÁ OCORRER
        QUANDO O PROGRAMA ESTIVER PRONTO, EM QUE AS ETIQUETAS SERÃO ADICIONADAS ENQUANTO O
        TEMPO ESTIVER SENDO INCREMENTADO */
        for( int i = 20; i >= 0; i-- ) {
            interfaceGrafica.adicionaNovaInformacao( "ID" + i, "IP" + ( 40 - i ) );
        }
        
        /* USADO APENAS PARA INCREMENTAR A QUANTIDADE DE LEITURA DA ETIQUETA COM ID2 */
        interfaceGrafica.ultimaLeituraInformacao( "ID2" );
    }
}
