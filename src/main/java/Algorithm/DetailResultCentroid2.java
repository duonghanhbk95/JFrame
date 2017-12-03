/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Hanh Nguyen
 */
public class DetailResultCentroid2 extends javax.swing.JFrame {

    /**
     * Creates new form DetailResultCentroid2
     */
    Kmean k = new Kmean();
    
    private int indexRowLevel1;
    private TableModel MeaningModel;
    private TableModel FrequencyModel;
    private int MeaningItem;
    private int FreauencyItem;
    
    private int stt;
    private int coutElm;
    private String vectorCentroid;
    private double rate;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getCoutElm() {
        return coutElm;
    }

    public void setCoutElm(int coutElm) {
        this.coutElm = coutElm;
    }

    public String getVectorCentroid() {
        return vectorCentroid;
    }

    public void setVectorCentroid(String vectorCentroid) {
        this.vectorCentroid = vectorCentroid;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public DetailResultCentroid2(int stt, String vectorCentroid, int countELm, double rate) {
        this.stt = stt;
        this.vectorCentroid = vectorCentroid;
        this.coutElm = countELm;
        this.rate = rate;
    }
    private ArrayList<DetailResultCentroid2> dataListCentroidLevel2() {
        int count = 0;
        int total = 0;
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        whereVector.add("meaning_id", indexRowLevel1);

        ArrayList<DetailResultCentroid2> list = new ArrayList();
        DBCursor cursorCentroid = k.centroid.find(whereVector.get());
        
        total = k.vector.find(whereVector.get()).count();
        
        DetailResultCentroid2 detail;
        
        int numFre = k.vector.distinct("frequency_id").size();
        while (cursorCentroid.hasNext()) {
            DBObject obj = cursorCentroid.next();
            for (int i = 1; i <= numFre; i++) {
                whereVector.add("frequency_id", i);
                count = k.vector.find(whereVector.get()).count();
                detail = new DetailResultCentroid2(i, obj.get("frequency_centroid " + i).toString(), count, (double) (count * 100) / total);
                list.add(detail);
            }
        }

        return list;
    }
    private void showCentroidResult2(ArrayList<DetailResultCentroid2> dataListCentroid, TableModel tableName) {
        ArrayList<DetailResultCentroid2> list = dataListCentroid;
        DefaultTableModel model = (DefaultTableModel) tableName;
        
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getStt();
            row[1] = list.get(i).getVectorCentroid();
            row[2] = list.get(i).getCoutElm();
            row[3] = list.get(i).getRate();
            model.addRow(row);
        }

    }
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }
    
    public DetailResultCentroid2(int MeaningItem, TableModel MeaningModel, int FrequencyItem, TableModel FrequencyModel) {
        this.MeaningModel = MeaningModel;
        this.FrequencyModel = FrequencyModel;
        this.MeaningItem = MeaningItem;
        this.FreauencyItem = FrequencyItem;
        setLocation(100, 150);
        setResizable(false);
        initComponents();
        
        System.out.println("fre " + FrequencyItem);
        for(int i = 1; i <= MeaningItem; i++) {
            cbMeaningId.addItem("Meaning Centroid " + String.valueOf(i));
        }
        for(int j = 1; j <= FrequencyItem; j++) {
            cbFrequencyId.addItem("Frequency Centroid " + String.valueOf(j));
        }
    }
    public DetailResultCentroid2() {
        initComponents();
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtValueFrequency = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtValueMeaning = new javax.swing.JTextArea();
        cbFrequencyId = new javax.swing.JComboBox<>();
        cbMeaningId = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtValueFrequency.setColumns(20);
        txtValueFrequency.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        txtValueFrequency.setRows(5);
        jScrollPane1.setViewportView(txtValueFrequency);

        txtValueMeaning.setColumns(20);
        txtValueMeaning.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        txtValueMeaning.setRows(5);
        jScrollPane2.setViewportView(txtValueMeaning);

        cbFrequencyId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFrequencyIdActionPerformed(evt);
            }
        });

        cbMeaningId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMeaningIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbFrequencyId, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbMeaningId, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbMeaningId, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbFrequencyId, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void cbMeaningIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMeaningIdActionPerformed
        String meaning_value = (String) MeaningModel.getValueAt(cbMeaningId.getSelectedIndex(), 1);
        String jsonString = toPrettyFormat(meaning_value);
        txtValueMeaning.setText(jsonString);
        txtValueMeaning.setEditable(false);
        indexRowLevel1 = (int) MeaningModel.getValueAt(cbMeaningId.getSelectedIndex(), 0);
        DefaultTableModel modelCentroid2 =  (DefaultTableModel)FrequencyModel;
        
        modelCentroid2.setRowCount(0);
        showCentroidResult2(dataListCentroidLevel2(), FrequencyModel);
    }//GEN-LAST:event_cbMeaningIdActionPerformed

    private void cbFrequencyIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFrequencyIdActionPerformed
        
      
        String frequency_value = (String) FrequencyModel.getValueAt(cbFrequencyId.getSelectedIndex(), 1);
        String jsonMeaning = toPrettyFormat(frequency_value);
        txtValueFrequency.setText(jsonMeaning);
        txtValueFrequency.setEditable(false);
    }//GEN-LAST:event_cbFrequencyIdActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbFrequencyId;
    private javax.swing.JComboBox<String> cbMeaningId;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtValueFrequency;
    private javax.swing.JTextArea txtValueMeaning;
    // End of variables declaration//GEN-END:variables
}
