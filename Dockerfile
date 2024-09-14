FROM camunda/zeebe:8.3.0

# Copy your custom exporter JAR
COPY target/zeebe-exporter-demo-1.0.0.jar /usr/local/zeebe/lib/demo-exporter-1.0.0.jar

# Add exporter configuration
COPY exporters.json /usr/local/zeebe/config/exporters.json
