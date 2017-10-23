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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Hanh Nguyen
 */
public class JFrameResult1 extends javax.swing.JFrame {
    
    private final ConnectionDB connect = new ConnectionDB();
    private final DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
    private final DBCollection centroid = connect.connect(MyConstants.CENTROID_COLLECTION_NAME);
    
    private int id_model;
    private String value;
    private int meaning_id;

    private int stt;
    private int coutElm;
    private String vectorCentroid;
    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
    
    private int total;
    private int count;
    private int indexRow;

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
    public int getId_model() {
        return id_model;
    }

    public void setId_model(int id_model) {
        this.id_model = id_model;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMeaning_id() {
        return meaning_id;
    }

    public void setMeaning_id(int meaning_id) {
        this.meaning_id = meaning_id;
    }
   
    /**
     * Creates new form JFrameKmeans
     * @param id_model
     * @param value
     * @param meaning_id
     */
    public JFrameResult1(int id_model, int meaning_id, String value) {
        this.id_model = id_model;
        this.value = value;
        this.meaning_id = meaning_id;
    }
    public JFrameResult1(int stt, String vectorCentroid, int countELm, double rate) {
        this.stt = stt;
        this.vectorCentroid = vectorCentroid;
        this.coutElm = countELm;
        this.rate = rate;
    }
    public JFrameResult1() {
        initComponents();
        
//        showtableResult();
        showCentroidResult();
        showTextField();
    }
    
    public double percent() {
        double percent;
        
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        
        DBCursor cursorVector = vector.find();
        total = cursorVector.count();
        
        
        DBCursor cursorCentroid = centroid.find();
        while(cursorCentroid.hasNext()) {
            DBObject obj = cursorCentroid.next();
            whereVector.add("meaning_id", obj.get("meaning_id"));
            count = vector.find(whereVector.get()).count();
        }
        percent = (double)(count*100)/total;
         
        return percent;
    }
    
    public ArrayList<JFrameResult1> dataListValue() {
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        whereVector.add("meaning_id", indexRow);
        ArrayList<JFrameResult1> list = new ArrayList();
        DBCursor cursor = vector.find(whereVector.get());
        
        JFrameResult1 jframe;
        while(cursor.hasNext()) {
            DBObject obj = cursor.next();
            jframe = new JFrameResult1(Integer.parseInt(obj.get("id_model").toString()),Integer.parseInt(obj.get("meaning_id").toString()),obj.toString());
            list.add(jframe);
        }
        return list;
    }
    private void showtableDetail() {
        ArrayList<JFrameResult1> list = dataListValue();
        DefaultTableModel model = (DefaultTableModel) tableResult.getModel();
        model.setRowCount(0);
        Object[] row = new Object[3];
        for(int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_model();
            row[1] = list.get(i).getMeaning_id();
            row[2] = list.get(i).getValue();
            model.addRow(row);
        }
        
    }
    public ArrayList<JFrameResult1> dataListCentroid() {
        ArrayList<JFrameResult1> list = new ArrayList();
        DBCursor cursorCentroid = centroid.find();
        JFrameResult1 jframe;
        
        while(cursorCentroid.hasNext()) {
            DBObject obj = cursorCentroid.next();
            jframe = new JFrameResult1(Integer.parseInt(obj.get("meaning_id").toString()),obj.get("meaning_centroid").toString(),count, percent());
            list.add(jframe);
        }
        
        return list;
    }
     private void showCentroidResult() {
        ArrayList<JFrameResult1> list = dataListCentroid();
        DefaultTableModel model = (DefaultTableModel) tableCentroid.getModel();
        
        Object[] row = new Object[4];
        for(int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getStt();
            row[1] = list.get(i).getVectorCentroid();
            row[2] = list.get(i).getCoutElm();
            row[3] = list.get(i).getRate();
            model.addRow(row);
        }
        
    }
     private void showTextField() {
         totalModel.setText(String.valueOf(total));
         Iteration.setText(String.valueOf(Kmean.iteration));
         
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCentroid = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableResult = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        totalModel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        Iteration = new javax.swing.JTextField();
        countCluster = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("KẾT QUẢ PHÂN CỤM");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel2.setText("THÔNG SỐ CÁC CỤM");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel3.setText("CHI TIẾT CỤM");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel4.setText("CỤM :");

        tableCentroid.setModel(new javax.swing.table.DefaultTableModel(
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
        tableCentroid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCentroidMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCentroid);

        tableResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Model", "Meaning_id", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableResult);

        jButton1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton1.setText("Kết thúc");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel5.setText("Tổng số mô hình :");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel6.setText("Số lần lặp            :");

        countCluster.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalModel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Iteration, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(264, 264, 264)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(countCluster, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
            .addGroup(layout.createSequentialGroup()
                .addGap(397, 397, 397)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countCluster, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalModel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                            .addComponent(Iteration)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tableCentroidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCentroidMouseClicked
        int index = tableCentroid.getSelectedRow();
        TableModel model = tableCentroid.getModel();
        
        indexRow = (int) model.getValueAt(index, 0);
        countCluster.setText(String.valueOf(indexRow));
        showtableDetail();
    }//GEN-LAST:event_tableCentroidMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Iteration;
    private javax.swing.JTextField countCluster;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableCentroid;
    private javax.swing.JTable tableResult;
    private javax.swing.JTextField totalModel;
    // End of variables declaration//GEN-END:variables
}