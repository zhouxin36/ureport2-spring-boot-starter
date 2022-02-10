package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class BuildFieldsReq extends DataSourceReq{

    private String sql;
    private String parameters;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "BuildFieldsReq{" +
                "driver='" + getDriver() + '\'' +
                ", name='" + getName() + '\'' +
                ", parameters='" + parameters + '\'' +
                ", password='" + getPassword() + '\'' +
                ", sql='" + sql + '\'' +
                ", type='" + getType() + '\'' +
                ", url='" + getUrl() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}
