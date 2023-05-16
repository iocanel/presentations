//usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 17+ 
//DEPS info.picocli:picocli:4.6.1
//DEPS org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r
//DEPS org.slf4j:slf4j-simple:1.6.1
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.*;

@SuppressWarnings("deprecation")
@Command(name = "present", mixinStandardHelpOptions = true, version = "1.0", description = "Serves reveal.js content via HTTP")
class present implements Runnable  {

    private static final String JBANG_PRESENTATION_REPO = "https://github.com/iocanel/reveal.js.git";
    private static final String JBANG_PRESENTATION_DIR = "JBANG_PRESENTATION_DIR";
    private static final String SLIDES_FILENAME = "slides.md";

    @Parameters(index = "0", description = "The presentation file")
    private File slidesFile;

    @Option(names = {"-p", "--port"}, defaultValue = "8000", description = "The server port")
    private int port;

    @Option(names = {"-d", "--presentation-dir"}, description = "A local presentation directory to use")
    private Optional<File> localPresentationDir = Optional.empty();

    public static void main(String... args) {
        CommandLine.run(new present(), args);
    }

    @Override
    public void run() {
         try {
            Path presentationDir = localPresentationDir.map(File::toPath)
            .or(() -> getPresentationDirFromEnv()) 
            .orElseGet(() -> clonePresentationRepo(JBANG_PRESENTATION_REPO));

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String requestPath = exchange.getRequestURI().getPath();
                File root = presentationDir.toFile();
                //1. Redirect / -> /index.html
                if (requestPath.equals("/")) {
                   requestPath = "/index.html";
                }
                //2. Redirect /slides.md -> /src/$slidesFile
                if (requestPath.startsWith("/slides.md")) {
                    requestPath = "/src/" + slidesFile.getName() ;
                }
                //3. Redirect /src/foo -> /foo
                if (requestPath.startsWith("/src")) {
                    root = slidesFile.getParentFile();
                    requestPath = requestPath.substring(4);
                }
                //4. Else serve request from root that should match presentDir
                File requestedFile = new File(root, requestPath.substring(1));
                if (requestedFile.exists() && requestedFile.isFile()) {
                    byte[] fileBytes = Files.readAllBytes(requestedFile.toPath());
                    exchange.sendResponseHeaders(200, fileBytes.length);
                    exchange.getResponseBody().write(fileBytes);
                    exchange.getResponseBody().close();
                } else {
                    String response = "File not found: " + requestPath;
                    exchange.sendResponseHeaders(404, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                }
            });
            server.start();
            System.out.println("Presentation served at: http://localhost:" + port);
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    /**
     * Get the {@link Path} of that 'JBANG_PRESENTATION_DIR` points to.
     * @return the {@link Path} wrapped in {@link @Optional}.
     **/
    private static Optional<Path> getPresentationDirFromEnv() {
        return Optional.ofNullable(System.getenv(JBANG_PRESENTATION_DIR)).map(d -> Paths.get(d));
    }

    /**
     * Clone the reveal.js repository to temporary directory
     * @param repositoryUrl The url of the repository to clone.
     * @return the {@link Path} to the directory
     **/
    private static Path clonePresentationRepo(String repositoryUrl) {
           System.out.println("Cloning reveal.js repository:" + repositoryUrl);
           try {
               Path cloneDir = Files.createTempDirectory("presentation-jbang");
                // Clone the repository
                CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(cloneDir.toFile());
                Git git = cloneCommand.call();
                Repository repository = git.getRepository();

                // Clear the Git configuration
                File dotGit = repository.getDirectory();
                File config = new File(dotGit, Constants.CONFIG);
                config.delete();

                return dotGit.getParentFile().toPath();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}

