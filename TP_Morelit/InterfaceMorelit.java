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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * this class is the graphic interface for Morelit application
 * @author vitorcezar
 */
public class InterfaceMorelit extends JFrame {

    /**
     * buttons' panel
     */
    private JPanel buttonPanel;
    
    /**
     * button that exports the data to a file
     */
    private JButton exportarDadosButton;
    
    /**
     * button that reads the data from Morelit
     */
    private JButton lerDadosButton;
    
    /**
     * scroll panel for the table
     */
    private JScrollPane scrollTablePanel;
    
    /**
     * table where the data will be shown
     */
    private JTable tabelaDados;
    
    /**
     * panel for the table
     */
    private JPanel tablePanel;
    
    /**
     * button to take photos from the node
     */
    private JButton tirarFotoButton;
    
    /**
     * the script that will be executed to get data from Morelit server
     */
    private static final String SCRIPT_LER_DADOS = "./scripts/getInterfaceData.sh 1";
    
    /**
     * the script that will be executed to get the photo from Morelit server
     */
    private static final String SCRIPT_LER_FOTO = "./scripts/getPhotos.sh";

    /**
     * the script that will be executed to show temperature's and luminosity's values
     */
    private static final String SCRIPT_GERAR_GRAFICOS = "Rscript rprogram.R";
    
    /**
     * the path of the file that contains the table information
     */
    private static final String PATH_ARQUIVO_LIDO = "datacsv.txt";
    
    /**
     * the path of the photo that will be read from the server
     */
    private static final String PATH_FOTO_LIDA = "./scripts/photo.jpg";
    
    /**
     * the path of the file that will have the table data exported
     */
    private static final String PATH_FILE_SAIDA = "exportedData.csv";
    
    /**
     * the photo width
     */
    private static final int LARGURA_FOTO = 640;
    
    /**
     * the photo height
     */
    private static final int ALTURA_FOTO = 360;
    
    /**
     * initializes the graphic interface
     */
    public InterfaceMorelit() {
        super( "Morelit" );
        initComponents();
        initEventListeners();
    }
    
    /**
     * displays the interface for the user
     */
    public void mostreInterface() {
        this.setVisible( true );
    }
    
    /**
     * generates a csv file corresponding to the table's data
     */
    private void generateDataFile() {
        try {
            File file = new File( PATH_FILE_SAIDA );
            PrintWriter printWriter = new PrintWriter( file );
            
            printWriter.println( "Endereco,Tipo_Dado,Data,Hora,Valor" );
            for( int i = 0; i < tabelaDados.getRowCount(); i++ ) {
                for( int j = 0; j < tabelaDados.getColumnCount(); j++ ) {
                    if( j == tabelaDados.getColumnCount() - 1 ) {
                        printWriter.print( "\"" );
                        printWriter.print( tabelaDados.getValueAt( i, j ) );
                        printWriter.println( "\"" );
                    } else {
                        printWriter.print( "\"" );
                        printWriter.print( tabelaDados.getValueAt( i, j ) );
                        printWriter.print( "\"," );
                    }                    
                }
            }
            printWriter.close();

            new Thread( () -> {
                try{
                    Process process;
                    process = Runtime.getRuntime().exec( SCRIPT_GERAR_GRAFICOS );
                    process.waitFor();
                } catch( Exception exp ) {
                    exp.printStackTrace();
                }
            }).start();

        } catch( FileNotFoundException e ) {
            // does nothing
        }
    }
    
    /**
     * initializes the listeners
     */
    private void initEventListeners() {
        // exports the table's data as csv
        exportarDadosButton.addActionListener( ( e ) -> {
            generateDataFile();
        } );
        // gets the data from the server and exhibits it on the table
        lerDadosButton.addActionListener( ( e ) -> {
            try {
                new Thread( () -> {
                    try{
                        Process process;
                        process = Runtime.getRuntime().exec( SCRIPT_LER_DADOS );
                        process.waitFor();
                        
                        // reads the data and puts them on the table
                        Scanner scanner = new Scanner( new File( PATH_ARQUIVO_LIDO ) );
                        DefaultTableModel tableModel = 
                            (DefaultTableModel) tabelaDados.getModel();
                        while( scanner.hasNext() ) {
                            String line = scanner.nextLine();
                            line = line.replaceAll( "\"", "" );
                            String[] dataLine = line.split( "," );
                            tableModel.addRow( dataLine );
                        }
                    } catch( Exception exp ) {
                        exp.printStackTrace();
                    }
                }).start();
            } catch( Exception exp ) {
            }
        } );
        // gets a photo from the server and exhibits on this program
        tirarFotoButton.addActionListener( ( e ) -> {
            try {
                new Thread( () -> {
                    try{
                        Process process;
                        process = Runtime.getRuntime().exec( SCRIPT_LER_FOTO );
                        process.waitFor();

                        PainelFoto painelFoto = new PainelFoto( PATH_FOTO_LIDA );
                        JFrame fotoFrame = new JFrame();
                        fotoFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                        fotoFrame.add( painelFoto );
                        fotoFrame.setSize( LARGURA_FOTO, ALTURA_FOTO );
                        fotoFrame.setVisible( true );
                    } catch( Exception exp ) {
                        exp.printStackTrace();
                    }
                }).start();
            } catch( Exception exp ) {
            }
        } );
    }

    /**
     * this method is called to initialize the graphic interface
     */                      
    private void initComponents() {
        buttonPanel = new javax.swing.JPanel();
        lerDadosButton = new javax.swing.JButton();
        tirarFotoButton = new javax.swing.JButton();
        exportarDadosButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        scrollTablePanel = new javax.swing.JScrollPane();
        tabelaDados = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lerDadosButton.setText("Ler dados");
        lerDadosButton.setFocusPainted(false);

        tirarFotoButton.setText("Tirar foto");
        tirarFotoButton.setFocusPainted(false);

        exportarDadosButton.setText("Exportar dados");
        exportarDadosButton.setFocusPainted(false);

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lerDadosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tirarFotoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94)
                .addComponent(exportarDadosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lerDadosButton)
                    .addComponent(tirarFotoButton)
                    .addComponent(exportarDadosButton))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        tabelaDados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Endereco", "Tipo de Dado", "Data", "Hora", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaDados.getTableHeader().setReorderingAllowed(false);
        scrollTablePanel.setViewportView(tabelaDados);

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollTablePanel)
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addComponent(scrollTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tablePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }                
}
