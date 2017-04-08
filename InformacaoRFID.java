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
 * Esta classe terá como finalidade armazenar as informações de uma etiqueta 
 * RFID
 * @author vitorcezar
 */
public class InformacaoRFID {
    
    /**
     * identificador da etiqueta RFID
     */
    private final String id;
    
    /**
     * endereço IP da etiqueta RFID, armazenado como string
     */
    private final String ip;
    
    /**
     * tempo quando ocorreu a primeira leitura da etiqueta
     */
    private final double tempoPrimeiraLeitura;
    
    /**
     * tempo quando ocorreu a última leitura da etiqueta
     */
    private double tempoUltimaLeitura;
    
    /**
     * quantas vezes a etiqueta foi lida
     */
    private int quantidadeLeitura;
    
    /**
     * @param id identificação da etiqueta
     * @param ip endereço IP da etiqueta
     * @param tempoPrimeiraLeitura tempo quando ocorreu a primeira leitura
     */
    public InformacaoRFID( String id, String ip, double tempoPrimeiraLeitura ) {
        this.id = id;
        this.ip = ip;
        this.tempoPrimeiraLeitura = tempoPrimeiraLeitura;
        this.tempoUltimaLeitura = tempoPrimeiraLeitura;
        quantidadeLeitura = 1;
    }
    
    /**
     * essa função deve ser invocada toda vez que a etiqueta equivalente for
     * lida, passando o tempo de leitura como parâmetro
     * @param tempoUltimaLeitura tempo da última leitura da etiqueta
     */
    public void defineUltimaLeitura( double tempoUltimaLeitura ) {
        this.tempoUltimaLeitura = tempoUltimaLeitura;
        quantidadeLeitura++;
    }
    
    /**
     * @return identificação da etiqueta
     */
    public String retornaId() {
        return id;
    }
    
    /**
     * @return endereço IP da etiqueta
     */
    public String retornaIp() {
        return ip;
    }
    
    /**
     * @return tempo quando ocorreu a primeira leitura
     */
    public double retornaTempoPrimeiraLeitura() {
        return tempoPrimeiraLeitura;
    }
    
    /**
     * @return tempo quando ocorreu a última leitura
     */
    public double retornaTempoUltimaLeitura() {
        return tempoUltimaLeitura;
    }
    
    /**
     * @return quantidade de vezes em que a etiqueta foi lida
     */
    public int retornaQuantidadeLeitura() {
        return quantidadeLeitura;
    }
}
