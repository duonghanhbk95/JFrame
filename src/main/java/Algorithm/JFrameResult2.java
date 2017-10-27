/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
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
public class JFrameResult2 extends javax.swing.JFrame {

    Kmean k = new Kmean();

    private int totalModel;
    private int indexRowLevel1;
    private int indexRowLevel2;

    private int id_model;
    private int meaning_id;
    private int frequency_id;
    private String value;
    
    //constructor for tableDetail
    public JFrameResult2(int id_model, int meaning_id, int frequency_id, String value) {
        this.id_model = id_model;
        this.meaning_id = meaning_id;
        this.frequency_id = frequency_id;
        this.value = value;
    }
    public int getId_model() {
        return id_model;
    }

    public void setId_model(int id_model) {
        this.id_model = id_model;
    }

    public int getMeaning_id() {
        return meaning_id;
    }

    public void setMeaning_id(int meaning_id) {
        this.meaning_id = meaning_id;
    }

    public int getFrequency_id() {
        return frequency_id;
    }

    public void setFrequency_id(int frequency_id) {
        this.frequency_id = frequency_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
    private int stt;
    private int coutElm;
    private String vectorCentroid;
    private double rate;

    //constructor for tableCentroidLevel 2
    public JFrameResult2(int stt, String vectorCentroid, int countELm, double rate) {
        this.stt = stt;
        this.vectorCentroid = vectorCentroid;
        this.coutElm = countELm;
        this.rate = rate;
    }

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

    //constructor for tableCentroidLevel2
    /**
     * Creates new form JFrameResult2
     */
    public JFrameResult2() {
        setLocation(120, 100);
        setResizable(false);
        initComponents();
        totalModel();
        showCentroidResult2(dataListCentroidLevel1(), tableCentroidlevel1);
        showTextField2();
    }

    private void totalModel() {
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        DBCursor cursor1 = k.vector.find();
        totalModel = cursor1.count();
        
        
    }

    private ArrayList<JFrameResult2> dataListCentroidLevel1() {
        int count = 0;
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        ArrayList<JFrameResult2> list = new ArrayList();
        DBCursor cursorCentroid = k.centroid.find();
        JFrameResult2 jframe;

        while (cursorCentroid.hasNext()) {
            DBObject obj = cursorCentroid.next();
            whereVector.add("meaning_id", obj.get("meaning_id"));
            count = k.vector.find(whereVector.get()).count();

            jframe = new JFrameResult2(Integer.parseInt(obj.get("meaning_id").toString()), obj.get("meaning_centroid").toString(), count, (double) (count * 100) / totalModel);
            list.add(jframe);
        }

        return list;
    }

    private ArrayList<JFrameResult2> dataListCentroidLevel2() {
        int count = 0;
        int total = 0;
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        whereVector.add("meaning_id", indexRowLevel1);

        ArrayList<JFrameResult2> list = new ArrayList();
        DBCursor cursorCentroid = k.centroid.find(whereVector.get());
        
        total = k.vector.find(whereVector.get()).count();
        
        JFrameResult2 jframe;
        
        int numFre = k.vector.distinct("frequency_id").size();
        while (cursorCentroid.hasNext()) {
            DBObject obj = cursorCentroid.next();
            for (int i = 1; i <= numFre; i++) {
                whereVector.add("frequency_id", i);
                count = k.vector.find(whereVector.get()).count();
                jframe = new JFrameResult2(i, obj.get("frequency_centroid " + i).toString(), count, (double) (count * 100) / total);
                list.add(jframe);
            }
        }

        return list;
    }

    private void showCentroidResult2(ArrayList<JFrameResult2> dataListCentroid, JTable tableName) {
        ArrayList<JFrameResult2> list = dataListCentroid;
        DefaultTableModel model = (DefaultTableModel) tableName.getModel();
        
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getStt();
            row[1] = list.get(i).getVectorCentroid();
            row[2] = list.get(i).getCoutElm();
            row[3] = list.get(i).getRate();
            model.addRow(row);
        }

    }

    private void showTextField2() {
        totalModel2.setText(String.valueOf(totalModel));
        totalModel2.setEditable(false);
        Iteration2.setText(String.valueOf(FrequencyKMean.iterationFrequency + Kmean.iterationMeaning));
        Iteration2.setEditable(false);
        countClusterLevel1.setEditable(false);
        countClusterLevel2.setEditable(false);
    }
    
