/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package vip.zhouxin.ureport.core.provider.report.file;

import org.apache.commons.io.IOUtils;
import vip.zhouxin.ureport.core.exception.ReportException;
import vip.zhouxin.ureport.core.provider.report.ReportFile;
import vip.zhouxin.ureport.core.provider.report.ReportProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author xinxingzhou
 * @since 2022/2/10
 */
public class CacheReportProvider implements ReportProvider {
    private final String prefix = "cache:";
    private String fileStoreDir = System.getProperty("java.io.tmpdir");

    public CacheReportProvider() {
    }

    @Override
    public InputStream loadReport(String file) {
        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length());
        }
        String fullPath = fileStoreDir + "/" + file;
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new ReportException(e);
        }
    }

    @Override
    public void deleteReport(String file) {
        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length());
        }
        String fullPath = fileStoreDir + "/" + file;
        File f = new File(fullPath);
        if (f.exists()) {
            f.delete();
        }
    }

    @Override
    public List<ReportFile> getReportFiles() {
        File file = new File(fileStoreDir);
        List<ReportFile> list = new ArrayList<>();
        for (File f : file.listFiles()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(f.lastModified());
            list.add(new ReportFile(f.getName(), calendar.getTime()));
        }
        list.sort((f1, f2) -> f2.getUpdateDate().compareTo(f1.getUpdateDate()));
        return list;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void saveReport(String file, String content) {
        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length());
        }
        String fullPath = fileStoreDir + "/" + file;
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(fullPath);
            IOUtils.write(content, outStream, StandardCharsets.UTF_8.displayName());
        } catch (Exception ex) {
            throw new ReportException(ex);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean disabled() {
        return false;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
