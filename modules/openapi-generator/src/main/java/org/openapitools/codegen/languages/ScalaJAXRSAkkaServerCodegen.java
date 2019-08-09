package org.openapitools.codegen.languages;

import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.SupportingFile;

import java.io.File;

public class ScalaJAXRSAkkaServerCodegen extends AbstractScalaJAXRSCodegen {

    /**
     * Entrypoint to generate Scala akka-http JAX-RS specification based server
     */

    public ScalaJAXRSAkkaServerCodegen() {
        super("generated-code/akka-http-server");

        //invokerPackage = ""
        apiPackage = "org.openapitools.server.api";
        modelPackage = "org.openapitools.server.model";

        invokerPackage = apiPackage;

        embeddedTemplateDir = templateDir = "scala-jaxrs";

      //  apiTemplateFiles.put("akka-http-server/api.mustache", ".scala");

        String apiPackageFolder = sourceFolder + File.separator + apiPackage().replace('.', File.separatorChar);

        supportingFiles.add(new SupportingFile("akka-http-server/build.sbt.mustache", "", "build.sbt"));
        supportingFiles.add(new SupportingFile("akka-http-server/abstractApi.mustache", apiPackageFolder, "AbstractApi.scala"));
        supportingFiles.add(new SupportingFile("akka-http-server/defaultJsonFormats.mustache", apiPackageFolder, "DefaultJsonFormats.scala"));

    }


    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "scala-jaxrs-akka-http-server";
    }

    @Override
    public String getHelp() {
        return "Generates a Scala Akka-Http JAX-RS Specification based server";
    }
}
