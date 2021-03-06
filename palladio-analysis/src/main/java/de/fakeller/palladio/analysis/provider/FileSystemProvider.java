package de.fakeller.palladio.analysis.provider;

import de.fakeller.palladio.analysis.PcmProvider;
import de.fakeller.palladio.config.PcmModelConfig;
import de.fakeller.palladio.environment.PalladioEclipseEnvironment;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;
import org.palladiosimulator.solver.models.PCMInstance;

import java.util.ArrayList;

/**
 * Provides a PCM instance by deserializing the XML representation stored on the file system.
 */
public class FileSystemProvider implements PcmProvider {

    private PcmModelConfig config;

    public FileSystemProvider(PcmModelConfig config) {
        this.config = config;
    }

    @Override
    public PCMInstance provide() {
        PCMResourceSetPartition resourceSetPartition = new PCMResourceSetPartition();
        // TODO: inject URI converter, instead of fetching from singleton
        resourceSetPartition.getResourceSet().setURIConverter(PalladioEclipseEnvironment.INSTANCE.getUriConverter());

        ArrayList<String> fileList = new ArrayList<String>();
        fileList.add(this.config.getUsageModel());
        fileList.add(this.config.getAllocationModel());

        resourceSetPartition.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);
        for (String modelFile : fileList) {
            resourceSetPartition.loadModel(modelFile);
        }
        resourceSetPartition.resolveAllProxies();

        return new PCMInstance(resourceSetPartition);
    }
}
