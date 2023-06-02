
package esignature;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import signature.chemistry.Molecule;
import signature.chemistry.MoleculeReader;
import signature.chemistry.MoleculeSignature;

public class Main {
    public static void main(String[] args) throws Exception {
        // Set switches to be used
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("i", "input", true, "Input v2000 SD file");
        options.addOption("d", "depth", true, "Depth of the signature of each vertex (default: 3)");
        options.addOption("h", "help", false, "Shows this Help");

        try {
            // Parse switches
            CommandLine commandLine = parser.parse(options, args);
            // Display help
            if (commandLine.hasOption("help")) {
                new HelpFormatter().printHelp("java -jar eSignature.jar", options);
            } else if (!commandLine.hasOption("input")) {
                // No input given
                throw new Exception("Input V3000 SD file must be provided.");
            } else {
                // Get signature depth
                int depth;
                if (commandLine.hasOption("depth")) {
                    depth = Integer.parseInt(commandLine.getOptionValue("depth"));
                } else {
                    depth = 3;
                }
                // Read content of file
                List<Molecule> molecules = MoleculeReader.readSDFFile(commandLine.getOptionValue("input"));
                for (Molecule molecule : molecules) {
                    // Obtain signature
                    MoleculeSignature signature = new MoleculeSignature(molecule);
                    List<String> signatures = new ArrayList<>();
                    // Iterate vertices
                    int i = 0;
                    while (i < signature.getVertexCount()) {
                        signatures.add(String.join("\t", String.valueOf(i), signature.signatureStringForVertex(i, depth)));
                        i++;
                    }
                    System.out.println(String.join("\n", signatures));
                    System.out.println();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}