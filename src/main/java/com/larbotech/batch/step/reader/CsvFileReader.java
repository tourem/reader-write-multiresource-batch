package com.larbotech.batch.step.reader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ArrayUtils.get;
import static org.apache.commons.lang3.ArrayUtils.getLength;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import com.larbotech.batch.dao.BatchDao;
import com.larbotech.batch.model.ContextReader;
import com.larbotech.batch.model.EndFile;
import com.larbotech.batch.model.Line;
import com.larbotech.batch.service.BatchService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@Slf4j
@StepScope
@Component
public class CsvFileReader extends SimpleCompletionPolicy implements ItemReader<Line> {


  public static final int DEFAULT_CHUNK_SIZE = 10;

  @Value("${in.directory}/")
  private String inDirectory;

  private Resource[] inputResources;

  private ICsvMapReader delegate;
  private int indexCurrentResource;
  private Resource currentResource;
  private final ContextReader contextReader;


  @PostConstruct
  private void initialize() throws Exception {
    Path inDirectoryPath = Paths.get(inDirectory);
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    inputResources = resolver.getResources(inDirectoryPath.toUri().toString() + "**/*.csv");
  }

  @Autowired
  public CsvFileReader(
          @Value("#{jobParameters['batchName']}") String batchName,
    BatchService batchService, BatchDao batchDao) {
    contextReader = batchService.getContextReader(batchDao.findByName(batchName));
    super.setChunkSize(DEFAULT_CHUNK_SIZE);
  }

  @Override
  public Line read()
      throws Exception {

    String line;

    if (isEmpty(inputResources)) {
      return null;
    }

    if (delegate == null && isNotEmpty(inputResources)
        && getLength(inputResources) > indexCurrentResource) {
      restReader();
    } else if (delegate == null) {
      return null;
    }

    requireNonNull(delegate, "reader delegate not nullable");
    requireNonNull(currentResource, "current resource not nullable");

    try {
      delegate
          .read(contextReader.getHeaders(), contextReader.getProcessors());
      line = delegate.getUntokenizedRow();

    } catch (SuperCsvException superCsvException) {
      return new Line("", currentResource.getFile().getName(), contextReader.getHeaders());
    }

    if (StringUtils.isEmpty(line)) {
      delegate.close();
      delegate = null;
      return new EndFile(currentResource.getFile().getName(), contextReader.getHeaders());
    }
    return new Line(line, true, currentResource.getFile().getName(), contextReader.getHeaders());
  }

  private void restReader() throws IOException {
    currentResource = get(inputResources, indexCurrentResource);
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(currentResource.getInputStream(),
            UTF_8));
    delegate = new CsvMapReader(
        bufferedReader, CsvPreference.STANDARD_PREFERENCE);
    indexCurrentResource++;
    String header = bufferedReader.readLine(); //read header
    log.info("header: " + header);
  }

  @Override
  public boolean isComplete(RepeatContext context) {
    return delegate == null || super.isComplete(context);
  }

  @Override
  public boolean isComplete(RepeatContext context, RepeatStatus result) {
    return delegate == null || super.isComplete(context, result);
  }

}
