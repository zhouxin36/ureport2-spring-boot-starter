package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class ParseDatasetNameRes {
    private String datasetName;

    public ParseDatasetNameRes(String datasetName) {
        this.datasetName = datasetName;
    }

    public ParseDatasetNameRes() {
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @Override
    public String toString() {
        return "ParseDatasetNameRes{" +
                "datasetName='" + datasetName + '\'' +
                '}';
    }
}
