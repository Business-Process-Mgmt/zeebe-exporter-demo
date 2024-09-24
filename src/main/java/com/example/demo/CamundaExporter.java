package com.example.demo;


import com.example.demo.dao.TaskDAO;
import com.example.demo.domain.ExporterTask;
import com.example.demo.service.RESTService;
import com.example.demo.service.ExporterFilter;
import io.camunda.operate.exception.OperateException;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.exporter.api.Exporter;
import io.camunda.zeebe.exporter.api.context.Context;
import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.protocol.record.Record;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.camunda.zeebe.protocol.record.ValueType;
import org.json.JSONObject;


public class CamundaExporter implements Exporter {

    Controller controller;
    Connection conn;

    public void configure(Context context) throws Exception {
       System.out.println("DemoExporter configure ");
        String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=camundadb;encrypt=true;trustServerCertificate=true";
        String username = "camundadb";
        String password = "camunda";
        context.setFilter(new ExporterFilter());

        try {

            conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void open(Controller controller) {
        System.out.println("DemoExporter opened");
        this.controller = controller;
    }

    public void close() {
        System.out.println("DemoExporter closed");
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public void export(Record<?> record) {
        System.out.println("Inside export record::" + record.toJson());

        this.controller.updateLastExportedRecordPosition(record.getPosition());

        ValueType valueType = record.getValueType();
        System.out.println("Inside export record valueType value::" + valueType.value());
        System.out.println("Inside export record valueType name::" + valueType.name());
        String recordValue=record.getValue().toJson();
        System.out.println("recordValue: "+recordValue+"\n");
        JSONObject recordJsonObject = new JSONObject(recordValue);
        String bpmnElementType=null;
        Long processInstanceKey=null;
        if(recordJsonObject!=null && recordJsonObject.has("bpmnElementType")) {
             bpmnElementType = recordJsonObject.getString("bpmnElementType");
             processInstanceKey= recordJsonObject.getLong("processInstanceKey");
        }
        System.out.println("bpmnElementType::: "+bpmnElementType+"\n");
        System.out.println("processInstanceKey::: "+processInstanceKey+"\n");
        try {

            if(bpmnElementType!=null && bpmnElementType.equals("USER_TASK")) {
               ExporterTask exporterTask= RESTService.getTaskInformation(processInstanceKey);
                TaskDAO.insertTask(conn,exporterTask);
            }
        } catch (TaskListException | OperateException e) {
            throw new RuntimeException(e);
        }
    }
}
