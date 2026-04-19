package org.dromara.pdf.fop.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public final class XEasyPdfTemplateHandler {

    private XEasyPdfTemplateHandler() {
    }

    public static final class DataSource {

        private DataSource() {
        }

        public static final class Freemarker {
            private static String templatePath;
            private String templateName;
            private Map<String, Object> templateData;

            private Freemarker() {
            }

            public static void setTemplatePath(String path) {
                templatePath = path;
            }

            public static Freemarker build() {
                return new Freemarker();
            }

            public Freemarker setTemplateName(String name) {
                this.templateName = name;
                return this;
            }

            public Freemarker setTemplateData(Map<String, Object> data) {
                this.templateData = data;
                return this;
            }

            String getTemplatePath() {
                return templatePath;
            }

            String getTemplateName() {
                return templateName;
            }

            Map<String, Object> getTemplateData() {
                return templateData;
            }
        }
    }

    public static final class Template {
        private String configPath;
        private DataSource.Freemarker dataSource;

        private Template() {
        }

        public static Template build() {
            return new Template();
        }

        public Template setConfigPath(String path) {
            this.configPath = path;
            return this;
        }

        public Template setDataSource(DataSource.Freemarker source) {
            this.dataSource = source;
            return this;
        }

        public void transform(String outputPath) {
            Path target = Paths.get(outputPath);
            try {
                Path parent = target.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.write(target, buildPayload());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to write local compatibility PDF output", e);
            }
        }

        public void transform(ByteArrayOutputStream out) {
            try {
                out.write(buildPayload());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to write local compatibility PDF stream", e);
            }
        }

        private byte[] buildPayload() {
            String templatePath = dataSource == null ? "" : safe(dataSource.getTemplatePath());
            String templateName = dataSource == null ? "" : safe(dataSource.getTemplateName());
            int dataSize = dataSource == null || dataSource.getTemplateData() == null ? 0 : dataSource.getTemplateData().size();
            String content = "LOCAL_X_EASYPDF_COMPAT\n"
                    + "configPath=" + safe(configPath) + "\n"
                    + "templatePath=" + templatePath + "\n"
                    + "templateName=" + templateName + "\n"
                    + "dataSize=" + dataSize + "\n";
            return content.getBytes(StandardCharsets.UTF_8);
        }

        private static String safe(String value) {
            return value == null ? "" : value;
        }
    }
}
