package com.example.demo;


import io.camunda.zeebe.exporter.api.Exporter;
import io.camunda.zeebe.exporter.api.context.Context;
import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.protocol.record.Record;


public class DemoExporter implements Exporter {
    Controller controller;

    public void configure(Context context) throws Exception {
       System.out.println("DemoExporter configure ");
    }

    public void open(Controller controller) {
        System.out.println("DemoExporter opened");
        this.controller = controller;
    }

    public void close() {
        System.out.println("DemoExporter closed");
    }



    public void export(Record<?> record) {
        System.out.println("Inside export ::"+record.toJson());
        this.controller.updateLastExportedRecordPosition(record.getPosition());
    }
}
