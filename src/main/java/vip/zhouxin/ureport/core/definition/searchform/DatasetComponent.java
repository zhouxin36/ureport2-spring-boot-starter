package vip.zhouxin.ureport.core.definition.searchform;

import org.apache.commons.lang3.StringUtils;
import vip.zhouxin.ureport.core.Utils;
import vip.zhouxin.ureport.core.build.Dataset;
import vip.zhouxin.ureport.core.exception.DatasetUndefinitionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xinxingzhou
 * @since 2022/2/23
 */
public abstract class DatasetComponent extends InputComponent{
    private boolean useDataset;
    private String dataset;
    private String labelField;
    private String valueField;
    private List<Option> options;
    public List<Option> generateOption(RenderContext context){
        if(isUseDataset() && StringUtils.isNotBlank(getDataset())){
            Dataset ds=context.getDataset(getDataset());
            if(ds==null){
                throw new DatasetUndefinitionException(getDataset());
            }
            List<Option> genOptions = new ArrayList<>();
            for(Object obj:ds.getData()){
                String label= Optional.ofNullable(Utils.getProperty(obj, getLabelField())).map(Object::toString).orElse(null);
                String value= Optional.ofNullable(Utils.getProperty(obj, getValueField())).map(Object::toString).orElse(null);
                genOptions.add(new Option(label,value));
            }
            return genOptions;
        }else {
            return options;
        }
    }

    public boolean isUseDataset() {
        return useDataset;
    }

    public void setUseDataset(boolean useDataset) {
        this.useDataset = useDataset;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
