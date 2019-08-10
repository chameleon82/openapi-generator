package org.openapitools.codegen.languages;

import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.GeneratorMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractScalaJAXRSCodegen extends AbstractScalaCodegen {

    public static final String USE_SWAGGER_ANNOTATIONS = "useSwaggerAnnotations";
    private boolean useSwaggerAnnotations = true;

    public AbstractScalaJAXRSCodegen() {
        this("generated-code/scala-jaxrs-akka-http-spec");
    }

    public AbstractScalaJAXRSCodegen(String baseOutputFolder) {
        super();
        generatorMetadata = GeneratorMetadata.newBuilder(generatorMetadata).build();

        outputFolder = baseOutputFolder;
        embeddedTemplateDir = templateDir = "scala-jaxrs";

        apiTemplateFiles.put("spec/apiSpec.mustache", "Spec.scala");
        modelTemplateFiles.put("spec/model.mustache", ".scala");

        cliOptions.add(CliOption.newBoolean(USE_SWAGGER_ANNOTATIONS, "Whether to generate Swagger annotations.", useSwaggerAnnotations));

    }

    @Override
    public void processOpts() {
        super.processOpts();

        additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);

        supportingFiles.add( new SupportingFile("spec/openApiSpec.mustache",
                (sourceFolder + '/' + invokerPackage).replace(".", "/"), "OpenApiSpec.scala"));

        if (additionalProperties.containsKey(USE_SWAGGER_ANNOTATIONS)) {
            useSwaggerAnnotations = Boolean.valueOf(additionalProperties.get(USE_SWAGGER_ANNOTATIONS).toString());
        }
        writePropertyBack(USE_SWAGGER_ANNOTATIONS, useSwaggerAnnotations);

    }

    @Override
    public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {
        super.postProcessOperationsWithModels(objs, allModels);

        @SuppressWarnings("unchecked")
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        if (operations != null) {
            String commonBaseName = null;
            boolean baseNameEquals = true;
            @SuppressWarnings("unchecked")
            List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
            for (CodegenOperation operation : ops) {

                List<CodegenResponse> responses = operation.responses;
                if (responses != null) {
                    for (CodegenResponse resp : responses) {
                        if ("array".equals(resp.containerType)) {
                            resp.containerType = "List";
                        } else if ("map".equals(resp.containerType)) {
                            resp.containerType = "Map";
                        }
                    }
                }

                if (operation.returnBaseType == null) {
                    operation.returnType = "Unit";
                    operation.returnBaseType = "Unit";
                    operation.vendorExtensions.put("x-scala-is-response-unit", true);
                }
            }
        }
        return objs;
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        String basePath = resourcePath;
        if (basePath.startsWith("/")) {
            basePath = basePath.substring(1);
        }
        int pos = basePath.indexOf("/");
        if (pos > 0) {
            basePath = basePath.substring(0, pos);
        }

        if (StringUtils.isEmpty(basePath)) {
            basePath = "default";
        } else {
            if (co.path.startsWith("/" + basePath)) {
                co.path = co.path.substring(("/" + basePath).length());
            }
            co.subresourceOperation = !co.path.isEmpty();
        }
        List<CodegenOperation> opList = operations.get(basePath);
        if (opList == null || opList.isEmpty()) {
            opList = new ArrayList<CodegenOperation>();
            operations.put(basePath, opList);
        }
        opList.add(co);
        co.baseName = basePath;
    }


}
