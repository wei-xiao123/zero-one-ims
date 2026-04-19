package com.zeroone.star.plugin.codegen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class CodegenMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("mp-code-generator-plugin local placeholder: no-op generate goal executed.");
    }
}
