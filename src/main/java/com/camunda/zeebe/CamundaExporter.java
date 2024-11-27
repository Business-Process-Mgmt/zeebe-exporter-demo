package com.camunda.zeebe;

import io.camunda.zeebe.exporter.api.Exporter;
import io.camunda.zeebe.exporter.api.context.Context;
import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.protocol.record.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class CamundaExporter implements Exporter {
    private static final Logger logger = LoggerFactory.getLogger(CamundaExporter.class);
    Controller controller;
    @Override
    public void configure(Context context) throws Exception {
        Exporter.super.configure(context);
    }

    @Override
    public void open(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void close() {
        Exporter.super.close();
    }

    @Override
    public void export(Record<?> record) {
        logger.info("Inside export record::" + record.toJson());
        this.controller.updateLastExportedRecordPosition(record.getPosition());
    }
}
