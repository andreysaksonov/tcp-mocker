package io.payworks.labs.tcpmocker.support.resource;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ResourceUtils {

    private static final String RESOURCE_SCHEME = "classpath:";
    private static final String FILE_SCHEME = "file:";

    private ResourceUtils() {
    }

    public static URL toResourceUrl(final Class<?> contextClass, final String path) {
        try {
            final String resourceName = path.substring(RESOURCE_SCHEME.length());
            final ClassLoader context = contextClass == null ? null : contextClass.getClassLoader();

            return IOUtils.resourceToURL(resourceName, context);
        } catch (final Exception e) {
            throw asResourceException(e);
        }
    }

    public static URL toResourceUrl(final String path) {
        return toResourceUrl(null, path);
    }

    public static Path toFilePath(final String path) {
        return Paths.get(path.substring(FILE_SCHEME.length()));
    }

    public static List<String> listDirectory(final String dirPath) {
        if (dirPath.startsWith(RESOURCE_SCHEME)) {
            try {
                final URL resourceUrl = toResourceUrl(dirPath);

                try (final InputStream is = resourceUrl.openStream()) {
                    final List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_8);

                    return lines.stream()
                            .map(entry -> concat(dirPath, entry))
                            .collect(Collectors.toList());
                }
            } catch (final Exception e) {
                throw asResourceException(e);
            }
        } else if (dirPath.startsWith(FILE_SCHEME)) {
            final Path filePath = toFilePath(dirPath);

            return listPath(filePath).stream()
                    .map(path -> FILE_SCHEME + path)
                    .collect(Collectors.toList());
        } else {
            return listPath(Paths.get(dirPath));
        }
    }

    public static byte[] toByteArray(final String path) {
        try {
            if (path.startsWith(RESOURCE_SCHEME)) {
                return IOUtils.toByteArray(toResourceUrl(path));
            } else if (path.startsWith(FILE_SCHEME)) {
                return Files.readAllBytes(toFilePath(path));
            } else {
                return Files.readAllBytes(Paths.get(path));
            }
        } catch (final Exception e) {
            throw asResourceException(e);
        }
    }

    public static InputStream getInputStream(final String path) {
        try {
            if (path.startsWith(RESOURCE_SCHEME)) {
                return toResourceUrl(path).openStream();
            } else if (path.startsWith(FILE_SCHEME)) {
                return Files.newInputStream(toFilePath(path));
            } else {
                return Files.newInputStream(Paths.get(path));
            }
        } catch (final Exception e) {
            throw asResourceException(e);
        }
    }

    public static String concat(String basePath, String path) {
        basePath = basePath.endsWith(pathSeparator())
                ? basePath.substring(0, basePath.length() - 1)
                : basePath;

        path = path.startsWith(pathSeparator())
                ? path.substring(1)
                : path;

        return basePath + pathSeparator() + path;
    }

    // Helpers

    private static List<String> listPath(final Path dirPath) {
        try (final Stream<Path> list = Files.list(dirPath)) {

            return list
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (final Exception e) {
            throw asResourceException(e);
        }
    }

    private static String pathSeparator() {
        return "/";
    }

    private static ResourceException asResourceException(final Exception e) {
        if (e instanceof ResourceException) {
            return (ResourceException) e;
        } else {
            return new ResourceException(e);
        }
    }
}
