package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.cache.ResourceCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@Controller
public class ImageController extends AbstractController {

    public static final String PREFIX = CONTENT + "/image";

    public ImageController(ObjectMapper objectMapper) {
        super(objectMapper);
    }


    @RequestMapping(value = PREFIX, method = RequestMethod.GET)
    public void loadMethods(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String key = req.getParameter("_key");
        if (StringUtils.isNotBlank(key)) {
            byte[] bytes = (byte[]) ResourceCache.getObject(key);
            InputStream input = new ByteArrayInputStream(bytes);
            OutputStream output = resp.getOutputStream();
            resp.setContentType("image/png");
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        }  //processImage(req, resp);

    }

}
