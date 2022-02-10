package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class ParseDatasetNameReq {

    private String expr;

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "ParseDatasetNameReq{" +
                "expr='" + expr + '\'' +
                '}';
    }
}
