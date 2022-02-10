package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.core.chart.ChartData;
import vip.zhouxin.ureport.console.dto.StoreDataReq;
import vip.zhouxin.ureport.core.utils.UnitUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class ChartController extends AbstractController {

    public static final String PREFIX = CONTENT + "/chart";

    public ChartController(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @RequestMapping(value = PREFIX + "/storeData", method = RequestMethod.POST)
    public void storeData(@RequestBody StoreDataReq req) {
        ChartData chartData = CacheUtils.getChartData(req.getChartId());
        if (chartData == null) {
            return;
        }
        String base64Data = req.getBase64Data();
        String prefix = "data:image/png;base64,";
        if (base64Data != null) {
            if (base64Data.startsWith(prefix)) {
                base64Data = base64Data.substring(prefix.length());
            }
        }
        chartData.setBase64Data(base64Data);
        String width = req.getWidth();
        String height = req.getHeight();
        chartData.setHeight(UnitUtils.pixelToPoint(Integer.parseInt(height)));
        chartData.setWidth(UnitUtils.pixelToPoint(Integer.parseInt(width)));
    }
}
