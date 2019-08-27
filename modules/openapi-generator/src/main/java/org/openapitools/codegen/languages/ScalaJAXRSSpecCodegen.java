package org.openapitools.codegen.languages;

import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.GeneratorMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScalaJAXRSSpecCodegen extends AbstractScalaCodegen {

    public static final String USE_SWAGGER_ANNOTATIONS = "useSwaggerAnnotations";
    public static final String USE_BEAN_VALIDATION = "useBeanValidation";
    private boolean useSwaggerAnnotations = true;
    private boolean useBeanValidation = true;

    public ScalaJAXRSSpecCodegen() {
        this("generated-code/scala-jaxrs-akka-http-spec");
    }

    public ScalaJAXRSSpecCodegen(String baseOutputFolder) {
        super();
        generatorMetadata = GeneratorMetadata.newBuilder(generatorMetadata).build();

        apiPackage = "org.openapitools.spec.api";
        modelPackage = "org.openapitools.spec.model";
        invokerPackage = apiPackage;

        outputFolder = baseOutputFolder;
        embeddedTemplateDir = templateDir = "";

        apiTemplateFiles.put("apiSpec.mustache", "Spec.scala");
        modelTemplateFiles.put("model.mustache", ".scala");

        supportingFiles.add(new SupportingFile("build.sbt.mustache", "", "build.sbt"));

        cliOptions.add(CliOption.newBoolean(USE_SWAGGER_ANNOTATIONS, "Whether to generate Swagger annotations.", useSwaggerAnnotations));
        cliOptions.add(CliOption.newBoolean(USE_BEAN_VALIDATION, "Whether to generate Bean validation annotations.", useBeanValidation));

    }

    @Override
    public void processOpts() {
        super.processOpts();

        additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);

        supportingFiles.add( new SupportingFile("openApiSpec.mustache",
                (sourceFolder + '/' + invokerPackage).replace(".", "/"), "OpenApiSpec.scala"));

        if (additionalProperties.containsKey(USE_SWAGGER_ANNOTATIONS)) {
            useSwaggerAnnotations = Boolean.valueOf(additionalProperties.get(USE_SWAGGER_ANNOTATIONS).toString());
        }
        writePropertyBack(USE_SWAGGER_ANNOTATIONS, useSwaggerAnnotations);

        if (additionalProperties.containsKey(USE_BEAN_VALIDATION)) {
            useBeanValidation = Boolean.valueOf(additionalProperties.get(USE_BEAN_VALIDATION).toString());
        }
        writePropertyBack(USE_BEAN_VALIDATION, useBeanValidation);

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

                boolean isMultipartPost = false;
                List<Map<String, String>> consumes = operation.consumes;
                if (consumes != null) {
                    for (Map<String, String> consume : consumes) {
                        String mt = consume.get("mediaType");
                        if (mt != null) {
                            if (mt.startsWith("multipart/form-data")) {
                                isMultipartPost = true;
                            }
                        }
                    }
                }

                operation.vendorExtensions.put("x-multipart","true");

                for (CodegenParameter parameter : operation.allParams) {
                    if (isMultipartPost) {
                        parameter.vendorExtensions.put("x-multipart", "true");
                    }
                }

                List<CodegenResponse> responses = operation.responses;
                if (responses != null) {
                    for (CodegenResponse resp : responses) {
                        if (resp.code.equals("0")) {
                            resp.code = "default";
                        }
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
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        //FIXME example returns "null" instead of null
        for (CodegenProperty _var: model.vars) {
            _var.example = "null".equals(_var.example) ? null : _var.example ;
        }

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

    @Override
    public Map<String, Object> postProcessModelsEnum(Map<String, Object> objs) {
        objs = super.postProcessModelsEnum(objs);

        //Add imports for Jackson
        List<Map<String, String>> imports = (List<Map<String, String>>) objs.get("imports");
        List<Object> models = (List<Object>) objs.get("models");
        for (Object _mo : models) {
            Map<String, Object> mo = (Map<String, Object>) _mo;
            CodegenModel cm = (CodegenModel) mo.get("model");
            // for enum model
            for (CodegenProperty _var: cm.vars) {
                _var.example = "test";
            }
          /*  if (Boolean.TRUE.equals(cm.isEnum) && cm.allowableValues != null) {
                cm.imports.add(importMapping.get("JsonValue"));
                Map<String, String> item = new HashMap<String, String>();
                item.put("import", importMapping.get("JsonValue"));
                imports.add(item);
            }*/
        }

        return objs;
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SCHEMA;
    }

    @Override
    public String getName() {
        return "scala-jaxrs-spec";
    }

    @Override
    public String getHelp() {
        return "Generates a Scala JAX-RS Specification based api interface";
    }

}
