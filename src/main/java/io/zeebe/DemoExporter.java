package io.zeebe;

import io.zeebe.exporter.api.context.Context;
import io.zeebe.exporter.api.context.Controller;
import io.zeebe.exporter.api.Exporter;
import io.zeebe.protocol.record.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoExporter implements Exporter {
    private static final Logger logger = LoggerFactory.getLogger(DemoExporter.class);

    Controller controller;

    public void configure(Context context) throws Exception {
    }

    public void open(Controller controller) {
        this.controller = controller;
    }

    public void close() {
    }

    public void export(Record record) {
        logger.info("Inside export ::"+record.toJson());
        this.controller.updateLastExportedRecordPosition(record.getPosition());
    }
}
