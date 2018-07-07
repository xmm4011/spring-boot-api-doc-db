package top.webdevelop.gull.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.webdevelop.gull.apidoc.APIDoc;
import top.webdevelop.gull.apidoc.APIDocProject;
import top.webdevelop.gull.apidoc.APIDocService;
import top.webdevelop.gull.common.Errors;
import top.webdevelop.gull.common.Response;
import top.webdevelop.gull.common.ResponseGen;
import top.webdevelop.gull.exception.APIDocException;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by xumingming on 2018/6/13.
 */
@RestController
@RequestMapping("/api-doc")
public class APIDocController {
    @Autowired
    private APIDocService apiDocService;

    @GetMapping("/project")
    public Response<List<APIDocProject>> projects() {
        return ResponseGen.genSuccessResult(apiDocService.findAllProjectDetail());
    }

    @GetMapping("/project/{projectName}")
    public Response<APIDocProject> projects(@PathVariable String projectName) {
        return ResponseGen.genSuccessResult(apiDocService.findAPIDocProjectDetail(projectName, false));
    }

    @GetMapping("/{apiDocId}")
    public Response<APIDoc> apiDoc(@PathVariable String apiDocId) {
        return ResponseGen.genSuccessResult(apiDocService.findAPIDocDetail(apiDocId));
    }

    @PutMapping("/menu/{menuId}")
    public Response modifyMenuDesc(@PathVariable String menuId, @RequestBody @Valid APIDocDescParam param) {
        int result = apiDocService.updateMenuDesc(menuId, param.getDesc(), param.getVersion());
        if (result < 1) {
            throw new APIDocException(Errors.DATA_EXPIRE_ERROR);
        }

        return ResponseGen.genSuccessResult();
    }

    @PutMapping("/field/{fieldId}")
    public Response modifyFieldDesc(@PathVariable String fieldId, @RequestBody @Valid APIDocDescParam param) {
        int result = apiDocService.updateFieldDesc(fieldId, param.getDesc(), param.getVersion());
        if (result < 1) {
            throw new APIDocException(Errors.DATA_EXPIRE_ERROR);
        }

        return ResponseGen.genSuccessResult();
    }
}