    public ArrayList<JFrameResult2> dataListValue() {
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        whereVector.add("meaning_id", indexRowLevel1);
        whereVector.add("frequency_id", indexRowLevel2);
        ArrayList<JFrameResult2> list = new ArrayList();
        DBCursor cursor = k.cluster.find(whereVector.get());

        JFrameResult2 jframe;
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            jframe = new JFrameResult2(Integer.parseInt(obj.get("id_model").toString()), Integer.parseInt(obj.get("meaning_id").toString()), Integer.parseInt(obj.get("frequency_id").toString()),obj.toString());
            list.add(jframe);
        }
        return list;
    }

    private void showtableDetail() {
        ArrayList<JFrameResult2> list = dataListValue();
        DefaultTableModel model = (DefaultTableModel) tableDetaillevel.getModel();
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_model();
            row[1] = list.get(i).getMeaning_id();
            row[2] = list.get(i).getFrequency_id();
            row[3] = list.get(i).getValue();
            model.addRow(row);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCentroidlevel1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDetaillevel = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableCentroidlevel2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        countClusterLevel2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        countClusterLevel1 = new javax.swing.JTextField();
        btnfinish2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        totalModel2 = new javax.swing.JTextField();
        Iteration2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("THÔNG SỐ CÁC CỤM MỨC 1");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("KẾT QUẢ PHÂN CỤM");

        tableCentroidlevel1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TT Cụm", "Tâm Cụm", "Số PT", "Tỷ lệ(%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCentroidlevel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCentroidlevel1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCentroidlevel1);

        tableDetaillevel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Model", "Meaning_id", "Frequency_id", "Value"
            }
        ));
        jScrollPane2.setViewportView(tableDetaillevel);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("CHI TIẾT CỤM");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("THÔNG SỐ CÁC CỤM MỨC 2");

        tableCentroidlevel2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TT Cụm", "Tâm Cụm", "Số PT", "Tỷ lệ(%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCentroidlevel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCentroidlevel2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableCentroidlevel2);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("CỤM MỨC 1");

        countClusterLevel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countClusterLevel2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("CỤM MỨC 2");

        countClusterLevel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countClusterLevel1ActionPerformed(evt);
            }
        });

        btnfinish2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnfinish2.setText("Kết Thúc");
        btnfinish2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnfinish2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfinish2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel7.setText("Tổng số mô hình       :");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel8.setText("Số lần lặp                  :");

        totalModel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        totalModel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Iteration2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Iteration2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(415, 415, 415)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnfinish2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(119, 119, 119)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(totalModel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Iteration2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(61, 61, 61)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countClusterLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countClusterLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(countClusterLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(countClusterLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnfinish2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalModel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Iteration2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void countClusterLevel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countClusterLevel2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countClusterLevel2ActionPerformed

    private void countClusterLevel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countClusterLevel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countClusterLevel1ActionPerformed

    private void tableCentroidlevel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCentroidlevel1MouseClicked
        int index = tableCentroidlevel1.getSelectedRow();
        TableModel model = tableCentroidlevel1.getModel();
        indexRowLevel1 = (int) model.getValueAt(index, 0);
        countClusterLevel1.setText(String.valueOf(indexRowLevel1));
        
        
        DefaultTableModel modelCentroid2 = (DefaultTableModel) tableCentroidlevel2.getModel();
        
        modelCentroid2.setRowCount(0);
        showCentroidResult2(dataListCentroidLevel2(), tableCentroidlevel2);
    }//GEN-LAST:event_tableCentroidlevel1MouseClicked

    private void tableCentroidlevel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCentroidlevel2MouseClicked
        int index = tableCentroidlevel2.getSelectedRow();
        TableModel model = tableCentroidlevel2.getModel();
        indexRowLevel2 = (int) model.getValueAt(index, 0);
        showtableDetail();
        countClusterLevel2.setText(String.valueOf(indexRowLevel2));
        
    }//GEN-LAST:event_tableCentroidlevel2MouseClicked

    private void btnfinish2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfinish2ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnfinish2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Iteration2;
    private javax.swing.JButton btnfinish2;
    private javax.swing.JTextField countClusterLevel1;
    private javax.swing.JTextField countClusterLevel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableCentroidlevel1;
    private javax.swing.JTable tableCentroidlevel2;
    private javax.swing.JTable tableDetaillevel;
    private javax.swing.JTextField totalModel2;
    // End of variables declaration//GEN-END:variables
}
