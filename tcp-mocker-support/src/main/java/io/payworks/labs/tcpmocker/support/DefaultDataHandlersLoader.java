package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableMap;
import io.payworks.labs.tcpmocker.datahandler.*;
import io.payworks.labs.tcpmocker.order.Ordered;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModelReader;
import io.payworks.labs.tcpmocker.support.json.JsonMappingReader;
import io.payworks.labs.tcpmocker.support.resource.DefaultResourceLoader;
import io.payworks.labs.tcpmocker.support.resource.ResourceLoader;
import io.payworks.labs.tcpmocker.support.yml.YamlMappingReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DefaultDataHandlersLoader implements DataHandlersLoader {

    public static final ImmutableMap<Pattern, DataHandlerModelReader> DEFAULT_READERS_MAP = ImmutableMap.of(
            Pattern.compile(".+\\.json"), new JsonMappingReader(),
            Pattern.compile(".+\\.ya?ml"), new YamlMappingReader()
    );
    public static final String DEFAULT_MAPPINGS_PATH = "classpath:/tcp-mappings/";

    private final ResourceLoader resourceLoader;

    private String mappingsPath = DEFAULT_MAPPINGS_PATH;
    private Map<Pattern, DataHandlerModelReader> readersMap = DEFAULT_READERS_MAP;

    public DefaultDataHandlersLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public DefaultDataHandlersLoader() {
        this(new DefaultResourceLoader());
    }

    public void setMappingsPath(final String mappingsPath) {
        this.mappingsPath = mappingsPath;
    }

    public void setReadersMap(final Map<Pattern, DataHandlerModelReader> readersMap) {
        this.readersMap = ImmutableMap.copyOf(readersMap);
    }

    @Override
    public Map<String, OrderedDataHandler> dataHandlers(final Set<String> filter) {
        final List<String> dirList = resourceLoader.list(mappingsPath);

        final Map<String, OrderedDataHandler> dataHandlers = new LinkedHashMap<>();

        readersMap.forEach((pattern, reader) -> {
            final Predicate<String> patternPredicate = pattern.asPredicate();
            final List<String> filePaths = dirList.stream()
                    .filter(filePath -> !filter.contains(filePath) && patternPredicate.test(filePath))
                    .collect(Collectors.toList());

            for (final String filePath : filePaths) {
                try (final InputStream inputStream = resourceLoader.getInputStream(filePath)) {
                    final DataHandlerModel dataHandlerModel = reader.read(inputStream);

                    dataHandlers.put(filePath, createOrderedDataHandler(dataHandlerModel));
                } catch (final IOException e) {
                    throw new DataHandlersLoaderException(e);
                }
            }
        });

        return Collections.unmodifiableMap(dataHandlers);
    }

    private OrderedDataHandler createOrderedDataHandler(final DataHandlerModel dataHandlerModel) {
        final DataHandler dataHandler = createDataHandler(dataHandlerModel);

        final int order = getOrder(dataHandlerModel);

        return new OrderedDataHandler(order, dataHandler);
    }

    private DataHandler createDataHandler(final DataHandlerModel dataHandlerModel) {
        final DataHandlerType dataHandlerType = getDataHandlerType(dataHandlerModel);
        final String hexRegex = dataHandlerModel.getRequest().getMatches();
        final String responseData = dataHandlerModel.getResponse().getData();

        switch (dataHandlerType) {
            case DYNAMIC_HEX_REGEX:
                return new DynamicHexRegexDataHandler(hexRegex, responseData);

            case STATIC_HEX_REGEX:
                return new StaticHexRegexDataHandler(hexRegex, responseData);

            default:
                throw new UnsupportedOperationException(String.format("Unknown DataHandler Type: %s", dataHandlerType));
        }
    }

    private int getOrder(final DataHandlerModel dataHandlerModel) {
        final Integer order = dataHandlerModel.getOrder();
        return order == null ? Ordered.DEFAULT_ORDER : order;
    }

    private DataHandlerType getDataHandlerType(final DataHandlerModel dataHandlerModel) {
        final DataHandlerType handlerType = dataHandlerModel.getHandlerType();
        return handlerType == null ? DataHandlerType.STATIC_HEX_REGEX : handlerType;
    }
}
