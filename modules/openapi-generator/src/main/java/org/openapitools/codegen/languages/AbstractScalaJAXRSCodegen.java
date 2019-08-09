package org.openapitools.codegen.languages;

import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.meta.GeneratorMetadata;

import java.io.File;
import java.util.List;

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

        if (additionalProperties.containsKey(USE_SWAGGER_ANNOTATIONS)) {
            useSwaggerAnnotations = Boolean.valueOf(additionalProperties.get(USE_SWAGGER_ANNOTATIONS).toString());
        }
        writePropertyBack(USE_SWAGGER_ANNOTATIONS, useSwaggerAnnotations);

    }

}
