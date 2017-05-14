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
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author vitorcezar
 */
public class TabelaInformacoes extends JFrame {
    
    /**
     * tabela que será utilizada para exibir informações
     */
    private JTable tabelaInformacoes;
    
    /**
     * construtor da tabela de informações
     */
    public TabelaInformacoes() {
        inicializaComponentes();
    }
    
    /**
     * mostra a tabela com informações de temperatura e luminosidade
     */
    public void mostrarInformacoesTabela() {
        this.setVisible( true );
    }

    /**
     * código gerado pelo NetBeans
     */                     
    private void inicializaComponentes() {

        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        tabelaInformacoes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE );

        tabelaInformacoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Identificador", "Temperatura", "Luminosidade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaInformacoes.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tabelaInformacoes);
        if (tabelaInformacoes.getColumnModel().getColumnCount() > 0) {
            tabelaInformacoes.getColumnModel().getColumn(0).setResizable(false);
            tabelaInformacoes.getColumnModel().getColumn(1).setResizable(false);
            tabelaInformacoes.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }
}
