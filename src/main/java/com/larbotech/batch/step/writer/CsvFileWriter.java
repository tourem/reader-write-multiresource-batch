package com.larbotech.batch.step.writer;

import static java.lang.System.getProperty;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.*;

import com.larbotech.batch.model.EndFile;
import com.larbotech.batch.model.Line;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CsvFileWriter implements ItemWriter<Line> {

  private Writer writer;

  private static final int bufferSize = 8 * 1024;

  @Value("${out.directory}")
  private String outDirectory;

  @Override
  public void write(List<? extends Line> items) throws Exception {

    initializeIfNecessary(items);

    requireNonNull(writer, "writer not nullable");

    boolean endFile = writeLines(items);

    if (endFile) {
      writer.close();
      writer = null;
    }
  }

  private void initializeIfNecessary(List<? extends Line> items) throws Exception {
    if (writer == null && !isEmpty(items)) {
      createNewFile(items.get(0));
    }
  }

  private boolean writeLines(List<? extends Line> items) throws IOException {
    boolean endFile = false;
    for (Line line : items) {
      if (line instanceof EndFile) {
        endFile = true;
      } else if (line.isValid() && !StringUtils.isEmpty(line.getValue())){
        writer.append(line.getValue()).append(getProperty("line.separator"));
      }
    }
    return endFile;
  }

  private void createNewFile(Line line) throws Exception {
    writer =
        new BufferedWriter(
            new FileWriter(
                outDirectory + File.separator + createValidFileName(line.getOriginFileName())),
            bufferSize
        );

    writer.append(line.getHeader()).append(getProperty("line.separator"));
  }

  private String createValidFileName(String originFileName) {
    return "valid_" + originFileName;
  }
}
