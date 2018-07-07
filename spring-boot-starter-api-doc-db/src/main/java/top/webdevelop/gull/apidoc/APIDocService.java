package top.webdevelop.gull.apidoc;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by xumingming on 2018/6/13.
 */
@Transactional(readOnly = true)
public class APIDocService {
    private APIDocDao apiDocDao;

    public APIDocService() {
        this.apiDocDao = new APIDocDao();
    }

    public APIDocService(APIDocDao apiDocDao) {
        this.apiDocDao = apiDocDao;
    }

    public List<APIDocProject> findAllProjectDetail() {
        return apiDocDao.findAllProject();
    }

    public APIDocProject findAPIDocProjectDetail(String name, boolean loadDoc) {
        APIDocProject project = apiDocDao.findProjectByName(name);
        if (project != null) {
            project.setMenus(findAPIDocMenuDetail(project, null, loadDoc));
        }
        return project;
    }

    public List<APIDocProject> findProjectDeletedByName(String name) {
        return apiDocDao.findProjectDeletedByName(name);
    }

    public Set<APIDocMenu> findAPIDocMenuDetail(APIDocProject project, APIDocMenu parent, boolean loadDoc) {
        List<APIDocMenu> menus = apiDocDao.findAllAPIDocMenuByProjectAndParent(project, parent);
        menus.forEach(menu -> {
            menu.setChilds(findAPIDocMenuDetail(project, menu, loadDoc));
            if (loadDoc) {
                menu.setApiDoc(findAPIDocDetail(menu.getApiDocId()));
            }
        });
        return new TreeSet<>(menus);
    }

    public APIDoc findAPIDocDetail(String id) {
        APIDoc apiDoc = apiDocDao.findAPIDocById(id);
        if (apiDoc != null) {
            apiDoc.setRequest(findAPIDocFieldDetail(apiDoc, null, APIDocFieldParamsType.request));
            apiDoc.setResponse(findAPIDocFieldDetail(apiDoc, null, APIDocFieldParamsType.response));
        }
        return apiDoc;
    }

    public List<APIDocField> findAPIDocFieldDetail(APIDoc apiDoc, APIDocField parent, APIDocFieldParamsType paramsType) {
        List<APIDocField> fields = apiDocDao.findAllAPIDocFieldByDocAndParentAndParamsType(apiDoc, parent, paramsType);
        fields.forEach(field -> field.setChilds(findAPIDocFieldDetail(apiDoc, field, paramsType)));
        return fields;
    }

    @Transactional
    public void deleteAPIDocProject(APIDocProject project) {
        apiDocDao.deleteProject(project);
        apiDocDao.deleteMenuByProject(project);
        apiDocDao.deleteDocByProject(project);
        apiDocDao.deleteFieldByProject(project);
    }

    @Transactional
    public void dropAPIDocProject(APIDocProject project) {
        apiDocDao.dropProject(project);
        apiDocDao.dropMenuByProject(project);
        apiDocDao.dropDocByProject(project);
        apiDocDao.dropFieldByProject(project);
    }

    @Transactional
    public void saveAPIDocProjectDetail(APIDocProject project) {
        apiDocDao.insertProject(project);
        saveAPIDocMenuDetail(project, null, project.getMenus());
    }

    @Transactional
    public int updateMenuDesc(String menuId, String desc, Long version) {
        return apiDocDao.updateMenuDesc(menuId, desc, version);
    }

    @Transactional
    public int updateFieldDesc(String fieldId, String desc, Long version) {
        return apiDocDao.updateFieldDesc(fieldId, desc, version);
    }

    private void saveAPIDocMenuDetail(APIDocProject project, APIDocMenu parent, Set<APIDocMenu> menus) {
        menus.forEach(menu -> {
            apiDocDao.insertMenu(project, parent, menu);
            saveAPIDocMenuDetail(project, menu, menu.getChilds());
            if (menu.getApiDoc() == null) {
                return;
            }
            apiDocDao.insertDoc(project, menu.getApiDoc());
            saveAPIDocFieldDetail(project, menu.getApiDoc(), null, menu.getApiDoc().getRequest(), APIDocFieldParamsType.request);
            saveAPIDocFieldDetail(project, menu.getApiDoc(), null, menu.getApiDoc().getResponse(), APIDocFieldParamsType.response);
        });
    }

    private void saveAPIDocFieldDetail(APIDocProject project, APIDoc doc, APIDocField parent, List<APIDocField> fields, APIDocFieldParamsType paramsType) {
        fields.forEach(field -> {
            apiDocDao.insertField(project, doc, parent, field, paramsType);
            saveAPIDocFieldDetail(project, doc, field, field.getChilds(), paramsType);
        });
    }
}
